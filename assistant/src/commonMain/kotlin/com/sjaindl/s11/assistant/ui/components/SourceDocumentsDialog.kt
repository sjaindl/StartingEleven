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
import com.sjaindl.s11.assistant.data.remote.model.SourceDocument
import kotlinx.serialization.json.JsonPrimitive
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import startingeleven.assistant.generated.resources.Res
import startingeleven.assistant.generated.resources.close
import startingeleven.assistant.generated.resources.source_documents_title

@Composable
fun SourceDocumentsDialog(
    documents: List<SourceDocument>,
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
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(Res.string.source_documents_title),
                        style = MaterialTheme.typography.titleLarge,
                    )

                    IconButton(onClick = onDismissRequest) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = stringResource(Res.string.close))
                    }
                }

                Spacer(modifier = Modifier.height(height = 16.dp))

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    items(items = documents) { document ->
                        OutlinedCard(
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Text(
                                text = document.pageContent,
                                modifier = Modifier
                                    .padding(16.dp),
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun SourceDocumentsDialogPreview() {
    val documents = (1..4).map {
        SourceDocument(
            pageContent = "Content $it",
            metadata = JsonPrimitive("Metadata $it"),
        )
    }

    SourceDocumentsDialog(
        documents = documents,
        onDismissRequest = { },
    )
}
