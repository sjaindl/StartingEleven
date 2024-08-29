package com.sjaindl.s11.prices

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.sjaindl.s11.core.theme.HvtdpTheme
import com.sjaindl.s11.prices.model.PricesData
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun PriceItem(
    title: String,
    subTitle: String?,
    leadingIcon: ImageVector?,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        leadingIcon?.let {
            Icon(
                imageVector = it,
                contentDescription = null,
                modifier = Modifier
                    .padding(end = 12.dp)
            )
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(space = 4.dp)
        ) {
            Text(
                text = title,
                fontWeight = if (subTitle == null) {
                    FontWeight.Normal
                } else {
                    FontWeight.Bold
                },
            )

            subTitle?.let {
                Text(
                    text = it,
                    style = typography.bodySmall.copy(
                        color = colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Medium,
                    ),
                )
            }
        }
    }
}


@Preview
@Composable
fun PriceItemPreview() {
    val item = PricesData.default2024.prices.first()

    HvtdpTheme {
        with(item) {
            PriceItem(
                title = place,
                subTitle = description,
                leadingIcon = icon,
            )
        }
    }
}
