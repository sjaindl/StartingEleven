package com.sjaindl.s11.core.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.navigation.NavBackStackEntry

fun fadeInFromTop(): AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition {
    return {
        slideInVertically(
            initialOffsetY = { -it },
            animationSpec = tween(durationMillis = 300)
        ) + fadeIn(
            animationSpec = tween(durationMillis = 300)
        )
    }
}

fun fadeOutToTop(): AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition {
    return {
        slideOutVertically(
            targetOffsetY = { -it },
            animationSpec = tween(durationMillis = 300)
        ) + fadeOut(
            animationSpec = tween(durationMillis = 300)
        )
    }
}

fun scaleIntoContainer(
    direction: ScaleTransitionDirection = ScaleTransitionDirection.INWARDS,
    initialScale: Float = if (direction == ScaleTransitionDirection.OUTWARDS) 0.5f else 1.5f
): AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition {
    return {
        scaleIn(
            animationSpec = tween(durationMillis = 500, delayMillis = 90),
            initialScale = initialScale
        ) + fadeIn(animationSpec = tween(durationMillis = 500, delayMillis = 90))
    }
}

fun scaleOutOfContainer(
    direction: ScaleTransitionDirection = ScaleTransitionDirection.OUTWARDS,
    targetScale: Float = if (direction == ScaleTransitionDirection.INWARDS) 0.5f else 1.5f
): AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition {
    return {
        scaleOut(
            animationSpec = tween(
                durationMillis = 500,
                delayMillis = 90
            ), targetScale = targetScale
        ) + fadeOut(animationSpec = tween(delayMillis = 290))
    }
}

fun slideOutOfContainer(
    towards: SlideDirection,
): AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition {
    return {
        slideOutOfContainer(
            towards = towards,
            animationSpec = tween(durationMillis = 500),
        )
    }
}

fun slideIntoContainer(
    towards: SlideDirection,
): AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition {
    return {
        slideIntoContainer(
            towards = towards,
            animationSpec = tween(durationMillis = 500)
        )
    }
}
