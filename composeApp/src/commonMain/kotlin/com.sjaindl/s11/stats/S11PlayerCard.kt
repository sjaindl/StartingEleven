package com.sjaindl.s11.stats

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.sjaindl.s11.core.baseui.S11Card
import com.sjaindl.s11.core.theme.HvtdpTheme
import com.sjaindl.s11.core.theme.spacing
import com.sjaindl.s11.stats.model.PlayerCardItem
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
internal fun S11PlayerCard(
    title: String,
    items: List<PlayerCardItem>,
    modifier: Modifier = Modifier,
) {
    S11Card(
        modifier = modifier
            .fillMaxWidth(),
        backgroundColor = colorScheme.surfaceContainer,
        isElevated = true,
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = spacing.md),
            verticalArrangement = Arrangement
                .spacedBy(spacing.xs),
        ) {
            Text(
                text = title,
                modifier = Modifier
                    .padding(bottom = 8.dp),
                style = typography.bodyLarge,
                fontWeight = FontWeight.Bold,
            )

            items.forEach {
                Text(
                    text = "${it.name} (${it.points})",
                )
            }
        }
    }
}

@Preview
@Composable
fun S11PlayerCardPreview() {
    HvtdpTheme {
        S11PlayerCard(
            title = "Card Title",
            items = listOf(
                PlayerCardItem(name = "Daniel Fabian", points = 18.5f),
                PlayerCardItem(name = "Philipp Bohacek", points = 16f),
                PlayerCardItem(name = "Stefan Jaindl", points = 15f),
                PlayerCardItem(name = "Hrvoje Sincek", points = 11f),
            ),
            modifier = Modifier.padding(8.dp)
        )
    }
}
