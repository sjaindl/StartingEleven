package com.sjaindl.s11.standings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sjaindl.s11.standings.domain.CalculatePointsUseCase
import com.sjaindl.s11.standings.model.UserWithPoints
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

sealed class StandingsState {
    data object Initial: StandingsState()
    data object Loading: StandingsState()
    data class Calculated(val usersWithPoints: List<UserWithPoints>): StandingsState()
    data class Error(val message: String): StandingsState()
}

class StandingsViewModel : ViewModel(), KoinComponent {

    private val tag = "StandingsViewModel"

    private val calculatePointsUseCase: CalculatePointsUseCase by inject()

    private var _standingsState: MutableStateFlow<StandingsState> = MutableStateFlow(
        StandingsState.Initial
    )
    var standingsState = _standingsState.asStateFlow()

    init {
        loadStandings()
    }

    fun loadStandings() = viewModelScope.launch {
        _standingsState.value = StandingsState.Loading

        try {
            val userWithPoints = calculatePointsUseCase.calculate()
            _standingsState.value = StandingsState.Calculated(usersWithPoints = userWithPoints)
        } catch (exception: Exception) {
            val message = exception.message ?: exception.toString()
            Napier.e(message = message, throwable = exception, tag = tag)
            _standingsState.value = StandingsState.Error(message = message)
        }
    }
}
