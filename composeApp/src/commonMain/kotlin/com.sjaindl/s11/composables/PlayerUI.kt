package com.sjaindl.s11.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.LocalPlatformContext
import coil3.compose.SubcomposeAsyncImage
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import com.sjaindl.s11.core.baseui.LoadingScreen
import com.sjaindl.s11.core.firestore.player.model.Player
import com.sjaindl.s11.core.firestore.player.model.Position
import com.sjaindl.s11.core.theme.HvtdpTheme
import kotlinx.coroutines.Dispatchers
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import startingeleven.composeapp.generated.resources.Res
import startingeleven.composeapp.generated.resources.no_photo
import startingeleven.composeapp.generated.resources.playerPoints

@Composable
fun PlayerUI(
    player: Player,
    modifier: Modifier = Modifier,
) {
    val context = LocalPlatformContext.current

    val totalPointsOfPlayer = remember(key1 = player.points) {
        player.points.values.fold(initial = 0,
            operation = { sum, pointsOfMatchDay ->
                sum + pointsOfMatchDay
            }
        )
    }

    Row(
        modifier = modifier
            .padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        val url = player.downloadUrl
        if (url != null) {
            val imageRequest = ImageRequest.Builder(context = context)
                .data(url)
                .dispatcher(Dispatchers.Default)
                .memoryCacheKey(url)
                .diskCacheKey(url)
                .diskCachePolicy(CachePolicy.ENABLED)
                .memoryCachePolicy(CachePolicy.ENABLED)
                .build()

            SubcomposeAsyncImage(
                modifier = Modifier
                    .size(120.dp),
                model = imageRequest,
                loading = {
                    LoadingScreen(
                        modifier = Modifier
                            .size(120.dp)
                            .border(width = 1.dp, color = colorScheme.onBackground)
                            .padding(16.dp),
                    )
                },
                contentScale = ContentScale.Fit,
                contentDescription = null,
            )
        } else {
            Image(
                painter = painterResource(Res.drawable.no_photo),
                contentDescription = null,
                modifier = Modifier
                    .size(120.dp),
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            UnderlinedText(
                text = player.name,
            )

            UnderlinedText(
                text = stringResource(Res.string.playerPoints, totalPointsOfPlayer),
            )

            // TODO: # Aufstellungen Spieltag
        }
    }
}

@Preview
@Composable
fun PlayerPreview() {
    HvtdpTheme {
        PlayerUI(
            player = Player(
                playerId = "1",
                name = "Del Piero",
                position = Position.Attacker,
                imageRef = null,
                downloadUrl = null,
                points = emptyMap(),
            ),
        )
    }
}
