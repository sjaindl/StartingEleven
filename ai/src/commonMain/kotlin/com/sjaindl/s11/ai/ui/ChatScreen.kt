package com.sjaindl.s11.ai.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sjaindl.s11.core.baseui.LoadingScreen
import com.sjaindl.s11.core.theme.HvtdpTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ChatScreen() {
    val chatViewModel = viewModel {
        ChatViewModel()
    }

    val uiState by chatViewModel.uiState.collectAsState()
    var prompt by remember {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier
                .weight(1f)
        ) {
            items(uiState.messages) { message ->
                Text(
                    text = "${if (message.isFromUser) "You" else "AI"}: ${message.text}",
                    modifier = Modifier
                        .padding(8.dp)
                )
            }
        }

        if (uiState.isLoading) {
            LoadingScreen()
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            TextField(
                value = prompt,
                onValueChange = {
                    prompt = it
                },
                modifier = Modifier
                    .weight(1f)
            )

            Button(
                onClick = {
                    chatViewModel.sendPrompt(prompt)
                    prompt = ""
                },
                enabled = prompt.isNotBlank() && !uiState.isLoading
            ) {
                Text("Send")
            }
        }
    }
}

@Preview
@Composable
fun ChatScreenPreview() {
    HvtdpTheme {
        ChatScreen()
    }
}
