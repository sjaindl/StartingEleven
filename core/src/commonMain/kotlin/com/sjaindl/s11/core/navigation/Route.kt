package com.sjaindl.s11.core.navigation

import androidx.navigation.NavBackStackEntry
import androidx.navigation.toRoute
import com.sjaindl.s11.core.navigation.Route.Auth
import com.sjaindl.s11.core.navigation.Route.DebugInfo
import com.sjaindl.s11.core.navigation.Route.Faqs
import com.sjaindl.s11.core.navigation.Route.Home
import com.sjaindl.s11.core.navigation.Route.MailSignIn
import com.sjaindl.s11.core.navigation.Route.MailSignInHome
import com.sjaindl.s11.core.navigation.Route.MailSignUp
import com.sjaindl.s11.core.navigation.Route.Players
import com.sjaindl.s11.core.navigation.Route.Prices
import com.sjaindl.s11.core.navigation.Route.Privacy
import com.sjaindl.s11.core.navigation.Route.Profile
import com.sjaindl.s11.core.navigation.Route.SignInChooser
import com.sjaindl.s11.core.navigation.Route.Standings
import com.sjaindl.s11.core.navigation.Route.Team
import io.github.aakira.napier.Napier
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.StringResource
import startingeleven.core.generated.resources.Res
import startingeleven.core.generated.resources.debugInfo
import startingeleven.core.generated.resources.routeAuth
import startingeleven.core.generated.resources.routeFaq
import startingeleven.core.generated.resources.routeMailSignIn
import startingeleven.core.generated.resources.routeMailSignUp
import startingeleven.core.generated.resources.routePrizes
import startingeleven.core.generated.resources.routePrivacy
import startingeleven.core.generated.resources.routeProfile
import startingeleven.core.generated.resources.tabHome
import startingeleven.core.generated.resources.tabPlayers
import startingeleven.core.generated.resources.tabStandings
import startingeleven.core.generated.resources.tabTeam

@Serializable
data object StandingsNavGraphRoute

@Serializable
data object AuthNavGraphRoute

@Serializable
data object ProfileNavGraphRoute

@Serializable
data object TeamNavGraphRoute

sealed interface Route {
    val showBackButton: Boolean
    val isTopLevelRoute: Boolean

    @Serializable
    data object Home: Route {
        override val showBackButton = false
        override val isTopLevelRoute = true
    }

    @Serializable
    data object Team: Route {
        override val showBackButton = true
        override val isTopLevelRoute = true
    }

    @Serializable
    data object Players: Route {
        override val showBackButton = true
        override val isTopLevelRoute = true
    }

    @Serializable
    data object Standings: Route {
        override val showBackButton = true
        override val isTopLevelRoute = true
    }

    @Serializable
    data object Prices: Route {
        override val showBackButton = true
        override val isTopLevelRoute = false
    }

    @Serializable
    data object Faqs: Route {
        override val showBackButton = true
        override val isTopLevelRoute = false
    }

    @Serializable
    data object Privacy: Route {
        override val showBackButton = true
        override val isTopLevelRoute = false
    }

    @Serializable
    data object DebugInfo: Route {
        override val showBackButton = true
        override val isTopLevelRoute = false
    }

    @Serializable
    data object Profile: Route {
        override val showBackButton = true
        override val isTopLevelRoute = false
    }

    @Serializable
    data object Auth: Route {
        override val showBackButton = false
        override val isTopLevelRoute = false
    }

    @Serializable
    data object SignInChooser: Route {
        override val showBackButton = false
        override val isTopLevelRoute = false
    }

    @Serializable
    data class MailSignInHome(val isSignUp: Boolean): Route {
        override val showBackButton = true
        override val isTopLevelRoute = false
    }

    @Serializable
    data class MailSignUp(val email: String): Route {
        override val showBackButton = true
        override val isTopLevelRoute = false
    }

    @Serializable
    data class MailSignIn(val email: String): Route {
        override val showBackButton = true
        override val isTopLevelRoute = false
    }

    companion object {
        fun stringResForRoute(route: Route): StringResource {
            return when (route) {
                Home -> Res.string.tabHome
                Team -> Res.string.tabTeam
                Players -> Res.string.tabPlayers
                Standings -> Res.string.tabStandings
                Prices -> Res.string.routePrizes
                Faqs -> Res.string.routeFaq
                Privacy -> Res.string.routePrivacy
                DebugInfo -> Res.string.debugInfo
                Profile -> Res.string.routeProfile
                Auth -> Res.string.routeAuth
                SignInChooser -> Res.string.routeAuth
                is MailSignInHome -> {
                    if (route.isSignUp) {
                        Res.string.routeMailSignUp
                    } else {
                        Res.string.routeMailSignIn
                    }
                }
                is MailSignUp -> Res.string.routeMailSignUp
                is MailSignIn -> Res.string.routeMailSignIn
            }
        }
    }
}

fun NavBackStackEntry?.toRoute(): Route? = when (this?.destination?.route?.substringAfterLast(".")?.substringBefore("/")) {
    Home.toString() -> toRoute<Home>()
    Team.toString() -> toRoute<Team>()
    Players.toString() -> toRoute<Players>()
    Standings.toString() -> toRoute<Standings>()
    Prices.toString() -> toRoute<Prices>()
    Faqs.toString() -> toRoute<Faqs>()
    DebugInfo.toString() -> toRoute<DebugInfo>()
    Privacy.toString() -> toRoute<Privacy>()
    Profile.toString() -> toRoute<Profile>()
    Auth.toString() -> toRoute<Auth>()
    SignInChooser.toString() -> toRoute<SignInChooser>()
    MailSignInHome.toString() -> toRoute<MailSignInHome>()
    MailSignUp.toString().substringBeforeLast("$").substringAfter("$") -> this.toRoute<MailSignUp>()
    MailSignIn.toString().substringBeforeLast("$").substringAfter("$") -> this.toRoute<MailSignIn>()

    else -> {
        Napier.e("Unknown route: ${this?.destination?.route}")
        null
    }
}
