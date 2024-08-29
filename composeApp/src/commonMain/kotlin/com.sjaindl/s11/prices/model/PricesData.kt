package com.sjaindl.s11.prices.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Celebration
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Euro
import androidx.compose.ui.graphics.vector.ImageVector

data class PricesData(
    val headlines: List<String>,
    val prices: List<Price>,
) {
    companion object {
        val default2024 = PricesData(
            headlines = listOf(
                "Melde dich jetzt an für den Starting Eleven Wettbewerb 2024/2025",
                "Entwickle für dein Team die perfekte Formation und Aufstellung",
                "Erziele möglichst viele Punkte",
                "...und gewinne am Ende der Saison einen der folgenden großartigen Preise"
            ),
            prices = listOf(
                Price(
                    place = "Platz 1",
                    description = "50 € Gutschein im HV TDP Stainz Fanshop",
                    icon = Icons.Default.EmojiEvents,
                ),
                Price(
                    place = "Platz 2",
                    description = "30 € Gutschein im HV TDP Stainz Fanshop",
                    icon = Icons.Default.Euro,
                ),
                Price(
                    place = "Platz 3",
                    description = "10 € Gutschein im HV TDP Stainz Fanshop",
                    icon = Icons.Default.Campaign,
                ),
                Price(
                    place = "Plätze 4-5",
                    description = "1 Getränk & Essen bei einem HV TDP Stainz Heimspiel",
                    icon = Icons.Default.Celebration,
                ),
            ),
        )
    }
}

data class Price(
    val place: String,
    val description: String,
    val icon: ImageVector,
)
