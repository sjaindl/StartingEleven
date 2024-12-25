package com.sjaindl.s11.players

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sjaindl.s11.core.baseui.ErrorScreen
import com.sjaindl.s11.core.baseui.LoadingScreen
import com.sjaindl.s11.core.firestore.player.model.Player
import com.sjaindl.s11.core.firestore.player.model.Position
import com.sjaindl.s11.core.player.PlayerUI
import com.sjaindl.s11.core.theme.HvtdpTheme
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import startingeleven.players.generated.resources.Res
import startingeleven.players.generated.resources.calculating

@Composable
fun PlayersScreen(
    playerState: PlayerState,
    loadPlayers: () -> Unit,
    modifier: Modifier = Modifier,
) {

    LaunchedEffect(Unit) {
        loadPlayers()
    }

    when (val state = playerState) {
        is PlayerState.Error -> {
            ErrorScreen(
                modifier = modifier
                    .fillMaxSize()
                    .wrapContentSize(),
                text = state.message,
                onButtonClick = {
                    loadPlayers()
                },
            )
        }

        PlayerState.Initial -> {
            LoadingScreen(
                modifier = modifier
                    .fillMaxSize()
                    .wrapContentSize(),
            )
        }

        is PlayerState.Loading -> {
            LoadingScreen(
                loadingInfo = state.playerName?.let {
                    stringResource(resource = Res.string.calculating, it)
                },
                modifier = modifier
                    .fillMaxSize()
                    .wrapContentSize(),
            )
        }

        is PlayerState.Success -> {
            LazyColumn(
                modifier = modifier,
            ) {
                items(state.players) {
                    PlayerUI(player = it.player, lineupCount = it.lineupCount)
                }
            }
        }
    }
}

@Preview
@Composable
fun PlayersScreenPreview() {
    val players = listOf(
        PlayerWithLineupCount(
            player = Player(
                playerId = "delPiero",
                name = "Del Piero",
                position = Position.Attacker,
                imageRef = null,
                downloadUrl = null,
                points = mapOf("delPiero" to 5f),
            ),
            lineupCount = 10,
        ),
        PlayerWithLineupCount(
            player = Player(
                playerId = "inzaghi",
                name = "Inzaghi",
                position = Position.Attacker,
                imageRef = null,
                downloadUrl = null,
                points = mapOf("inzaghi" to 4f),
            ),
            lineupCount = 9,
        ),
    )

    HvtdpTheme {
        PlayersScreen(
            playerState = PlayerState.Success(players = players),
            loadPlayers = { },
        )
    }
}
