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
import startingeleven.home.generated.resources.topEleven

@Composable
fun Top11OfRound(
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
                title = stringResource(resource = Res.string.topEleven),
                items = statsState.topElevenLastRound,
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
fun Top11OfRoundPreview() {
    HvtdpTheme {
        Top11OfRound(
            statsState = Content(
                topElevenLastRound = listOf(
                    PlayerCardItem(name = "Del Piero", points = 10f),
                    PlayerCardItem(name = "Inzaghi", points = 9f),
                ),
                mvps = emptyList(),
            ),
            loadStatistics = { },
            modifier = Modifier
                .padding(8.dp),
        )
    }
}
