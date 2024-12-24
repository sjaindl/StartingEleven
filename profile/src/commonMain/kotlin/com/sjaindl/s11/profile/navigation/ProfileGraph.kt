package com.sjaindl.s11.profile.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.navigation
import com.sjaindl.s11.core.extensions.secondaryScreenComposable
import com.sjaindl.s11.core.navigation.ProfileNavGraphRoute
import com.sjaindl.s11.profile.ProfileScreen
import kotlinx.serialization.Serializable

@Serializable
data object Profile

fun NavController.navigateToProfile(navOptions: NavOptions? = null) {
    navigate(route = ProfileNavGraphRoute, navOptions = navOptions)
}

fun NavGraphBuilder.profileGraph() {
    navigation<ProfileNavGraphRoute>(
        startDestination = Profile
    ) {
        secondaryScreenComposable<Profile> {
            ProfileScreen()
        }
    }
}
