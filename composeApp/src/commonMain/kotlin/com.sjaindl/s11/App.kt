package com.sjaindl.s11

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.annotation.ExperimentalCoilApi
import coil3.compose.setSingletonImageLoaderFactory
import coil3.disk.DiskCache
import coil3.memory.MemoryCache
import coil3.request.CachePolicy
import coil3.request.crossfade
import coil3.util.DebugLogger
import com.sjaindl.s11.auth.navigation.authenticationGraph
import com.sjaindl.s11.core.baseui.S11AppBar
import com.sjaindl.s11.core.baseui.S11NavigationBar
import com.sjaindl.s11.composables.PlayersScreen
import com.sjaindl.s11.home.HomeScreen
import com.sjaindl.s11.core.navigation.Auth
import com.sjaindl.s11.core.navigation.Home
import com.sjaindl.s11.core.navigation.Players
import com.sjaindl.s11.core.navigation.Standings
import com.sjaindl.s11.core.navigation.Team
import com.sjaindl.s11.core.theme.HvtdpTheme
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import okio.FileSystem
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinContext
import startingeleven.composeapp.generated.resources.Res
import startingeleven.composeapp.generated.resources.appName
import startingeleven.composeapp.generated.resources.compose_multiplatform
import startingeleven.composeapp.generated.resources.signInSuccess

private val TOP_LEVEL_SCREENS = listOf(Home.toString(), Team.toString(), Players.toString(), Standings.toString())

@OptIn(ExperimentalCoilApi::class)
@Composable
@Preview
fun App() {
    setSingletonImageLoaderFactory { context ->
        getAsyncImageLoader(context)
    }

    KoinContext {
        val navController = rememberNavController()

        val snackBarHostState = remember {
            SnackbarHostState()
        }

        var showBars by remember {
            mutableStateOf(value = true)
        }

        val coroutineScope = rememberCoroutineScope()

        val user by Firebase.auth.authStateChanged.distinctUntilChanged().collectAsState(
            initial = Firebase.auth.currentUser
        )

        HvtdpTheme {
            Scaffold(
                topBar = {
                    if (showBars) {
                        S11AppBar(
                            title = stringResource(resource = Res.string.appName),
                            userIsSignedIn = user?.displayName != null,
                            canNavigateBack = navController.previousBackStackEntry != null &&
                                    TOP_LEVEL_SCREENS.contains(navController.currentBackStackEntry?.destination?.route).not() &&
                                    navController.currentBackStackEntry?.destination?.route != Auth.toString(),
                            navigateUp = navController::navigateUp,
                        )
                    }
                },
                bottomBar = {
                    if (showBars) {
                        S11NavigationBar(navController)
                    }
                },
                snackbarHost = {
                    SnackbarHost(hostState = snackBarHostState)
                },
            ) {
                NavHost(
                    navController = navController,
                    startDestination = Home,
                    modifier = Modifier
                        .padding(paddingValues = it),
                ) {
                    composable<Home> {
                        HomeScreen(
                            displayName = user?.displayName,
                            onAuthenticate = {
                                showBars = false
                                navController.navigate(route = Auth)
                            },
                        )
                    }
                    composable<Team> {
                        Text("Team")
                        TestContent()
                    }
                    composable<Players> {
                        PlayersScreen()
                    }
                    composable<Standings> {
                        Text("Standings")
                        TestContent()
                    }

                    composable<Auth> {
                        val signInSuccessText = stringResource(Res.string.signInSuccess)

                        authenticationGraph() {
                            navController.popBackStack()
                            showBars = true
                            coroutineScope.launch {
                                snackBarHostState.showSnackbar(message = signInSuccessText)
                            }
                        }
                    }
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

fun getAsyncImageLoader(context: PlatformContext)=
    ImageLoader
        .Builder(context)
        .crossfade(true)
        .logger(DebugLogger())
        .memoryCachePolicy(CachePolicy.ENABLED)
        .memoryCache {
            MemoryCache.Builder()
                .maxSizePercent(context = context, percent = 0.3)
                .strongReferencesEnabled(enable = true)
                .build()
        }
        .diskCache(newDiskCache())
        .build()

fun newDiskCache(): DiskCache {
    return DiskCache.Builder()
        .directory(FileSystem.SYSTEM_TEMPORARY_DIRECTORY / "image_cache")
        .maxSizeBytes(size = 50L * 1024 * 1024) // 50 MB
        .build()
}
