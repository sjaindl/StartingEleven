package com.sjaindl.s11.core.player

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sjaindl.s11.core.baseui.FallbackImage
import com.sjaindl.s11.core.baseui.TextDropdown
import com.sjaindl.s11.core.baseui.UnderlinedText
import com.sjaindl.s11.core.firestore.player.model.Player
import com.sjaindl.s11.core.firestore.player.model.Position
import com.sjaindl.s11.core.model.TextDropdownMenuItem
import com.sjaindl.s11.core.theme.HvtdpTheme
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import startingeleven.core.generated.resources.Res
import startingeleven.core.generated.resources.choosePlayer
import startingeleven.core.generated.resources.playerPoints

@Composable
fun PlayerUI(
    player: Player?,
    possiblePlayers: List<Player> = emptyList(),
    displayDropdown: Boolean = false,
    onPlayerSelected: (Player) -> Unit = { },
    modifier: Modifier = Modifier,
) {
    val totalPointsOfPlayer = remember(key1 = player?.points) {
        player?.points?.values?.fold(initial = 0F,
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
        FallbackImage(photoRefDownloadUrl = player?.downloadUrl)

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            if (displayDropdown) {
                TextDropdown(
                    text = player?.name ?: stringResource(Res.string.choosePlayer),
                    menuItems = possiblePlayers.map {
                        TextDropdownMenuItem(
                            text = it.name,
                            checked = it.playerId == player?.playerId,
                        )
                    },
                    onItemSelected = { playerId ->
                        possiblePlayers.firstOrNull {
                            it.playerId == playerId
                        }?.let {
                            onPlayerSelected(it)
                        }
                    },
                    modifier = modifier,
                )
            } else {
                UnderlinedText(
                    text = player?.name ?: stringResource(Res.string.choosePlayer),
                )
            }

            totalPointsOfPlayer?.let {
                UnderlinedText(
                    text = stringResource(Res.string.playerPoints, it),
                )
            }

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
