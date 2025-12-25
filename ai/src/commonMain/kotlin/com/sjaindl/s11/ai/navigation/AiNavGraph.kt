package com.sjaindl.s11.ai.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.sjaindl.s11.ai.config.AssistantConfig
import com.sjaindl.s11.ai.ui.AIAppBar
import com.sjaindl.s11.ai.ui.ChatScreen
import com.sjaindl.s11.ai.ui.ChatViewModel
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

const val chatScreenRoute = "ChatBot"

fun NavGraphBuilder.aiGraph(
    rootNavController: NavController,
) {
    composable(route = chatScreenRoute) {
        val chatViewModel = koinViewModel<ChatViewModel>()
        val uiState by chatViewModel.uiState.collectAsState()
        val config = koinInject<AssistantConfig>()

        Scaffold(
            topBar = {
                AIAppBar(
                    title = config.appBarTitle,
                    onResetChat = chatViewModel::resetChat,
                    navigateUp = {
                        rootNavController.popBackStack()
                    },
                )
            }
        ) {
            ChatScreen(
                modifier = Modifier
                    .padding(paddingValues = it),
                uiState = uiState,
                onSendPrompt = chatViewModel::sendPrompt,
            )
        }
    }
}

fun NavController.navigateToAi() {
    navigate(chatScreenRoute)
}
