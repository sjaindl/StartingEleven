package com.sjaindl.s11.ai.ui.components

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
import com.sjaindl.s11.core.theme.HvtdpTheme
import com.sjaindl.s11.core.theme.spacing
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import startingeleven.ai.generated.resources.Res
import startingeleven.ai.generated.resources.sample_question_1
import startingeleven.ai.generated.resources.sample_question_2

@Composable
fun SampleQuestions(onSendPrompt: (String) -> Unit) {
    val sampleQuestion1 = stringResource(Res.string.sample_question_1)
    val sampleQuestion2 = stringResource(Res.string.sample_question_2)

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
            ElevatedAssistChip(
                onClick = {
                    onSendPrompt(sampleQuestion1)
                },
                label = {
                    Text(
                        text = sampleQuestion1,
                        textAlign = TextAlign.Center,
                    )
                }
            )

            ElevatedAssistChip(
                onClick = {
                    onSendPrompt(sampleQuestion2)
                },
                label = {
                    Text(
                        text = sampleQuestion2,
                        textAlign = TextAlign.Center,
                    )
                }
            )
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
