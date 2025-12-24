package com.sjaindl.s11.ai.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedAssistChip
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.sjaindl.s11.ai.data.remote.model.SourceDocument
import com.sjaindl.s11.ai.data.remote.model.Tool
import com.sjaindl.s11.ai.ui.ChatMessage
import com.sjaindl.s11.core.theme.HvtdpTheme
import com.sjaindl.s11.core.theme.spacing
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import startingeleven.ai.generated.resources.Res
import startingeleven.ai.generated.resources.chat_user
import startingeleven.ai.generated.resources.chatbot

@Composable
fun MessageCard(
    message: ChatMessage,
) {
    var showToolsDialogFor by remember {
        mutableStateOf<List<Tool>?>(null)
    }

    var showSourceDocsDialogFor by remember {
        mutableStateOf<List<SourceDocument>?>(null)
    }

    Card(
        modifier = Modifier
            .padding(all = 8.dp)
            .fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier
                .padding(all = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                painter = painterResource(if (message.isFromUser) Res.drawable.chat_user else Res.drawable.chatbot),
                contentDescription = null,
                modifier = Modifier
                    .size(size = 40.dp)
                    .clip(CircleShape)
                    .align(Alignment.Top),
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

    showToolsDialogFor?.let { tools ->
        UsedToolsDialog(
            tools = tools,
            onDismissRequest = {
                showToolsDialogFor = null
            },
        )
    }

    showSourceDocsDialogFor?.let { docs ->
        SourceDocumentsDialog(
            documents = docs,
            onDismissRequest = {
                showSourceDocsDialogFor = null
            },
        )
    }
}

@Preview
@Composable
fun MessageCardPreview() {
    HvtdpTheme {
        MessageCard(
            message = ChatMessage(
                text = "This is a preview with a very long text to see how it looks like",
                isFromUser = false
            )
        )
    }
}
