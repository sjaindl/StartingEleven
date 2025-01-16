package com.sjaindl.s11.auth.navigation

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.navigation
import com.sjaindl.s11.auth.SignInChooserScreen
import com.sjaindl.s11.auth.SignInWithMailHomeScreen
import com.sjaindl.s11.auth.SignInWithMailScreen
import com.sjaindl.s11.auth.SignUpWithMailScreen
import com.sjaindl.s11.auth.SocialAuthenticationViewModel
import com.sjaindl.s11.core.extensions.primaryScreenComposable
import com.sjaindl.s11.core.extensions.secondaryScreenComposable
import com.sjaindl.s11.core.navigation.AuthNavGraphRoute
import com.sjaindl.s11.core.navigation.Route.MailSignIn
import com.sjaindl.s11.core.navigation.Route.MailSignInHome
import com.sjaindl.s11.core.navigation.Route.MailSignUp
import com.sjaindl.s11.core.navigation.Route.SignInChooser

private const val MAIL_ARG = "email"
private const val IS_SIGNUP_ARG = "isSignUp"

private fun NavController.navigateToMailSignInHome(isSignUp: Boolean, navOptions: NavOptions? = null) {
    navigate(route = MailSignInHome(isSignUp = isSignUp), navOptions = navOptions)
}

private fun NavController.navigateToMailSignIn(email: String, navOptions: NavOptions? = null) {
    navigate(route = MailSignIn(email = email), navOptions = navOptions)
}

private fun NavController.navigateToMailSignUp(email: String, navOptions: NavOptions? = null) {
    navigate(route = MailSignUp(email = email), navOptions = navOptions)
}

fun NavGraphBuilder.authenticationGraph(
    navController: NavHostController,
    onSuccess: () -> Unit,
    modifier: Modifier = Modifier,
) {

    navigation<AuthNavGraphRoute>(
        startDestination = SignInChooser
    ) {
        primaryScreenComposable<SignInChooser> { navBackStackEntry ->
            val authenticationViewModel = viewModel {
                SocialAuthenticationViewModel()
            }

            val authenticationState by authenticationViewModel.authenticationState.collectAsState()

            SignInChooserScreen(
                authenticationState = authenticationState,
                onSignInWithMail = {
                    navController.navigateToMailSignInHome(isSignUp = false)
                },
                onSignUpWithMail = {
                    navController.navigateToMailSignInHome(isSignUp = true)
                },
                onRetry = {
                    navController.navigate(route = SignInChooser)
                },
                onSuccess = {
                    navController.popBackStack(route = SignInChooser, inclusive = true)
                    onSuccess()
                },
                handleGoogleSignIn = authenticationViewModel::handleGoogleSignIn,
                handleFacebookSignIn = authenticationViewModel::handleFacebookSignIn,
                handleAppleSignIn = { appleAuthResponse, cancelMessage ->
                    authenticationViewModel.handleAppleSignIn(
                        appleAuthResponse = appleAuthResponse,
                        cancelMessage = cancelMessage,
                    )
                },
                resetState = authenticationViewModel::resetState,
                modifier = modifier,
            )
        }
    }

    secondaryScreenComposable<MailSignInHome> { navBackStackEntry ->
        val isSignUp = navBackStackEntry.arguments?.getBoolean(IS_SIGNUP_ARG) ?: true

        SignInWithMailHomeScreen(
            isSignUp = isSignUp,
            signIn = { email ->
                navController.navigateToMailSignIn(email = email)
            },
            signUp = { email ->
                navController.navigateToMailSignUp(email = email)
            },
            modifier = modifier,
        )
    }

    secondaryScreenComposable<MailSignIn> { navBackStackEntry ->
        val email = navBackStackEntry.arguments?.getString(MAIL_ARG).orEmpty()

        SignInWithMailScreen(
            email = email,
            modifier = modifier,
        ) {
            navController.popBackStack(route = SignInChooser, inclusive = true)
            onSuccess()
        }
    }

    secondaryScreenComposable<MailSignUp> { navBackStackEntry ->
        val email = navBackStackEntry.arguments?.getString(MAIL_ARG).orEmpty()

        SignUpWithMailScreen(
            email = email,
            modifier = modifier,
        ) {
            navController.popBackStack(route = SignInChooser, inclusive = true)
            onSuccess()
        }
    }
}
