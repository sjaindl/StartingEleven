package com.sjaindl.s11.ai.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.sjaindl.s11.ai.data.remote.model.Tool
import com.sjaindl.s11.core.theme.spacing
import kotlinx.serialization.json.JsonPrimitive
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun UsedToolsDialog(tools: List<Tool>, onDismissRequest: () -> Unit) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
        )
    ) {
        Card {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(spacing.s),
            ) {
                Text(
                    text = "Tools",
                    style = MaterialTheme.typography.titleLarge,
                )

                tools.forEach { tool ->
                    Text("Tool:\n${tool.tool}", style = MaterialTheme.typography.titleMedium)
                    Text("Input:\n${tool.toolInput}")
                    Text("Output:\n${tool.toolOutput}")
                }
            }
        }
    }
}

@Preview
@Composable
fun UsedToolsDialogPreview() {
    val tools = (1..4).map {
        Tool(
            tool = "Tool $it",
            toolInput = JsonPrimitive("Input $it"),
            toolOutput = "Output $it",
        )
    }

    UsedToolsDialog(
        tools = tools,
        onDismissRequest = { },
    )
}
