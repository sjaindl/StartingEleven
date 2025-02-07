package com.sjaindl.s11.core.baseui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import com.sjaindl.s11.core.navigation.Route.Home
import com.sjaindl.s11.core.navigation.Route.Players
import com.sjaindl.s11.core.navigation.StandingsNavGraphRoute
import com.sjaindl.s11.core.navigation.TeamNavGraphRoute
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import kotlinx.coroutines.flow.distinctUntilChanged
import org.jetbrains.compose.resources.stringResource
import startingeleven.core.generated.resources.Res
import startingeleven.core.generated.resources.tabHome
import startingeleven.core.generated.resources.tabPlayers
import startingeleven.core.generated.resources.tabStandings
import startingeleven.core.generated.resources.tabTeam

@Composable
fun S11BottomBar(
    navController: NavController,
    selectedItem: Int,
    onSetSelectedItem: (Int) -> Unit,
) {
    val items = listOf(Home, TeamNavGraphRoute, Players, StandingsNavGraphRoute)
    val user by Firebase.auth.authStateChanged.distinctUntilChanged().collectAsState(initial = Firebase.auth.currentUser)

    NavigationBar {
        items.forEachIndexed { index, screen ->
            val contentDescription = screen.toString()

            NavigationBarItem(
                selected = selectedItem == index,
                onClick = {
                    onSetSelectedItem(index)
                    navController.navigate(route = screen)
                },
                icon = {
                    when (screen) {
                        Home -> Icon(Icons.Filled.Home, contentDescription = contentDescription)
                        TeamNavGraphRoute -> Icon(Icons.Filled.SportsSoccer, contentDescription = contentDescription)
                        Players -> Icon(Icons.Filled.Person, contentDescription = contentDescription)
                        StandingsNavGraphRoute -> Icon(Icons.Filled.Calculate, contentDescription = contentDescription)
                        else -> { }
                    }
                },
                enabled = screen == Home || user != null,
                label = {
                    when (screen) {
                        Home -> Text(text = stringResource(Res.string.tabHome), textAlign = TextAlign.Center)
                        TeamNavGraphRoute -> Text(text = stringResource(Res.string.tabTeam), textAlign = TextAlign.Center)
                        Players -> Text(text = stringResource(Res.string.tabPlayers), textAlign = TextAlign.Center)
                        StandingsNavGraphRoute -> Text(text = stringResource(Res.string.tabStandings), textAlign = TextAlign.Center)
                        else -> { }
                    }
                },
            )
        }
    }
}
