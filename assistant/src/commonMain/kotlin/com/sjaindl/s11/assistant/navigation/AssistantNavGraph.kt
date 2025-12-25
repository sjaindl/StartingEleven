package com.sjaindl.s11.assistant.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.sjaindl.s11.assistant.config.AssistantConfig
import com.sjaindl.s11.assistant.ui.AssistantAppBar
import com.sjaindl.s11.assistant.ui.ChatScreen
import com.sjaindl.s11.assistant.ui.ChatViewModel
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

const val chatScreenRoute = "ChatBot"

fun NavGraphBuilder.assistantGraph(
    rootNavController: NavController,
) {
    composable(route = chatScreenRoute) {
        val chatViewModel = koinViewModel<ChatViewModel>()
        val uiState by chatViewModel.uiState.collectAsState()
        val config = koinInject<AssistantConfig>()

        Scaffold(
            topBar = {
                AssistantAppBar(
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

fun NavController.navigateToAssistant() {
    navigate(chatScreenRoute)
}
