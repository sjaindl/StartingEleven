package com.sjaindl.s11.players

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sjaindl.s11.core.firestore.player.PlayerRepository
import com.sjaindl.s11.core.firestore.player.model.Player
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

data class PlayerWithLineupCount(
    val player: Player,
    val lineupCount: Int,
)

sealed class PlayerState {
    data object Initial: PlayerState()
    data class Loading(val playerName: String? = null): PlayerState()
    data class Success(val players: List<PlayerWithLineupCount>): PlayerState()
    data class Error(val message: String): PlayerState()
}

class PlayerViewModel : ViewModel(), KoinComponent {

    private val playerRepository: PlayerRepository by inject()
    private val calculatePlayerLineupsUseCase: CalculatePlayerLineupsUseCase by inject()

    private var _playerState: MutableStateFlow<PlayerState> = MutableStateFlow(PlayerState.Initial)
    var playerState = _playerState.asStateFlow()

    fun loadPlayers() {
        _playerState.value = PlayerState.Loading()

        viewModelScope.launch {
            _playerState.value = PlayerState.Success(
                players = playerRepository.getPlayers().sortedByDescending {
                    it.points.values.sum()
                }.map { player ->
                    _playerState.value = PlayerState.Loading(playerName = player.name)

                    val lineupCount = calculatePlayerLineupsUseCase.calculate(playerId = player.playerId)

                    PlayerWithLineupCount(
                        player = Player(
                            playerId = player.playerId,
                            name = player.name,
                            position = player.position,
                            imageRef = player.imageRef,
                            downloadUrl = player.downloadUrl,
                            points = player.points,
                        ),
                        lineupCount = lineupCount,
                    )
                }
            )
        }
    }
}
