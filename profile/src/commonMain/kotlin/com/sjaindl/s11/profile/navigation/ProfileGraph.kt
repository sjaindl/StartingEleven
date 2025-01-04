package com.sjaindl.s11.profile.navigation

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.navigation
import com.sjaindl.s11.core.extensions.secondaryScreenComposable
import com.sjaindl.s11.core.navigation.ProfileNavGraphRoute
import com.sjaindl.s11.profile.ProfileScreen
import com.sjaindl.s11.profile.ProfileViewModel
import kotlinx.serialization.Serializable

@Serializable
data object Profile

fun NavController.navigateToProfile(navOptions: NavOptions? = null) {
    navigate(route = ProfileNavGraphRoute, navOptions = navOptions)
}

fun NavGraphBuilder.profileGraph(
    navigateHome: () -> Unit,
) {
    navigation<ProfileNavGraphRoute>(
        startDestination = Profile
    ) {
        secondaryScreenComposable<Profile> {
            val profileViewModel = viewModel {
                ProfileViewModel()
            }

            val userState by profileViewModel.userState.collectAsState()

            ProfileScreen(
                userState = userState,
                changeUserName = profileViewModel::changeUserName,
                uploadProfilePhoto = profileViewModel::uploadProfilePhoto,
                deleteProfilePhoto = profileViewModel::deleteProfilePhoto,
                loadUser = profileViewModel::loadUser,
                onDeleteAccount = {
                    profileViewModel.deleteAccount {
                        navigateHome()
                    }
                },
            )
        }
    }
}
