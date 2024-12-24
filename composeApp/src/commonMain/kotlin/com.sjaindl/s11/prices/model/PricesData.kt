package com.sjaindl.s11.prices.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Celebration
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Euro
import androidx.compose.ui.graphics.vector.ImageVector
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.getString
import startingeleven.composeapp.generated.resources.Res
import startingeleven.composeapp.generated.resources.place
import startingeleven.composeapp.generated.resources.places
import startingeleven.composeapp.generated.resources.prices2024Headline1
import startingeleven.composeapp.generated.resources.prices2024Headline2
import startingeleven.composeapp.generated.resources.prices2024Headline3
import startingeleven.composeapp.generated.resources.prices2024Headline4
import startingeleven.composeapp.generated.resources.prices2024place1
import startingeleven.composeapp.generated.resources.prices2024place2
import startingeleven.composeapp.generated.resources.prices2024place3
import startingeleven.composeapp.generated.resources.prices2024place4To5

data class PricesData(
    val headlines: List<StringResource>,
    val prices: List<Price>,
) {
    companion object {
        suspend fun default2024() = PricesData(
            headlines = listOf(
                Res.string.prices2024Headline1,
                Res.string.prices2024Headline2,
                Res.string.prices2024Headline3,
                Res.string.prices2024Headline4,
            ),
            prices = listOf(
                Price(
                    place = getString(resource = Res.string.place, 1),
                    description = Res.string.prices2024place1,
                    icon = Icons.Default.EmojiEvents,
                ),
                Price(
                    place = getString(resource = Res.string.place, 2),
                    description = Res.string.prices2024place2,
                    icon = Icons.Default.Euro,
                ),
                Price(
                    place = getString(resource = Res.string.place, 3),
                    description = Res.string.prices2024place3,
                    icon = Icons.Default.Campaign,
                ),
                Price(
                    place = getString(resource = Res.string.places, 4, 5),
                    description = Res.string.prices2024place4To5,
                    icon = Icons.Default.Celebration,
                ),
            ),
        )
    }
}

data class Price(
    val place: String,
    val description: StringResource,
    val icon: ImageVector,
)
