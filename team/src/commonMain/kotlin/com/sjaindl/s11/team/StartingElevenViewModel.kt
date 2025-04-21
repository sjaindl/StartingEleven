package com.sjaindl.s11.team

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sjaindl.s11.core.Event
import com.sjaindl.s11.core.EventRepository
import com.sjaindl.s11.core.extensions.insertAt
import com.sjaindl.s11.core.firestore.config.ConfigRepository
import com.sjaindl.s11.core.firestore.formations.FormationRepository
import com.sjaindl.s11.core.firestore.player.PlayerRepository
import com.sjaindl.s11.core.firestore.player.model.Position
import com.sjaindl.s11.core.firestore.user.UserRepository
import com.sjaindl.s11.core.firestore.userlineup.UserLineupRepository
import com.sjaindl.s11.core.firestore.userlineup.model.LineupData
import com.sjaindl.s11.players.CalculatePlayerLineupsUseCase
import com.sjaindl.s11.players.PlayerWithLineupCount
import com.sjaindl.s11.team.model.Formation
import io.github.aakira.napier.Napier
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

sealed class StartingElevenState {
    data object Initial: StartingElevenState()
    data object Loading: StartingElevenState()
    data class Content(
        val season: String?,
        val possibleFormations: List<Formation>,
        val formation: Formation,
        val playersWithLineupCount: List<PlayerWithLineupCount>,
        val lineup: LineupData,
        val enabled: Boolean,
    ): StartingElevenState()
    data class Error(val message: String): StartingElevenState()
}

@KoinViewModel
class StartingElevenViewModel : ViewModel(), KoinComponent {

    private val tag = "StartingElevenViewModel"

    private val configRepository: ConfigRepository by inject()
    private val formationRepository: FormationRepository by inject()
    private val userRepository: UserRepository by inject()
    private val playerRepository: PlayerRepository by inject()
    private val lineupRepository: UserLineupRepository by inject()
    private val eventRepository: EventRepository by inject()

    private val calculatePlayerLineupsUseCase: CalculatePlayerLineupsUseCase by inject()

    private var _startingElevenState: MutableStateFlow<StartingElevenState> = MutableStateFlow(
        StartingElevenState.Initial
    )
    var startingElevenState = _startingElevenState.asStateFlow()

    private val _showSnackBar = MutableStateFlow<Boolean>(false)
    val showSnackBar: StateFlow<Boolean> = _showSnackBar

    init {
        loadTeam()
        initObservers()
    }

    fun loadTeam() = viewModelScope.launch {
        _startingElevenState.value = StartingElevenState.Loading

        val currentUser = userRepository.getCurrentUser()
        if (currentUser == null) {
            _startingElevenState.value = StartingElevenState.Error(message = "Please sign in")
            return@launch
        }

        configRepository.getConfigFlow().collect { config ->
            try {
                val enabled = config?.freeze?.not() ?: false

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

                val playersWithLineupCount = playerRepository.getPlayers(onlyActive = true).map { player ->
                    val lineupCount = calculatePlayerLineupsUseCase.calculate(playerId = player.playerId)
                    PlayerWithLineupCount(
                        player = player,
                        lineupCount = lineupCount,
                    )
                }

                val lineupData = lineupRepository.getUserLineup(uid = currentUser.uid)

                _startingElevenState.value = StartingElevenState.Content(
                    season = config?.season,
                    possibleFormations = possibleFormations,
                    formation = userFormation,
                    playersWithLineupCount = playersWithLineupCount,
                    lineup = lineupData,
                    enabled = enabled,
                )
            } catch (exception: Exception) {
                val message = exception.message ?: exception.toString()
                Napier.e(message = message, throwable = exception, tag = tag)
                _startingElevenState.value = StartingElevenState.Error(message = message)
            }
        }
    }

    fun onFormationSelected(formation: Formation) {
        val state = startingElevenState.value as? StartingElevenState.Content ?: return
        if (formation == state.formation) return

        _startingElevenState.value = state.copy(
            formation = formation,
            lineup = LineupData(
                goalkeeper = null,
                defenders = emptyList(),
                midfielders = emptyList(),
                attackers = emptyList(),
            )
        )

        viewModelScope.launch {
            eventRepository.teamChanged()
        }
    }

    fun onChoosePlayer(position: Position, index: Int, playerId: String) {
        val state = startingElevenState.value as? StartingElevenState.Content ?: return

        val lineup = when (position) {
            Position.Goalkeeper -> {
                state.lineup.copy(goalkeeper = playerId)
            }
            Position.Defender ->  {
                val defenders = state.lineup.defenders.insertAt(index = index, element = playerId)
                state.lineup.copy(defenders = defenders)
            }
            Position.Midfielder -> {
                val midfielders = state.lineup.midfielders.insertAt(index = index, element = playerId)
                state.lineup.copy(midfielders = midfielders)
            }
            Position.Attacker -> {
                val attackers = state.lineup.attackers.insertAt(index = index, element = playerId)
                state.lineup.copy(attackers = attackers)
            }
        }

        _startingElevenState.value = state.copy(
            lineup = lineup
        )

        viewModelScope.launch {
            eventRepository.teamChanged()

            if (lineup.isComplete) {
                eventRepository.saveTeam()
            }
        }
    }

    fun saveTeam() {
        val state = startingElevenState.value as? StartingElevenState.Content ?: return

        viewModelScope.launch {
            val currentUser = userRepository.getCurrentUser()
            if (currentUser == null) {
                _startingElevenState.value = StartingElevenState.Error(message = "Please sign in")
                return@launch
            }

            _startingElevenState.value = StartingElevenState.Loading

            lineupRepository.setUserLineup(
                uid = currentUser.uid,
                userLineup = state.lineup,
            )

            userRepository.setFormation(
                uid = currentUser.uid,
                formationId = state.formation.formationId,
            )

            _startingElevenState.value = StartingElevenState.Content(
                season = configRepository.getConfig()?.season,
                possibleFormations = state.possibleFormations,
                formation = state.formation,
                playersWithLineupCount = state.playersWithLineupCount,
                lineup = state.lineup,
                enabled = state.enabled,
            )
        }
    }

    private fun initObservers() {
        viewModelScope.launch {
            eventRepository.onNewEvent.collect {
                if (it == Event.SaveTeam) {
                    saveTeam()
                    eventRepository.teamSaved()
                    _showSnackBar.value = true
                    delay(100)
                    _showSnackBar.value = false
                }
            }
        }
    }
}
