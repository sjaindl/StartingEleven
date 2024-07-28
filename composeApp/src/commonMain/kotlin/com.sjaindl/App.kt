package com.sjaindl

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.sjaindl.baseui.S11AppBar
import com.sjaindl.baseui.S11NavigationBar
import navigation.Home
import navigation.Players
import navigation.Standings
import navigation.Team
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import startingeleven.composeapp.generated.resources.Res
import startingeleven.composeapp.generated.resources.appName
import startingeleven.composeapp.generated.resources.compose_multiplatform

@Composable
@Preview
fun App() {
    val navController = rememberNavController()

    MaterialTheme {
        Scaffold(
            topBar = {
                S11AppBar(
                    title = stringResource(resource = Res.string.appName),
                    canNavigateBack = navController.previousBackStackEntry != null,
                    navigateUp = {
                        navController.navigateUp()
                    },
                )
            },
            bottomBar = {
                S11NavigationBar(navController)
            },
        ) {
            NavHost(navController = navController, startDestination = Home.toString(), modifier = Modifier.padding(it)) {
                composable(route = Home.toString()) {
                    Text("Home")
                }
                composable(route = Team.toString()) {
                    Text("Team")
                    TestContent()
                }
                composable(route = Players.toString()) {
                    Text("Players")
                    TestContent()
                }
                composable(route = Standings.toString()) {
                    Text("Standings")
                    TestContent()
                }
            }
        }
    }
}

@Composable
private fun TestContent() {
    var showContent by remember {
        mutableStateOf(false)
    }

    Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Button(onClick = { showContent = !showContent }) {
            Text("Click me!")
        }

        AnimatedVisibility(showContent) {
            val greeting = remember { Greeting().greet() }
            Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                Image(painterResource(Res.drawable.compose_multiplatform), null)
                Text("Compose: $greeting")
            }
        }
    }
}