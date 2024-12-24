package com.sjaindl.s11.auth.navigation

import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.navigation
import com.sjaindl.s11.auth.SignInChooserScreen
import com.sjaindl.s11.auth.SignInWithMailHomeScreen
import com.sjaindl.s11.auth.SignInWithMailScreen
import com.sjaindl.s11.auth.SignUpWithMailScreen
import com.sjaindl.s11.core.extensions.primaryScreenComposable
import com.sjaindl.s11.core.extensions.secondaryScreenComposable
import com.sjaindl.s11.core.navigation.AuthNavGraphRoute
import com.sjaindl.s11.core.navigation.Route.MailSignIn
import com.sjaindl.s11.core.navigation.Route.MailSignInHome
import com.sjaindl.s11.core.navigation.Route.MailSignUp
import com.sjaindl.s11.core.navigation.Route.SignInChooser

private const val MAIL_ARG = "email"

private fun NavController.navigateToMailSignInHome(navOptions: NavOptions? = null) {
    navigate(route = MailSignInHome, navOptions = navOptions)
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
            SignInChooserScreen(
                onSignInWithMail = navController::navigateToMailSignInHome,
                onRetry = {
                    navController.navigate(route = SignInChooser)
                },
                onSuccess = {
                    navController.popBackStack(route = SignInChooser, inclusive = true)
                    onSuccess()
                },
                modifier = modifier,
            )
        }
    }

    secondaryScreenComposable<MailSignInHome> {
        SignInWithMailHomeScreen(
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
