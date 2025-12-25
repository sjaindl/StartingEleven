package com.sjaindl.s11.assistant.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ElevatedAssistChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.sjaindl.s11.assistant.config.AssistantConfig
import com.sjaindl.s11.core.theme.HvtdpTheme
import com.sjaindl.s11.core.theme.spacing
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject

@Composable
fun SampleQuestions(onSendPrompt: (String) -> Unit) {
    val config = koinInject<AssistantConfig>()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = spacing.md, vertical = spacing.lg),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(spacing.md)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(spacing.xxs),
        ) {
            config.sampleQuestions.forEach { question ->
                val text = stringResource(question)

                ElevatedAssistChip(
                    onClick = {
                        onSendPrompt(text)
                    },
                    label = {
                        Text(
                            text = text,
                            textAlign = TextAlign.Center,
                        )
                    }
                )
            }
        }
    }
}

@Preview
@Composable
fun SampleQuestionsPreview() {
    HvtdpTheme {
        SampleQuestions(onSendPrompt = {})
    }
}
