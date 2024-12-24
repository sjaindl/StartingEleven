package com.sjaindl.s11.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.sjaindl.s11.auth.navigation.authenticationGraph
import com.sjaindl.s11.core.extensions.primaryScreenComposable
import com.sjaindl.s11.core.extensions.secondaryScreenComposable
import com.sjaindl.s11.core.navigation.AuthNavGraphRoute
import com.sjaindl.s11.core.navigation.Route
import com.sjaindl.s11.core.navigation.Route.Faqs
import com.sjaindl.s11.core.navigation.Route.Home
import com.sjaindl.s11.core.navigation.Route.Players
import com.sjaindl.s11.core.navigation.toRoute
import com.sjaindl.s11.debuginfo.DebugInfoScreen
import com.sjaindl.s11.faq.FaqViewModel
import com.sjaindl.s11.faq.Faqs
import com.sjaindl.s11.home.HomeScreen
import com.sjaindl.s11.players.PlayersScreen
import com.sjaindl.s11.prices.PricesScreen
import com.sjaindl.s11.prices.model.PricesData
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

        primaryScreenComposable<Home> {
            HomeScreen(
                displayName = userName,
                onAuthenticated = { authenticated ->
                    val currentRoute = navController.currentBackStackEntry.toRoute()

                    if (authenticated) {
                        if (currentRoute != Home) {
                            navController.navigate(route = Home)
                        }
                    } else {
                        navController.navigate(route = AuthNavGraphRoute)
                    }
                },
                onShowSnackBar = onShowSnackBar,
            )
        }

        teamGraph()

        secondaryScreenComposable<Route.Prices> {
            var pricesData: PricesData? by remember {
                mutableStateOf(value = null)
            }

            LaunchedEffect(Unit) {
                pricesData = PricesData.default2024()
            }

            pricesData?.let {
                PricesScreen(pricesData = it)
            }
        }

        primaryScreenComposable<Players> {
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

        secondaryScreenComposable<Faqs> {
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

        secondaryScreenComposable<Route.Privacy> {
            PrivacyPolicyScreen()
        }

        secondaryScreenComposable<Route.DebugInfo> {
            DebugInfoScreen()
        }
    }
}
