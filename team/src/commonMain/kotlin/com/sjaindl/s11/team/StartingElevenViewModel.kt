package com.sjaindl.s11.team

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sjaindl.s11.core.firestore.formations.FormationRepository
import com.sjaindl.s11.core.firestore.player.PlayerRepository
import com.sjaindl.s11.core.firestore.player.model.Player
import com.sjaindl.s11.core.firestore.user.UserRepository
import com.sjaindl.s11.core.firestore.userlineup.UserLineupRepository
import com.sjaindl.s11.core.firestore.userlineup.model.LineupData
import com.sjaindl.s11.team.model.Formation
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

sealed class StartingElevenState {
    data object Initial: StartingElevenState()
    data object Loading: StartingElevenState()
    data class Content(
        val possibleFormations: List<Formation>,
        val formation: Formation,
        val players: List<Player>,
        val lineup: LineupData,
    ): StartingElevenState()
    data class Error(val message: String): StartingElevenState()
}

class StartingElevenViewModel : ViewModel(), KoinComponent {

    private val tag = "StartingElevenViewModel"

    private val formationRepository: FormationRepository by inject()
    private val userRepository: UserRepository by inject()
    private val playerRepository: PlayerRepository by inject()
    private val lineupRepository: UserLineupRepository by inject()

    private var _startingElevenState: MutableStateFlow<StartingElevenState> = MutableStateFlow(
        StartingElevenState.Initial
    )
    var startingElevenState = _startingElevenState.asStateFlow()

    init {
        loadTeam()
    }

    fun loadTeam() = viewModelScope.launch {
        _startingElevenState.value = StartingElevenState.Loading

        val currentUser = userRepository.getCurrentUser()
        if (currentUser == null) {
            _startingElevenState.value = StartingElevenState.Error(message = "Please sign in")
            return@launch
        }

        try {
            val possibleFormations = formationRepository.getFormations().map {
                Formation(
                    formationId = it.formation,
                    defenders = it.defense,
                    midfielders = it.midfield,
                    attackers = it.attack,
                )
            }
            val userFormation = possibleFormations.find {
                it.formationId == currentUser.formation
            } ?: Formation.defaultFormation
            val players = playerRepository.getPlayers()
            val lineupData = lineupRepository.getUserLineup(uid = currentUser.uid)

            _startingElevenState.value = StartingElevenState.Content(
                possibleFormations = possibleFormations,
                formation = userFormation,
                players = players,
                lineup = lineupData,
            )
        } catch (exception: Exception) {
            val message = exception.message ?: exception.toString()
            Napier.e(message = message, throwable = exception, tag = tag)
            _startingElevenState.value = StartingElevenState.Error(message = message)
        }
    }
}
