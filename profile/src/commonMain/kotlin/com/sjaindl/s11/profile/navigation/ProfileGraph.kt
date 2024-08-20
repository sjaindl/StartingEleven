package com.sjaindl.s11.profile.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.sjaindl.s11.profile.ProfileScreen
import kotlinx.serialization.Serializable

@Serializable
data object Profile

private fun NavController.navigateToProfile(navOptions: NavOptions? = null) {
    navigate(route = Profile, navOptions = navOptions)
}


@Composable
fun ProfileGraph(
    //navController: NavHostController,
) {
    // Needs to be a separate navController than the top one, because Each NavHost is designed to manage its own navigation graph and associated ViewModelStore
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Profile) {
        composable<Profile> {
            ProfileScreen(
                navigateUp = navController::navigateUp,
            )
        }
    }
}
