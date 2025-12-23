package com.sjaindl.s11.ai.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedAssistChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import com.sjaindl.s11.ai.data.remote.model.SourceDocument
import com.sjaindl.s11.ai.data.remote.model.Tool
import com.sjaindl.s11.ai.ui.components.JumpingDotsIndicator
import com.sjaindl.s11.ai.ui.components.SourceDocumentsDialog
import com.sjaindl.s11.ai.ui.components.UsedToolsDialog
import com.sjaindl.s11.core.baseui.ErrorScreen
import com.sjaindl.s11.core.theme.HvtdpTheme
import com.sjaindl.s11.core.theme.spacing
import kotlinx.serialization.json.JsonPrimitive
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import startingeleven.ai.generated.resources.Res
import startingeleven.ai.generated.resources.chat_user
import startingeleven.ai.generated.resources.chatbot

@Composable
fun ChatScreen(
    modifier: Modifier = Modifier,
    uiState: ChatUiState,
    onSendPrompt: (String) -> Unit,
) {

    var prompt by remember {
        mutableStateOf("")
    }

    var showToolsDialogFor by remember {
        mutableStateOf<List<Tool>?>(null)
    }

    var showSourceDocsDialogFor by remember {
        mutableStateOf<List<SourceDocument>?>(null)
    }

    val keyboardController = LocalSoftwareKeyboardController.current
    val listState = rememberLazyListState()

    LaunchedEffect(uiState.messages, uiState.isLoading) {
        if (uiState.messages.isNotEmpty() || uiState.isLoading) {
            listState.animateScrollToItem(index = listState.layoutInfo.totalItemsCount)
        }
    }

    uiState.error?.let {
        ErrorScreen(
            modifier = modifier,
            text = it,
        )
    } ?: Column(
        modifier = modifier
            .fillMaxSize(),
    ) {
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f),
        ) {
            items(items = uiState.messages) { message ->
                Card(
                    modifier = Modifier
                        .padding(all = 8.dp)
                        .fillMaxWidth(),
                ) {
                    Row(
                        modifier = Modifier
                            .padding(all = 8.dp),
                        verticalAlignment = Alignment.Top,
                    ) {
                        Image(
                            painter = painterResource(if (message.isFromUser) Res.drawable.chat_user else Res.drawable.chatbot),
                            contentDescription = null,
                            modifier = Modifier
                                .size(size = 40.dp)
                                .clip(CircleShape),
                        )

                        Spacer(
                            modifier = Modifier
                                .size(size = 8.dp),
                        )

                        Column {
                            Text(text = message.text)

                            if (message.isTyping) {
                                Spacer(
                                    modifier = Modifier
                                        .size(size = 16.dp)
                                )

                                JumpingDotsIndicator()
                            }

                            Row(
                                horizontalArrangement = Arrangement.spacedBy(space = spacing.s)
                            ) {
                                message.usedTools?.let { tools ->
                                    ElevatedAssistChip(
                                        onClick = {
                                            showToolsDialogFor = tools
                                        },
                                        label = {
                                            Text("Used Tools (${tools.size})")
                                        },
                                        colors = AssistChipDefaults.elevatedAssistChipColors(
                                            containerColor = colorScheme.secondaryContainer,
                                        )
                                    )
                                }
                                message.sourceDocuments?.let { docs ->
                                    ElevatedAssistChip(
                                        onClick = {
                                            showSourceDocsDialogFor = docs
                                        },
                                        label = {
                                            Text("Source Documents (${docs.size})")
                                        },
                                        colors = AssistChipDefaults.elevatedAssistChipColors(
                                            containerColor = colorScheme.tertiaryContainer,
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            }

            if (uiState.isLoading) {
                item {
                    Row(
                        modifier = Modifier
                            .padding(all = 8.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Image(
                            painter = painterResource(Res.drawable.chatbot),
                            contentDescription = "",
                            modifier = Modifier
                                .size(size = 40.dp)
                                .clip(CircleShape)
                        )

                        Spacer(
                            modifier = Modifier
                                .size(size = 8.dp)
                        )

                        JumpingDotsIndicator()
                    }
                }
            }
        }

        showToolsDialogFor?.let { tools ->
            UsedToolsDialog(
                tools = tools,
                onDismissRequest = { showToolsDialogFor = null }
            )
        }

        showSourceDocsDialogFor?.let { docs ->
            SourceDocumentsDialog(
                documents = docs,
                onDismissRequest = { showSourceDocsDialogFor = null }
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(space = spacing.md),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            TextField(
                value = prompt,
                onValueChange = {
                    prompt = it
                },
                modifier = Modifier
                    .weight(weight = 1f),
            )
            Button(
                onClick = {
                    onSendPrompt(prompt)
                    prompt = ""
                    keyboardController?.hide()
                },
                enabled = prompt.isNotBlank() && !uiState.isLoading
            ) {
                Icon(imageVector = Icons.AutoMirrored.Filled.Send, contentDescription = null)
            }
        }
    }
}

@Preview
@Composable
fun ChatScreenPreview() {
    HvtdpTheme {
        ChatScreen(
            uiState = ChatUiState(
                isLoading = false,
                messages = listOf(
                    ChatMessage(
                        text = "Hello",
                        isFromUser = true,
                    ),
                    ChatMessage(
                        text = "Hello! How can I help you?",
                        isFromUser = false,
                    ),
                    ChatMessage(
                        text = "Who are the bosses of the club?",
                        isFromUser = true,
                    ),
                    ChatMessage(
                        text = "The bosses are top secret!",
                        isFromUser = false,
                        usedTools = listOf(
                            Tool(tool = "Search", toolInput = JsonPrimitive("input"), toolOutput = "output")
                        ),
                        sourceDocuments = listOf(
                            SourceDocument(
                                pageContent = "pageContent",
                                metadata = JsonPrimitive("metadata")
                            )
                        )
                    ),
                    ChatMessage(
                        text = "And what is the next event?",
                        isFromUser = true,
                    ),
                    ChatMessage(
                        text = "The next event is ",
                        isFromUser = false,
                        isTyping = true,
                    ),
                )
            ),
            onSendPrompt = { }
        )
    }
}
