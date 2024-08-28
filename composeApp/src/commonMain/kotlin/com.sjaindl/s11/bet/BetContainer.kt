package com.sjaindl.s11.bet

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sjaindl.s11.bet.BetState.Content
import com.sjaindl.s11.bet.BetState.Error
import com.sjaindl.s11.bet.BetState.Initial
import com.sjaindl.s11.bet.BetState.Loading
import com.sjaindl.s11.bet.BetState.NoBets
import com.sjaindl.s11.core.baseui.ErrorScreen
import com.sjaindl.s11.core.baseui.LoadingScreen
import com.sjaindl.s11.core.baseui.S11Card
import com.sjaindl.s11.core.theme.HvtdpTheme
import com.sjaindl.s11.core.theme.spacing
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import startingeleven.composeapp.generated.resources.Res
import startingeleven.composeapp.generated.resources.betsSaved
import startingeleven.composeapp.generated.resources.noBets

@Composable
fun BetContainer(
    onShowSnackBar: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val viewModel = viewModel {
        BetViewModel()
    }

    val betState by viewModel.betState.collectAsState()
    val userBetState by viewModel.userBet.collectAsState()
    val savedBet by viewModel.savedBet.collectAsState()

    val betsSavedText = stringResource(resource = Res.string.betsSaved)

    LaunchedEffect(savedBet) {
        if (savedBet) {
            onShowSnackBar(betsSavedText)
            viewModel.resetSavedBetState()
        }
    }

    S11Card(
        modifier = modifier
            .padding(horizontal = spacing.md)
            .padding(bottom = spacing.md),
        backgroundColor = colorScheme.surfaceContainer,
        isElevated = true,
    ) {

        when (val state = betState) {
            Initial, Loading -> {
                LoadingScreen()
            }

            is Content -> {
                BetUI(
                    homeTeam = state.bet.home,
                    awayTeam = state.bet.away,
                    homeBet = userBetState.homeBet,
                    awayBet = userBetState.awayBet,
                    enabled = state.enabled,
                    onHomeBetChanged = { newBet ->
                        if (newBet >= 0) {
                            viewModel.setHomeBet(score = newBet)
                        }
                    },
                    onAwayBetChanged = { newBet ->
                        if (newBet >= 0) {
                            viewModel.setAwayBet(score = newBet)
                        }
                    },
                    onSubmitBet = viewModel::submitBet,
                    modifier = modifier,
                )
            }

            is Error -> {
                ErrorScreen(
                    modifier = modifier,
                    text = state.message,
                    onButtonClick = {
                        viewModel.loadBets()
                    },
                )
            }

            NoBets -> {
                Text(text = stringResource(resource = Res.string.noBets))
            }
        }
    }
}

@Preview
@Composable
fun BetContainerPreview() {
    HvtdpTheme {
        BetContainer(
            modifier = Modifier
                .padding(8.dp),
            onShowSnackBar = { },
        )
    }
}
