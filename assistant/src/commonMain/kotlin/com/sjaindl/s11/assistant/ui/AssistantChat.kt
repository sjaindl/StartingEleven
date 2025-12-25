package com.sjaindl.s11.assistant.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.sjaindl.s11.assistant.navigation.navigateToAssistant
import com.sjaindl.s11.core.theme.HvtdpTheme
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import startingeleven.assistant.generated.resources.Res
import startingeleven.assistant.generated.resources.chat

@Composable
fun AssistantChat(navController: NavController) {
    FloatingActionButton(
        onClick = {
            navController.navigateToAssistant()
        }
    ) {
        Icon(imageVector = Icons.Filled.Forum, contentDescription = stringResource(Res.string.chat))
    }
}

@Preview
@Composable
fun AssistantChatPreview() {
    val navController = rememberNavController()

    HvtdpTheme {
        AssistantChat(navController = navController)
    }
}
