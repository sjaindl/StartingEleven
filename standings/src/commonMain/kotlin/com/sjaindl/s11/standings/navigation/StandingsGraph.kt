package com.sjaindl.s11.standings.navigation

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.navigation
import com.sjaindl.s11.core.extensions.primaryScreenComposable
import com.sjaindl.s11.core.navigation.Route.Standings
import com.sjaindl.s11.core.navigation.StandingsNavGraphRoute
import com.sjaindl.s11.standings.StandingsScreen
import com.sjaindl.s11.standings.StandingsViewModel

private fun NavController.navigateToStandings(navOptions: NavOptions? = null) {
    navigate(route = StandingsNavGraphRoute, navOptions = navOptions)
}

fun NavGraphBuilder.standingsGraph() {
    navigation<StandingsNavGraphRoute>(
        startDestination = Standings
    ) {
        primaryScreenComposable<Standings> {
            val standingsViewModel = viewModel {
                StandingsViewModel()
            }

            val standingsState by standingsViewModel.standingsState.collectAsState()

            StandingsScreen(
                standingsState = standingsState,
                onReload = {
                    standingsViewModel.loadStandings()
                },
            )
        }
    }
}
