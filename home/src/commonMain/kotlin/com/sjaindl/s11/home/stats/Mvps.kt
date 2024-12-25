package com.sjaindl.s11.home.stats

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
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
import startingeleven.home.generated.resources.noMatches

@Composable
fun Mvps(
    statsState: StatsState,
    loadStatistics: () -> Unit,
    modifier: Modifier = Modifier,
) {
    when (val state = statsState) {
        Initial, Loading -> {
            LoadingScreen()
        }

        is Content -> {
            S11PlayerCard(
                title = stringResource(resource = Res.string.mvpPoints),
                items = state.mvps,
                modifier = modifier,
            )
        }

        is Error -> {
            ErrorScreen(
                modifier = modifier,
                text = state.message,
                onButtonClick = loadStatistics,
            )
        }

        NoMatches -> {
            Text(text = stringResource(resource = Res.string.noMatches))
        }
    }
}

@Preview
@Composable
fun MvpsPreview() {
    HvtdpTheme {
        Mvps(
            statsState = Content(
                topElevenLastRound = emptyList(),
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
