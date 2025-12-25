package com.sjaindl.s11.assistant.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.sjaindl.s11.assistant.data.remote.model.Tool
import com.sjaindl.s11.core.theme.spacing
import kotlinx.serialization.json.JsonPrimitive
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import startingeleven.assistant.generated.resources.Res
import startingeleven.assistant.generated.resources.close
import startingeleven.assistant.generated.resources.tool_input
import startingeleven.assistant.generated.resources.tool_name
import startingeleven.assistant.generated.resources.tool_output
import startingeleven.assistant.generated.resources.tools_title

@Composable
fun UsedToolsDialog(
    tools: List<Tool>,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
        )
    ) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 32.dp),
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = stringResource(Res.string.tools_title),
                        style = MaterialTheme.typography.titleLarge,
                    )
                    IconButton(onClick = onDismissRequest) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = stringResource(Res.string.close))
                    }
                }

                Spacer(Modifier.height(16.dp))

                LazyColumn(
                    verticalArrangement = Arrangement
                        .spacedBy(space = 8.dp),
                ) {
                    items(items = tools) { tool ->
                        OutlinedCard(
                            modifier = Modifier
                                .fillMaxWidth(),
                        ) {
                            Column(
                                modifier = Modifier
                                    .padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(space = spacing.s),
                            ) {
                                Text(stringResource(Res.string.tool_name, tool.tool), style = MaterialTheme.typography.titleMedium)

                                Text(stringResource(Res.string.tool_input, tool.toolInput))

                                Text(stringResource(Res.string.tool_output, tool.toolOutput))
                            }
                        }
                    }
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
