package com.sjaindl.s11.ai.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.sjaindl.s11.ai.data.remote.model.SourceDocument
import kotlinx.serialization.json.JsonPrimitive
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun SourceDocumentsDialog(documents: List<SourceDocument>, onDismissRequest: () -> Unit) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
        )
    ) {
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Source Documents",
                    style = MaterialTheme.typography.titleLarge,
                )

                LazyColumn {
                    items(documents) { document ->
                        Text(
                            text = document.pageContent,
                            modifier = Modifier.padding(vertical = 8.dp),
                        )
                        HorizontalDivider()
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
