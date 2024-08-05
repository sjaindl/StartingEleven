package com.sjaindl.s11.baseui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavController
import com.sjaindl.s11.navigation.Home
import com.sjaindl.s11.navigation.Players
import com.sjaindl.s11.navigation.Standings
import com.sjaindl.s11.navigation.Team
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import kotlinx.coroutines.flow.distinctUntilChanged

@Composable
fun S11NavigationBar(
    navController: NavController,
) {
    var selectedItem by remember { mutableStateOf(0) }
    val items = listOf(Home, Team, Players, Standings)
    val user by Firebase.auth.authStateChanged.distinctUntilChanged().collectAsState(initial = Firebase.auth.currentUser)

    NavigationBar {
        items.forEachIndexed { index, screen ->
            val contentDescription = screen.toString()

            NavigationBarItem(
                selected = selectedItem == index,
                onClick = {
                    selectedItem = index
                    navController.navigate(route = screen)
                },
                icon = {
                    when (screen) {
                        Home -> Icon(Icons.Filled.Home, contentDescription = contentDescription)
                        Team -> Icon(Icons.Filled.Favorite, contentDescription = contentDescription)
                        Players -> Icon(Icons.Filled.Person, contentDescription = contentDescription)
                        Standings -> Icon(Icons.Filled.Calculate, contentDescription = contentDescription)
                        else -> { }
                    }
                },
                enabled = screen == Home || user != null,
                label = {
                    Text(text = screen.toString())
                },
            )
        }
    }
}
