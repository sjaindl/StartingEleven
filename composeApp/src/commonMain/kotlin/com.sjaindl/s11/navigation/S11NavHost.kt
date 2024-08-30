package com.sjaindl.s11.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.sjaindl.s11.debuginfo.DebugInfoScreen
import com.sjaindl.s11.auth.navigation.authenticationGraph
import com.sjaindl.s11.composables.PlayersScreen
import com.sjaindl.s11.core.navigation.AuthNavGraphRoute
import com.sjaindl.s11.core.navigation.Route
import com.sjaindl.s11.core.navigation.Route.Faqs
import com.sjaindl.s11.core.navigation.Route.Home
import com.sjaindl.s11.core.navigation.Route.Players
import com.sjaindl.s11.faq.FaqViewModel
import com.sjaindl.s11.faq.Faqs
import com.sjaindl.s11.home.HomeScreen
import com.sjaindl.s11.prices.PricesScreen
import com.sjaindl.s11.privacypolicy.PrivacyPolicyScreen
import com.sjaindl.s11.profile.navigation.profileGraph
import com.sjaindl.s11.standings.navigation.standingsGraph
import com.sjaindl.s11.team.navigation.teamGraph
import org.jetbrains.compose.resources.stringResource
import startingeleven.composeapp.generated.resources.Res
import startingeleven.composeapp.generated.resources.signInSuccess

@Composable
fun S11NavHost(
    navController: NavHostController,
    userName: String?,
    onShowSnackBar: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val signInSuccessText = stringResource(resource = Res.string.signInSuccess)

    NavHost(
        navController = navController,
        startDestination = Home,
        modifier = modifier,
    ) {

        composable<Home> {
            HomeScreen(
                displayName = userName,
                onAuthenticate = {
                    navController.navigate(route = AuthNavGraphRoute)
                },
                onShowSnackBar = onShowSnackBar,
            )
        }

        teamGraph()

        composable<Route.Prices>(
            enterTransition = fadeInFromTop(),
            exitTransition = fadeOutToTop(),
        ) {
            PricesScreen()
        }

        composable<Players> {
            PlayersScreen()
        }

        standingsGraph()

        authenticationGraph(
            navController = navController,
            onSuccess = {
                navController.navigate(Home) {
                    popUpTo<Home>()
                }
                onShowSnackBar(signInSuccessText)
            }
        )

        profileGraph()

        composable<Faqs> {
            val faqViewModel = viewModel {
                FaqViewModel()
            }

            val faqState by faqViewModel.faqState.collectAsState()

            Faqs(
                faqState = faqState,
                onRetry = {
                    faqViewModel.loadFaq()
                },
            )
        }

        composable<Route.Privacy> {
            PrivacyPolicyScreen()
        }

        composable<Route.DebugInfo> {
            DebugInfoScreen()
        }
    }
}
