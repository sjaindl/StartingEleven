package com.sjaindl.s11.home.bet

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
import com.sjaindl.s11.home.bet.BetState.Content
import com.sjaindl.s11.home.bet.BetState.Error
import com.sjaindl.s11.home.bet.BetState.Initial
import com.sjaindl.s11.home.bet.BetState.Loading
import com.sjaindl.s11.home.bet.BetState.NoBets
import com.sjaindl.s11.core.baseui.ErrorScreen
import com.sjaindl.s11.core.baseui.LoadingScreen
import com.sjaindl.s11.core.baseui.S11Card
import com.sjaindl.s11.core.firestore.bets.model.Bet
import com.sjaindl.s11.core.theme.HvtdpTheme
import com.sjaindl.s11.core.theme.spacing
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import startingeleven.home.generated.resources.Res
import startingeleven.home.generated.resources.betSaved
import startingeleven.home.generated.resources.noBets

@Composable
fun BetContainer(
    betState: BetState,
    userBetState: UserBet,
    savedBet: Boolean,
    resetSavedBetState: () -> Unit,
    setHomeBet: (Int) -> Unit,
    setAwayBet: (Int) -> Unit,
    submitBet: () -> Unit,
    loadBets: () -> Unit,
    onShowSnackBar: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val betSavedText = stringResource(resource = Res.string.betSaved)

    LaunchedEffect(savedBet) {
        if (savedBet) {
            onShowSnackBar(betSavedText)
            resetSavedBetState()
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
                            setHomeBet(newBet)
                        }
                    },
                    onAwayBetChanged = { newBet ->
                        if (newBet >= 0) {
                            setAwayBet(newBet)
                        }
                    },
                    onSubmitBet = submitBet,
                    modifier = modifier,
                )
            }

            is Error -> {
                ErrorScreen(
                    modifier = modifier,
                    text = state.message,
                    onButtonClick = loadBets,
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
            betState = Content(
                Bet(
                    id = "1",
                    home = "HVTDP Stainz",
                    away = "Sturm Graz",
                    resultScoreHome = null,
                    resultScoreAway = null,
                ),
                enabled = true,
            ),
            userBetState = UserBet(homeBet = 1, awayBet = 1),
            savedBet = false,
            resetSavedBetState = { },
            setHomeBet = { },
            setAwayBet = { },
            submitBet = { },
            loadBets = { },
            onShowSnackBar = { },
            modifier = Modifier
                .padding(8.dp),
        )
    }
}
