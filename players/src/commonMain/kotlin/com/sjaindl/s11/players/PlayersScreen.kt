package com.sjaindl.s11.composables

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
import com.sjaindl.s11.PlayerState
import com.sjaindl.s11.PlayerViewModel
import com.sjaindl.s11.core.baseui.ErrorScreen
import com.sjaindl.s11.core.baseui.LoadingScreen
import com.sjaindl.s11.core.player.PlayerUI
import com.sjaindl.s11.core.theme.HvtdpTheme

@Composable
fun PlayersScreen(
    modifier: Modifier = Modifier,
) {
    val playerViewModel = viewModel {
        PlayerViewModel()
    }

    LaunchedEffect(Unit) {
        playerViewModel.loadPlayers()
    }

    val players by playerViewModel.playerState.collectAsState()

    when (val state = players) {
        is PlayerState.Error -> {
            ErrorScreen(
                modifier = modifier
                    .fillMaxSize()
                    .wrapContentSize(),
                text = state.message,
                onButtonClick = {
                  playerViewModel.loadPlayers()
                },
            )
        }
        PlayerState.Initial, PlayerState.Loading -> {
            LoadingScreen(
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
                    PlayerUI(player = it)
                }
            }
        }
    }
}

@Composable
fun PlayersScreenPreview() {
    HvtdpTheme {
        PlayersScreen()
    }
}
