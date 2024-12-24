package com.sjaindl.s11.prices

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sjaindl.s11.core.baseui.S11Card
import com.sjaindl.s11.core.theme.HvtdpTheme
import com.sjaindl.s11.prices.model.PricesData
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun PricesScreen(
    pricesData: PricesData,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(all = 16.dp)
            .verticalScroll(state = rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(space = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        S11Card {
            pricesData.headlines.forEach {
                PriceItem(
                    title = stringResource(resource = it),
                    subTitle = null,
                    leadingIcon = Icons.Default.Check,
                )
            }
        }

        HorizontalDivider(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            thickness = 1.dp,
        )

        S11Card {
            pricesData.prices.forEach {
                PriceItem(
                    title = it.place,
                    subTitle = stringResource(resource = it.description),
                    leadingIcon = it.icon,
                )
            }
        }
    }
}

@Preview
@Composable
fun PricesScreenPreview() {
    HvtdpTheme {
        var pricesData: PricesData? by remember {
            mutableStateOf(value = null)
        }

        LaunchedEffect(Unit) {
            pricesData = PricesData.default2024()
        }

        pricesData?.let {
            PricesScreen(pricesData = it)
        }
    }
}
