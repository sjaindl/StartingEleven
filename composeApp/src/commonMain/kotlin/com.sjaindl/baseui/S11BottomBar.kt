package com.sjaindl.baseui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavController
import navigation.Home
import navigation.Players
import navigation.Standings
import navigation.Team

@Composable
fun S11NavigationBar(navController: NavController) {
    var selectedItem by remember { mutableStateOf(0) }
    val items = listOf(Home, Team, Players, Standings)

    NavigationBar {
        items.forEachIndexed { index, screen ->
            NavigationBarItem(
                icon = {
                    when (screen) {
                        Home -> Icon(Icons.Filled.Home, contentDescription = screen.toString())
                        Team -> Icon(Icons.Filled.Favorite, contentDescription = screen.toString())
                        Players -> Icon(Icons.Filled.Person, contentDescription = screen.toString())
                        Standings -> Icon(Icons.Filled.Calculate, contentDescription = screen.toString())
                        else -> {}
                    }
                },
                label = {
                    Text(text = screen.toString())
                },
                selected = selectedItem == index,
                onClick = {
                    selectedItem = index
                    navController.navigate(route = screen.toString())
                }
            )
        }
    }
}
