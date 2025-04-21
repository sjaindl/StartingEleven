package com.sjaindl.s11.team.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.navigation
import com.sjaindl.s11.core.extensions.primaryScreenComposable
import com.sjaindl.s11.core.navigation.TeamNavGraphRoute
import com.sjaindl.s11.team.StartingElevenScreen
import com.sjaindl.s11.team.StartingElevenViewModel
import com.sjaindl.s11.team.bet.BetViewModel
import com.sjaindl.s11.team.recommender.LineupRecommendationViewModel
import kotlinx.serialization.Serializable
import startingeleven.team.generated.resources.Res

@Serializable
data object Team

fun NavController.navigateToTeam(navOptions: NavOptions? = null) {
    navigate(route = TeamNavGraphRoute, navOptions = navOptions)
}

fun NavGraphBuilder.teamGraph(
    onShowSnackBar: (String) -> Unit,
) {
    navigation<TeamNavGraphRoute>(
        startDestination = Team
    ) {
        primaryScreenComposable<Team> {
            val viewModel = viewModel {
                StartingElevenViewModel()
            }

            val betViewModel = viewModel {
                BetViewModel()
            }

            val lineupRecommendationViewModel = viewModel {
                LineupRecommendationViewModel()
            }

            val startingElevenState by viewModel.startingElevenState.collectAsStateWithLifecycle()
            val showSnackBar by viewModel.showSnackBar.collectAsStateWithLifecycle()
            val recommendationState by lineupRecommendationViewModel.recommendationState.collectAsStateWithLifecycle()

            val betState by betViewModel.betState.collectAsStateWithLifecycle()
            val userBetState by betViewModel.userBet.collectAsStateWithLifecycle()
            val savedBet by betViewModel.savedBet.collectAsStateWithLifecycle()

            LaunchedEffect(key1 = showSnackBar) {
                if (showSnackBar) {
                    onShowSnackBar(Res.string.lineupSaved)
                }
            }

            StartingElevenScreen(
                startingElevenState = startingElevenState,
                recommendationState = recommendationState,
                betState = betState,
                userBetState = userBetState,
                savedBet = savedBet,
                loadRecommendations = lineupRecommendationViewModel::determineLineupRecommendation,
                onFormationSelected = viewModel::onFormationSelected,
                onChoosePlayer = viewModel::onChoosePlayer,
                loadTeam = viewModel::loadTeam,
                resetSavedBetState = betViewModel::resetSavedBetState,
                setHomeBet = betViewModel::setHomeBet,
                setAwayBet = betViewModel::setAwayBet,
                submitBet = betViewModel::submitBet,
                loadBets = betViewModel::loadBets,
                onShowSnackBar = onShowSnackBar,
            )
        }
    }
}
