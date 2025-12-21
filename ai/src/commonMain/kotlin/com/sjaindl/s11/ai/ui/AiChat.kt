package com.sjaindl.s11.ai.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
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
            },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                ChatScreen()
            }
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
