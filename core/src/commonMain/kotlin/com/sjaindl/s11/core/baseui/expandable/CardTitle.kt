package com.sjaindl.s11.core.baseui.expandable

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.sjaindl.s11.core.theme.HvtdpTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun CardTitle(
    title: String,
    modifier: Modifier = Modifier,
) {
    HvtdpTheme {
        Text(
            text = title,
            modifier = modifier
                .padding(16.dp),
            textAlign = TextAlign.Center,
        )
    }
}

@Preview
@Composable
fun CardTitlePreview() {
    CardTitle(
        title = "Card"
    )
}
