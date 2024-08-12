package com.sjaindl.s11

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sjaindl.s11.core.player.PlayerRepository
import com.sjaindl.s11.core.player.model.Player
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

sealed class PlayerState {
    data object Initial: PlayerState()
    data object Loading: PlayerState()
    data class Success(val players: List<Player>): PlayerState()
    data class Error(val message: String): PlayerState()
}

class PlayerViewModel : ViewModel(), KoinComponent {

    private val playerRepository: PlayerRepository by inject()

    private var _playerState: MutableStateFlow<PlayerState> = MutableStateFlow(PlayerState.Initial)
    var playerState = _playerState.asStateFlow()

    fun loadPlayers() {
        _playerState.value = PlayerState.Loading

        viewModelScope.launch {
            _playerState.value = PlayerState.Success(players = playerRepository.getPlayers())
        }
    }
}
