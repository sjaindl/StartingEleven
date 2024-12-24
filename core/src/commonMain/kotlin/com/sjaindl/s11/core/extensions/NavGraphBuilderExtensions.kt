package com.sjaindl.s11.core.extensions

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection.Companion.Left
import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection.Companion.Right
import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import com.sjaindl.s11.core.navigation.ScaleTransitionDirection
import com.sjaindl.s11.core.navigation.fadeInFromTop
import com.sjaindl.s11.core.navigation.fadeOutToTop
import com.sjaindl.s11.core.navigation.scaleIntoContainer
import com.sjaindl.s11.core.navigation.scaleOutOfContainer
import com.sjaindl.s11.core.navigation.slideIntoContainer
import com.sjaindl.s11.core.navigation.slideOutOfContainer
import kotlin.reflect.KType

inline fun <reified T : Any> NavGraphBuilder.primaryScreenComposable(
    typeMap: Map<KType, NavType<*>> = emptyMap(),
    deepLinks: List<NavDeepLink> = emptyList(),
    noinline content: @Composable AnimatedContentScope.(NavBackStackEntry) -> Unit
) {
    composable<T>(
        typeMap = typeMap,
        deepLinks = deepLinks,
        enterTransition = slideIntoContainer(towards = Left),
        exitTransition = slideOutOfContainer(towards = Left),
        popEnterTransition = slideIntoContainer(towards = Right),
        popExitTransition = slideOutOfContainer(towards = Right),
        content = content,
    )
}

inline fun <reified T : Any> NavGraphBuilder.secondaryScreenComposable(
    typeMap: Map<KType, NavType<*>> = emptyMap(),
    deepLinks: List<NavDeepLink> = emptyList(),
    noinline content: @Composable AnimatedContentScope.(NavBackStackEntry) -> Unit
) {
    composable<T>(
        typeMap = typeMap,
        deepLinks = deepLinks,
        enterTransition = scaleIntoContainer(),
        exitTransition = scaleOutOfContainer(direction = ScaleTransitionDirection.INWARDS),
        popEnterTransition = fadeInFromTop(),
        popExitTransition = fadeOutToTop(),
        content = content,
    )
}
