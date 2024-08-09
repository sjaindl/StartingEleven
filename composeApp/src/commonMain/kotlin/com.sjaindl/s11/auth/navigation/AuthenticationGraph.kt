package com.sjaindl.s11.auth.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.sjaindl.s11.auth.AuthenticationState.EmailSignInSuccess
import com.sjaindl.s11.auth.AuthenticationState.EmailSignUpSuccess
import com.sjaindl.s11.auth.AuthenticationState.Error
import com.sjaindl.s11.auth.AuthenticationState.FacebookSignInSuccess
import com.sjaindl.s11.auth.AuthenticationState.GoogleSignInSuccess
import com.sjaindl.s11.auth.AuthenticationState.Initial
import com.sjaindl.s11.auth.AuthenticationState.Loading
import com.sjaindl.s11.auth.AuthenticationViewModel
import com.sjaindl.s11.auth.GoogleSignIn
import com.sjaindl.s11.auth.SignInChooserScreen
import com.sjaindl.s11.auth.SignInWithMailHomeScreen
import com.sjaindl.s11.auth.SignInWithMailScreen
import com.sjaindl.s11.auth.SignUpWithMailScreen
import com.sjaindl.s11.auth.FacebookSignIn
import com.sjaindl.s11.baseui.ErrorScreen
import com.sjaindl.s11.baseui.LoadingScreen
import com.sjaindl.s11.baseui.S11AppBar
import com.sjaindl.s11.core.BackHandler
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.stringResource
import startingeleven.composeapp.generated.resources.Res
import startingeleven.composeapp.generated.resources.appName
import startingeleven.composeapp.generated.resources.signInCancel

private const val MAIL_ARG = "email"

@Serializable
data object SignInChooser

@Serializable
data object MailSignInHome

private fun NavController.navigateToMailSignInHome(navOptions: NavOptions? = null) {
    navigate(route = MailSignInHome, navOptions = navOptions)
}

@Serializable
data class MailSignIn(val email: String)

private fun NavController.navigateToMailSignIn(email: String, navOptions: NavOptions? = null) {
    navigate(route = MailSignIn(email = email), navOptions = navOptions)
}

@Serializable
data class MailSignUp(val email: String)

private fun NavController.navigateToMailSignUp(email: String, navOptions: NavOptions? = null) {
    navigate(route = MailSignUp(email = email), navOptions = navOptions)
}

@Composable
fun authenticationGraph(
    onSuccess: () -> Unit,
) {
    // Needs to be a separate navController than the top one, because Each NavHost is designed to manage its own navigation graph and associated ViewModelStore
    val navController = rememberNavController()

    val cancelMessage = stringResource(resource = Res.string.signInCancel)

    var currentRoute by remember {
        mutableStateOf(value = navController.currentBackStackEntry?.destination?.route)
    }

    val authenticationViewModel = viewModel {
        AuthenticationViewModel()
    }

    val authenticationState by authenticationViewModel.authenticationState.collectAsState()

    var signInWithGoogle by remember {
        mutableStateOf(value = false)
    }

    var signInWithFacebook by remember {
        mutableStateOf(value = false)
    }

    BackHandler(
        isEnabled = true,
        onBack = { },
    )

    navController.addOnDestinationChangedListener { _, destination, _ ->
        currentRoute = destination.route
    }

    if (signInWithGoogle) {
        GoogleSignIn {
            signInWithGoogle = false

            authenticationViewModel.handleGoogleSignIn(
                googleAuthResponse = it,
                cancelMessage = cancelMessage,
            )
        }
    }

    if (signInWithFacebook) {
        signInWithFacebook = false

        FacebookSignIn {
            authenticationViewModel.handleFacebookSignIn(
                facebookAuthResponse = it,
                cancelMessage = cancelMessage,
            )
        }
    }

    Scaffold(
        topBar = {
            S11AppBar(
                title = stringResource(resource = Res.string.appName),
                userIsSignedIn = false,
                canNavigateBack = currentRoute?.contains(other = SignInChooser.toString())?.not() ?: false,
                navigateUp = navController::navigateUp,
            )
        }
    ) { paddingValues ->
        when (val state = authenticationState) {
            Initial -> {
                NavHost(navController = navController, startDestination = SignInChooser) {
                    composable<SignInChooser> {
                        SignInChooserScreen(
                            signInWithGoogle = {
                                signInWithGoogle = true
                            },
                            signInWithFacebook = {
                                signInWithFacebook = true
                            },
                            signInWithMail = {
                                navController.navigateToMailSignInHome()
                            },
                            modifier = Modifier
                                .padding(paddingValues = paddingValues),
                        )
                    }

                    composable<MailSignInHome> {
                        SignInWithMailHomeScreen(
                            signIn = { email ->
                                navController.navigateToMailSignIn(email = email)
                            },
                            signUp = { email ->
                                navController.navigateToMailSignUp(email = email)
                            },
                            modifier = Modifier
                                .padding(paddingValues = paddingValues),
                        )
                    }

                    composable<MailSignIn> { navBackStackEntry ->
                        val email = navBackStackEntry.arguments?.getString(MAIL_ARG).orEmpty()

                        SignInWithMailScreen(
                            email = email,
                            modifier = Modifier
                                .padding(paddingValues = paddingValues),
                        ) { _, password ->
                            authenticationViewModel.signInWithMail(
                                email = email,
                                password = password,
                            )
                        }
                    }

                    composable<MailSignUp> { navBackStackEntry ->
                        val email = navBackStackEntry.arguments?.getString(MAIL_ARG).orEmpty()

                        SignUpWithMailScreen(
                            email = email,
                            modifier = Modifier
                                .padding(paddingValues = paddingValues),
                        ) { _, password, name ->
                            authenticationViewModel.signUpWithMail(
                                email = email,
                                password = password,
                                name = name,
                            )
                        }
                    }
                }
            }

            Loading -> {
                LoadingScreen()
            }

            is Error -> {
                ErrorScreen(
                    text = state.message,
                    onButtonClick = {
                        authenticationViewModel.resetState()
                        navController.navigate(route = SignInChooser)
                    },
                )
            }

            EmailSignInSuccess, EmailSignUpSuccess, FacebookSignInSuccess, GoogleSignInSuccess -> {
                LaunchedEffect(authenticationState) {
                    authenticationViewModel.resetState()
                    navController.popBackStack(route = SignInChooser, inclusive = true)
                    onSuccess()
                }
            }
        }
    }
}
