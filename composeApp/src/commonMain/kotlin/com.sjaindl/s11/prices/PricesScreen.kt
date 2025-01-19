package com.sjaindl.s11.prices

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Celebration
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Euro
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sjaindl.s11.core.Platform
import com.sjaindl.s11.core.baseui.S11Card
import com.sjaindl.s11.core.getPlatform
import com.sjaindl.s11.core.theme.HvtdpTheme
import com.sjaindl.s11.prices.model.Price
import com.sjaindl.s11.prices.model.PricesData
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import startingeleven.composeapp.generated.resources.Res
import startingeleven.composeapp.generated.resources.prices2024Headline1
import startingeleven.composeapp.generated.resources.prices2024Headline2
import startingeleven.composeapp.generated.resources.prices2024Headline3
import startingeleven.composeapp.generated.resources.prices2024Headline4
import startingeleven.composeapp.generated.resources.prices2024place1
import startingeleven.composeapp.generated.resources.prices2024place2
import startingeleven.composeapp.generated.resources.prices2024place3
import startingeleven.composeapp.generated.resources.prices2024place4To5
import startingeleven.composeapp.generated.resources.pricesRulesHint

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

        if (getPlatform() is Platform.iOS) {
            Text(
                text = stringResource(resource = Res.string.pricesRulesHint),
            )
        }
    }
}

@Preview
@Composable
fun PricesScreenPreview() {
    HvtdpTheme {
        val data = PricesData(
            headlines = listOf(
                Res.string.prices2024Headline1,
                Res.string.prices2024Headline2,
                Res.string.prices2024Headline3,
                Res.string.prices2024Headline4,
            ),
            prices = listOf(
                Price(
                    place = "Platz 1",
                    description = Res.string.prices2024place1,
                    icon = Icons.Default.EmojiEvents,
                ),
                Price(
                    place = "Platz 2",
                    description = Res.string.prices2024place2,
                    icon = Icons.Default.Euro,
                ),
                Price(
                    place = "Platz 3",
                    description = Res.string.prices2024place3,
                    icon = Icons.Default.Campaign,
                ),
                Price(
                    place = "Plätze 4-5",
                    description = Res.string.prices2024place4To5,
                    icon = Icons.Default.Celebration,
                ),
            ),
        )

        PricesScreen(pricesData = data)
    }
}
