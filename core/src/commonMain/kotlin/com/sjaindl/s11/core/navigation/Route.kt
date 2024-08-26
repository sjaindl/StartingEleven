package com.sjaindl.s11.core.navigation

import androidx.navigation.NavBackStackEntry
import androidx.navigation.toRoute
import com.sjaindl.s11.core.navigation.Route.Auth
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
import startingeleven.core.generated.resources.routeAuth
import startingeleven.core.generated.resources.routeFaq
import startingeleven.core.generated.resources.routeMailSignIn
import startingeleven.core.generated.resources.routeMailSignUp
import startingeleven.core.generated.resources.routePrices
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
    data object MailSignInHome: Route {
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
                Prices -> Res.string.routePrices
                Faqs -> Res.string.routeFaq
                Privacy -> Res.string.routePrivacy
                Profile -> Res.string.routeProfile
                Auth -> Res.string.routeAuth
                SignInChooser -> Res.string.routeAuth
                MailSignInHome -> Res.string.routeMailSignIn
                is MailSignUp -> Res.string.routeMailSignUp
                is MailSignIn -> Res.string.routeMailSignIn
            }
        }
    }
}

fun NavBackStackEntry?.toRoute(): Route? = when (this?.destination?.route?.substringAfterLast(".")?.substringBefore("/")) {
    Home.toString() -> this.toRoute<Home>()
    Team.toString() -> this.toRoute<Team>()
    Players.toString() -> this.toRoute<Players>()
    Standings.toString() -> this.toRoute<Standings>()
    Prices.toString() -> this.toRoute<Prices>()
    Faqs.toString() -> this.toRoute<Faqs>()
    Privacy.toString() -> this.toRoute<Privacy>()
    Profile.toString() -> this.toRoute<Profile>()
    Auth.toString() -> this.toRoute<Auth>()
    SignInChooser.toString() -> this.toRoute<SignInChooser>()
    MailSignInHome.toString() -> this.toRoute<MailSignInHome>()
    MailSignUp.toString().substringBeforeLast("$").substringAfter("$") -> this.toRoute<MailSignUp>()
    MailSignIn.toString().substringBeforeLast("$").substringAfter("$") -> this.toRoute<MailSignIn>()

    else -> {
        Napier.e("Unknown route: ${this?.destination?.route}")
        null
    }
}
