package com.sjaindl.s11.team.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.sjaindl.s11.core.navigation.TeamNavGraphRoute
import com.sjaindl.s11.team.StartingElevenScreen
import kotlinx.serialization.Serializable

@Serializable
data object Team

fun NavController.navigateToTeam(navOptions: NavOptions? = null) {
    navigate(route = TeamNavGraphRoute, navOptions = navOptions)
}

fun NavGraphBuilder.teamGraph() {
    navigation<TeamNavGraphRoute>(
        startDestination = Team
    ) {
        composable<Team> {
            StartingElevenScreen()
        }
    }
}
