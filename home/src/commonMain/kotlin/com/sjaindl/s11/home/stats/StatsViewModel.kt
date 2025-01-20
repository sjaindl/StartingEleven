package com.sjaindl.s11.home.stats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sjaindl.s11.core.firestore.matchday.MatchDayRepository
import com.sjaindl.s11.core.firestore.player.PlayerRepository
import com.sjaindl.s11.core.firestore.user.UserRepository
import com.sjaindl.s11.home.stats.model.PlayerCardItem
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

sealed class StatsState {
    data object Initial: StatsState()
    data object Loading: StatsState()
    data object NoMatches: StatsState()
    data class Content(
        val topElevenLastRound: List<PlayerCardItem>,
        val mvps: List<PlayerCardItem>,
    ): StatsState()
    data class Error(val message: String): StatsState()
}

class StatsViewModel : ViewModel(), KoinComponent {

    companion object {
        const val NUM_OF_DISPLAYED_MVPS = 5
        const val NUM_OF_DISPLAYED_MVPS_LAST_ROUND = 11
    }

    private val tag = "StatsViewModel"

    private val matchDayRepository: MatchDayRepository by inject()
    private val playerRepository: PlayerRepository by inject()
    private val userRepository: UserRepository by inject()

    private var _statsState: MutableStateFlow<StatsState> = MutableStateFlow(value = StatsState.Initial)
    var statsState = _statsState.asStateFlow()

    init {
        loadStatistics()
        setObservers()
    }

    fun loadStatistics() = viewModelScope.launch {
        _statsState.value = StatsState.Loading

        try {
            val currentUser = userRepository.getCurrentUser()
            if (currentUser == null) {
                _statsState.value = StatsState.Error(message = "Please sign in")
                return@launch
            }

            val lastMatchDay = matchDayRepository.getMatchDays().lastOrNull()
            if (lastMatchDay == null) {
                _statsState.value = StatsState.NoMatches
                return@launch
            }

            val players = playerRepository.getPlayers(onlyActive = false)

            val mvps = players.map { player ->
                PlayerCardItem(
                    name = player.name,
                    points = player.points.values.sum(),
                )
            }.sortedByDescending {
                it.points
            }.take(n = NUM_OF_DISPLAYED_MVPS)

            val topElevenLastRound = players.map { player ->
                PlayerCardItem(
                    name = player.name,
                    points = player.points.filter {
                        it.key == lastMatchDay.docId
                    }.map {
                        it.value
                    }.sum()
                )
            }.sortedByDescending {
                it.points
            }.take(n = NUM_OF_DISPLAYED_MVPS_LAST_ROUND)

            _statsState.value = StatsState.Content(
                topElevenLastRound = topElevenLastRound,
                mvps = mvps,
            )
        } catch (exception: Exception) {
            val message = exception.message ?: exception.toString()
            Napier.e(message = message, throwable = exception, tag = tag)
            _statsState.value = StatsState.Error(message = message)
        }
    }

    private fun setObservers() {
        viewModelScope.launch {
            userRepository.getCurrentUserFlow().distinctUntilChanged().collect { user ->
                if (user != null) {
                    loadStatistics()
                }
            }
        }
    }
}
