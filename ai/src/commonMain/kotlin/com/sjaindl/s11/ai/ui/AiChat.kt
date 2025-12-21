package com.sjaindl.s11.ai.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.window.Dialog
import com.sjaindl.s11.core.theme.HvtdpTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun AiChat() {
    var showChat by remember {
        mutableStateOf(false)
    }

    FloatingActionButton(
        onClick = {
            showChat = true
        }
    ) {
        Icon(Icons.Filled.Forum, contentDescription = "Chat")
    }

    if (showChat) {
        Dialog(
            onDismissRequest = {
                showChat = false
            }
        ) {
            ChatScreen()
        }
    }
}

@Preview
@Composable
fun AiChatPreview() {
    HvtdpTheme {
        AiChat()
    }
}
