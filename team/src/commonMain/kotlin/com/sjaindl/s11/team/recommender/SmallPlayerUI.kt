package com.sjaindl.s11.team.recommender

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sjaindl.s11.core.baseui.FallbackImage
import com.sjaindl.s11.core.firestore.player.model.Player
import com.sjaindl.s11.core.firestore.player.model.Position
import com.sjaindl.s11.core.theme.HvtdpTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun SmallPlayerUI(
    player: Player,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        FallbackImage(photoRefDownloadUrl = player.downloadUrl, maxSize = 40.dp)

        Text(
            text = player.name.split(" ").lastOrNull() ?: player.name,
            style = typography.bodyLarge,
        )
    }
}

@Preview
@Composable
fun SmallPlayerUIPreview() {
    HvtdpTheme {
        SmallPlayerUI(
            player = Player(
                playerId = "1",
                name = "Del Piero",
                positions = listOf(Position.Attacker),
                imageRef = null,
                downloadUrl = null,
                points = emptyMap(),
            ),
        )
    }
}
