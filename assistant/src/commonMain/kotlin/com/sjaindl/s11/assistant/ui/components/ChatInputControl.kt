package com.sjaindl.s11.assistant.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import com.sjaindl.s11.assistant.config.AssistantConfig
import com.sjaindl.s11.assistant.ui.ChatUiState
import com.sjaindl.s11.core.theme.HvtdpTheme
import com.sjaindl.s11.core.theme.spacing
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject
import startingeleven.assistant.generated.resources.Res
import startingeleven.assistant.generated.resources.character_limit_exceeded

@Composable
fun ChatInputControl(
    uiState: ChatUiState,
    onSendPrompt: (String) -> Unit,
) {
    var prompt by remember {
        mutableStateOf("")
    }

    val keyboardController = LocalSoftwareKeyboardController.current
    val config = koinInject<AssistantConfig>()

    val isError = prompt.length > config.messageCharLimit

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
            isError = isError,
            placeholder = {
                Text(stringResource(config.promptPlaceholder))
            },
            supportingText = {
                if (isError) {
                    Text(stringResource(Res.string.character_limit_exceeded))
                }
            }
        )
        Button(
            onClick = {
                onSendPrompt(prompt)
                prompt = ""
                keyboardController?.hide()
            },
            enabled = prompt.isNotBlank() && !uiState.isLoading && !isError
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.Send,
                contentDescription = null,
            )
        }
    }
}

@Preview
@Composable
fun ChatInputControlPreview() {
    HvtdpTheme {
        ChatInputControl(
            uiState = ChatUiState(
                isLoading = false,
            ),
            onSendPrompt = {}
        )
    }
}
