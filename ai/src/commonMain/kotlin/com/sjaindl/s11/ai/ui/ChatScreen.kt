package com.sjaindl.s11.ai.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedAssistChip
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
import com.sjaindl.s11.ai.data.remote.model.SourceDocument
import com.sjaindl.s11.ai.data.remote.model.Tool
import com.sjaindl.s11.ai.ui.components.SourceDocumentsDialog
import com.sjaindl.s11.ai.ui.components.UsedToolsDialog
import com.sjaindl.s11.core.baseui.ErrorScreen
import com.sjaindl.s11.core.baseui.LoadingScreen
import com.sjaindl.s11.core.theme.HvtdpTheme
import com.sjaindl.s11.core.theme.spacing
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

    var showToolsDialogFor by remember {
        mutableStateOf<List<Tool>?>(null)
    }

    var showSourceDocsDialogFor by remember {
        mutableStateOf<List<SourceDocument>?>(null)
    }

    if (uiState.error != null) {
        ErrorScreen(text = uiState.error!!)
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize(),
    ) {
        LazyColumn(
            modifier = Modifier
                .weight(1f),
        ) {
            items(uiState.messages) { message ->
                Text(
                    text = "${if (message.isFromUser) "You" else "AI"}: ${message.text}",
                    modifier = Modifier
                        .padding(8.dp),
                )

                Row(horizontalArrangement = Arrangement.spacedBy(spacing.s)) {
                    message.usedTools?.let { tools ->
                        ElevatedAssistChip(
                            onClick = {
                                showToolsDialogFor = tools
                            },
                            label = {
                                Text("Used Tools (${tools.size})")
                            }
                        )
                    }

                    message.sourceDocuments?.let { docs ->
                        ElevatedAssistChip(
                            onClick = {
                                showSourceDocsDialogFor = docs
                            },
                            label = {
                                Text("Source Documents (${docs.size})")
                            }
                        )
                    }
                }
            }
        }

        showToolsDialogFor?.let { tools ->
            UsedToolsDialog(
                tools = tools,
                onDismissRequest = {
                    showToolsDialogFor = null
                }
            )
        }

        showSourceDocsDialogFor?.let { docs ->
            SourceDocumentsDialog(
                documents = docs,
                onDismissRequest = {
                    showSourceDocsDialogFor = null
                }
            )
        }

        if (uiState.isLoading) {
            LoadingScreen()
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(spacing.s),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            TextField(
                value = prompt,
                onValueChange = {
                    prompt = it
                },
                modifier = Modifier
                    .weight(1f),
            )

            Button(
                onClick = {
                    chatViewModel.sendPrompt(prompt = prompt)
                    prompt = ""
                },
                enabled = prompt.isNotBlank() && !uiState.isLoading
            ) {
                Text(text = "Send")
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
