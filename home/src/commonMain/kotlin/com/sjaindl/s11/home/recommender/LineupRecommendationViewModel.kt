package com.sjaindl.s11.home.recommender

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sjaindl.s11.core.firestore.player.model.Player
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

sealed class RecommendationState {
    data object Initial: RecommendationState()
    data object Loading: RecommendationState()
    data object NoRecommendation: RecommendationState()

    data class Recommendation(
        val goalkeeper: Player,
        val defenders: List<Player>,
        val midfielders: List<Player>,
        val attackers: List<Player>,
    ): RecommendationState()

    data class Error(val message: String): RecommendationState()
}

class LineupRecommendationViewModel : ViewModel(), KoinComponent {

    private val recommendLineupUseCase: RecommendLineupUseCase by inject()

    private var _recommendationState: MutableStateFlow<RecommendationState> = MutableStateFlow(value = RecommendationState.Initial)
    var recommendationState = _recommendationState.asStateFlow()

    init {
        determineLineupRecommendation()
    }

    fun determineLineupRecommendation()  = viewModelScope.launch {
        _recommendationState.value = RecommendationState.Loading
        _recommendationState.value = recommendLineupUseCase.determineLineupRecommendation()
    }
}
