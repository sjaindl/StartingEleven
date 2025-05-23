package com.sjaindl.s11.home.stats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sjaindl.s11.core.firestore.config.ConfigRepository
import com.sjaindl.s11.core.firestore.matchday.MatchDayRepository
import com.sjaindl.s11.core.firestore.player.PlayerRepository
import com.sjaindl.s11.core.firestore.user.UserRepository
import com.sjaindl.s11.home.stats.model.PlayerCardItem
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.remoteconfig.remoteConfig
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

sealed class StatsState {
    data object Initial: StatsState()
    data object Loading: StatsState()
    data object NoMatches: StatsState()
    data class Content(
        val topPlayersLastRound: List<PlayerCardItem>,
        val mvps: List<PlayerCardItem>,
    ): StatsState()
    data class Error(val message: String): StatsState()
}

@KoinViewModel
class StatsViewModel : ViewModel(), KoinComponent {

    companion object {
        const val NUM_OF_DISPLAYED_MVPS_KEY = "NUM_OF_DISPLAYED_MVPS"
        const val NUM_OF_DISPLAYED_MVPS_LAST_ROUND_KEY = "NUM_OF_DISPLAYED_MVPS_LAST_ROUND"
        const val NUM_OF_DISPLAYED_MVPS = 3
        const val NUM_OF_DISPLAYED_MVPS_LAST_ROUND = 5
    }

    private val tag = "StatsViewModel"

    private val remoteConfig by lazy {
        Firebase.remoteConfig
    }

    private val configRepository: ConfigRepository by inject()
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

            fetchRemoteConfig()

            val season = configRepository.getConfig()?.season
            val players = playerRepository.getPlayers(onlyActive = false)

            val numOfDisplayedMvps = remoteConfig.getValue(NUM_OF_DISPLAYED_MVPS_KEY).asLong()
            val numOfDisplayedMvpsOfRound = remoteConfig.getValue(NUM_OF_DISPLAYED_MVPS_LAST_ROUND_KEY).asLong()

            val mvps = players.map { player ->
                PlayerCardItem(
                    name = player.name,
                    points = player.pointsOfSeason(season = season).values.sum(),
                )
            }.sortedByDescending {
                it.points
            }.take(n = numOfDisplayedMvps.toInt())

            val topPlayersLastRound = players.map { player ->
                PlayerCardItem(
                    name = player.name,
                    points = player.pointsOfSeason(season = season).filter {
                        it.key == lastMatchDay.docId
                    }.map {
                        it.value
                    }.sum()
                )
            }.sortedByDescending {
                it.points
            }.take(n = numOfDisplayedMvpsOfRound.toInt())

            _statsState.value = StatsState.Content(
                topPlayersLastRound = topPlayersLastRound,
                mvps = mvps,
            )
        } catch (exception: Exception) {
            val message = exception.message ?: exception.toString()
            Napier.e(message = message, throwable = exception, tag = tag)
            _statsState.value = StatsState.Error(message = message)
        }
    }

    private suspend fun fetchRemoteConfig() {
        with(remoteConfig) {
            settings {
                minimumFetchIntervalInSeconds = 3600
            }

            setDefaults(
                Pair(NUM_OF_DISPLAYED_MVPS_KEY, NUM_OF_DISPLAYED_MVPS),
                Pair(NUM_OF_DISPLAYED_MVPS_LAST_ROUND_KEY, NUM_OF_DISPLAYED_MVPS_LAST_ROUND)
            )

            fetchAndActivate()
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
