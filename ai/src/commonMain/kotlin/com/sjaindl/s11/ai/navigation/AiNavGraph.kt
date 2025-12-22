package com.sjaindl.s11.ai.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.sjaindl.s11.ai.ui.ChatScreen

const val chatScreenRoute = "ChatBot"

fun NavGraphBuilder.aiGraph() {
    composable(route = chatScreenRoute) {
        ChatScreen()
    }
}

fun NavController.navigateToAi() {
    navigate(chatScreenRoute)
}
