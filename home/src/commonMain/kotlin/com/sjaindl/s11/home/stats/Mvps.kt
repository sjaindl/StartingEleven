package com.sjaindl.s11.home.stats

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sjaindl.s11.core.baseui.ErrorScreen
import com.sjaindl.s11.core.baseui.LoadingScreen
import com.sjaindl.s11.core.theme.HvtdpTheme
import com.sjaindl.s11.home.stats.StatsState.Content
import com.sjaindl.s11.home.stats.StatsState.Error
import com.sjaindl.s11.home.stats.StatsState.Initial
import com.sjaindl.s11.home.stats.StatsState.Loading
import com.sjaindl.s11.home.stats.StatsState.NoMatches
import com.sjaindl.s11.home.stats.model.PlayerCardItem
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import startingeleven.home.generated.resources.Res
import startingeleven.home.generated.resources.mvpPoints

@Composable
fun Mvps(
    statsState: StatsState,
    loadStatistics: () -> Unit,
    modifier: Modifier = Modifier,
) {
    when (statsState) {
        Initial, Loading -> {
            LoadingScreen()
        }

        is Content -> {
            S11PlayerCard(
                title = stringResource(resource = Res.string.mvpPoints),
                items = statsState.mvps,
                modifier = modifier,
            )
        }

        is Error -> {
            ErrorScreen(
                modifier = modifier,
                text = statsState.message,
                onButtonClick = loadStatistics,
            )
        }

        NoMatches -> {
            S11EmptyPlayerCard(
                title = stringResource(resource = Res.string.mvpPoints),
                modifier = modifier,
            )
        }
    }
}

@Preview
@Composable
fun MvpsPreview() {
    HvtdpTheme {
        Mvps(
            statsState = Content(
                topPlayersLastRound = emptyList(),
                mvps = listOf(
                    PlayerCardItem(name = "Inzaghi", points = 10f),
                    PlayerCardItem(name = "Del Piero", points = 9f),
                ),
            ),
            loadStatistics = { },
            modifier = Modifier
                .padding(8.dp),
        )
    }
}
