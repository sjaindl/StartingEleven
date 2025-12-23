package com.sjaindl.s11.ai.navigation

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.sjaindl.s11.ai.ui.ChatScreen
import com.sjaindl.s11.ai.ui.ChatViewModel
import org.koin.compose.viewmodel.koinViewModel

const val chatScreenRoute = "ChatBot"

fun NavGraphBuilder.aiGraph() {
    composable(route = chatScreenRoute) {
        val chatViewModel = koinViewModel<ChatViewModel>()
        val uiState by chatViewModel.uiState.collectAsState()

        ChatScreen(
            uiState = uiState,
            onSendPrompt = chatViewModel::sendPrompt,
        )
    }
}

fun NavController.navigateToAi() {
    navigate(chatScreenRoute)
}
