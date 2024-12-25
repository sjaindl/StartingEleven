package com.sjaindl.s11

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
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
import com.sjaindl.s11.core.Event
import com.sjaindl.s11.core.EventRepository
import com.sjaindl.s11.core.baseui.S11AppBar
import com.sjaindl.s11.core.baseui.S11BottomBar
import com.sjaindl.s11.core.navigation.Route
import com.sjaindl.s11.core.navigation.Route.Faqs
import com.sjaindl.s11.core.navigation.Route.Home
import com.sjaindl.s11.core.navigation.Route.Players
import com.sjaindl.s11.core.navigation.Route.Standings
import com.sjaindl.s11.core.navigation.Route.Team
import com.sjaindl.s11.core.navigation.toRoute
import com.sjaindl.s11.core.theme.HvtdpTheme
import com.sjaindl.s11.navigation.S11NavHost
import com.sjaindl.s11.profile.navigation.navigateToProfile
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import okio.FileSystem
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinContext
import org.koin.compose.koinInject

@OptIn(ExperimentalCoilApi::class)
@Composable
@Preview
fun App() {
    setSingletonImageLoaderFactory { context ->
        getAsyncImageLoader(context)
    }

    KoinContext {
        val viewModel = viewModel {
            AppViewModel()
        }

        val displayName by viewModel.userName.collectAsState()

        val navController = rememberNavController()

        val snackBarHostState = remember {
            SnackbarHostState()
        }

        val coroutineScope = rememberCoroutineScope()

        var showBottomBar by remember {
            mutableStateOf(value = true)
        }

        var currentRoute: Route? by remember(navController.currentBackStackEntry) {
            mutableStateOf(value = null)
        }

        var selectedItem by remember {
            mutableStateOf(value = 0)
        }

        var saveTeamEnabled by rememberSaveable {
            mutableStateOf(value = false)
        }

        val user by Firebase.auth.authStateChanged.distinctUntilChanged().collectAsState(
            initial = Firebase.auth.currentUser
        )

        val eventRepository = koinInject<EventRepository>()

        coroutineScope.launch {
            eventRepository.onNewEvent.collect { event ->
                if (event == Event.TeamChanged) {
                    saveTeamEnabled = true
                } else if (event == Event.TeamSaved) {
                    saveTeamEnabled = false
                }
            }
        }

        navController.addOnDestinationChangedListener { controller, _, _ ->
            currentRoute = controller.currentBackStackEntry.toRoute()
            selectedItem = when (currentRoute) {
                Home -> 0
                Team -> 1
                Players -> 2
                Standings -> 3
                else -> selectedItem
            }
            showBottomBar = currentRoute?.isTopLevelRoute == true
        }

        val canNavigateBack by remember(navController.previousBackStackEntry, currentRoute) {
            mutableStateOf(
                value = navController.previousBackStackEntry != null &&
                        currentRoute?.showBackButton == true
            )
        }

        HvtdpTheme {
            Scaffold(
                topBar = {
                    Column {
                        //Text("canNavigateBack: $canNavigateBack")
                        //Text("previousBackStackEntry: ${navController.previousBackStackEntry}")
                        //Text("currentRoute: $currentRoute")
                        S11AppBar(
                            userIsSignedIn = user != null,
                            currentRoute = currentRoute,
                            canNavigateBack = canNavigateBack,
                            saveTeamEnabled = saveTeamEnabled,
                            saveTeam = {
                                coroutineScope.launch {
                                    eventRepository.saveTeam()
                                }
                            },
                            navigateUp = navController::navigateUp,
                            navigateHome = {
                                navController.navigate(Home) {
                                    popUpTo<Home>()
                                }
                            },
                            navigateToFaqs = {
                                navController.navigate(route = Faqs)
                            },
                            navigateToPrices = {
                                navController.navigate(route = Route.Prices)
                            },
                            navigateToPrivacyPolicy = {
                                navController.navigate(route = Route.Privacy)
                            },
                            navigateToDebugInfo = {
                                navController.navigate(route = Route.DebugInfo)
                            },
                            onClickProfile = {
                                navController.navigateToProfile()
                            }
                        )
                    }
                },
                bottomBar = {
                    if (showBottomBar) {
                        S11BottomBar(
                            navController = navController,
                            selectedItem = selectedItem,
                            onSetSelectedItem = { index ->
                                selectedItem = index
                            }
                        )
                    }
                },
                snackbarHost = {
                    SnackbarHost(hostState = snackBarHostState)
                },
            ) {
                S11NavHost(
                    navController = navController,
                    userName = displayName,
                    onShowSnackBar = {
                        coroutineScope.launch {
                            snackBarHostState.showSnackbar(message = it)
                        }
                    },
                    modifier = Modifier
                        .padding(paddingValues = it),
                )
            }
        }
    }
}

fun getAsyncImageLoader(context: PlatformContext) =
    ImageLoader
        .Builder(context)
        .crossfade(true)
        .logger(logger = DebugLogger())
        .memoryCachePolicy(policy = CachePolicy.ENABLED)
        .memoryCache {
            MemoryCache.Builder()
                .maxSizePercent(context = context, percent = 0.3)
                .strongReferencesEnabled(enable = true)
                .build()
        }
        .diskCache(diskCache = newDiskCache())
        .build()

fun newDiskCache(): DiskCache {
    return DiskCache.Builder()
        .directory(FileSystem.SYSTEM_TEMPORARY_DIRECTORY / "image_cache")
        .maxSizeBytes(size = 50L * 1024 * 1024) // 50 MB
        .build()
}
