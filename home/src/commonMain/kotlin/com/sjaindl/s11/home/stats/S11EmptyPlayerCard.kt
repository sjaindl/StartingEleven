package com.sjaindl.s11.home.stats

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
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import startingeleven.home.generated.resources.Res
import startingeleven.home.generated.resources.noMatches

@Composable
internal fun S11EmptyPlayerCard(
    title: String,
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

            Text(text = stringResource(resource = Res.string.noMatches))
        }
    }
}

@Preview
@Composable
fun S11EmptyPlayerCardPreview() {
    HvtdpTheme {
        S11EmptyPlayerCard(
            title = "Card Title",
            modifier = Modifier.padding(8.dp)
        )
    }
}
