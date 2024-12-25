package com.sjaindl.s11.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import com.sjaindl.s11.core.firestore.bets.model.Bet
import com.sjaindl.s11.core.theme.HvtdpTheme
import com.sjaindl.s11.core.theme.spacing
import com.sjaindl.s11.home.bet.BetContainer
import com.sjaindl.s11.home.bet.BetState
import com.sjaindl.s11.home.bet.UserBet
import com.sjaindl.s11.home.recommender.LineupRecommendation
import com.sjaindl.s11.home.recommender.RecommendationState
import com.sjaindl.s11.home.stats.Mvps
import com.sjaindl.s11.home.stats.StatsState
import com.sjaindl.s11.home.stats.Top11OfRound
import com.sjaindl.s11.home.stats.model.PlayerCardItem
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import startingeleven.home.generated.resources.Res
import startingeleven.home.generated.resources.hello
import startingeleven.home.generated.resources.home

@Composable
fun HomeScreen(
    displayName: String?,
    onAuthenticated: (Boolean) -> Unit,
    recommendationState: RecommendationState,
    betState: BetState,
    userBetState: UserBet,
    statsState: StatsState,
    savedBet: Boolean,
    loadRecommendations: () -> Unit,
    resetSavedBetState: () -> Unit,
    setHomeBet: (Int) -> Unit,
    setAwayBet: (Int) -> Unit,
    submitBet: () -> Unit,
    loadBets: () -> Unit,
    loadStatistics: () -> Unit,
    onShowSnackBar: (String) -> Unit,
) {
    LaunchedEffect(displayName) {
        onAuthenticated(displayName != null)
    }

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(spacing.xl),
    ) {
        Image(
            painter = painterResource(Res.drawable.home),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
        )

        if (displayName != null) {
            Text(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Normal)) {
                        append(text = "${stringResource(Res.string.hello)} ")
                    }
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append(text = displayName)
                    }
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Normal)) {
                        append(text = "!")
                    }
                },
                style = typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(horizontal = spacing.md),
            )
        }

        LineupRecommendation(
            recommendationState = recommendationState,
            loadRecommendations = loadRecommendations,
        )

        BetContainer(
            betState = betState,
            userBetState = userBetState,
            savedBet = savedBet,
            resetSavedBetState = resetSavedBetState,
            setHomeBet = setHomeBet,
            setAwayBet = setAwayBet,
            submitBet = submitBet,
            loadBets = loadBets,
            onShowSnackBar = onShowSnackBar,
        )

        Top11OfRound(
            statsState = statsState,
            loadStatistics = loadStatistics,
            modifier = Modifier
                .padding(horizontal = spacing.md),
        )

        Mvps(
            statsState = statsState,
            loadStatistics = loadStatistics,
            modifier = Modifier
                .padding(horizontal = spacing.md)
                .padding(bottom = spacing.xl),
        )
    }
}

@Composable
@Preview
fun HomeScreenPreview() {
    HvtdpTheme {
        HomeScreen(
            displayName = "User Name",
            onAuthenticated = { },
            recommendationState = RecommendationState.NoRecommendation,
            betState = BetState.Content(
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
            statsState = StatsState.Content(
                topElevenLastRound = listOf(
                    PlayerCardItem(name = "Del Piero", points = 10f),
                    PlayerCardItem(name = "Inzaghi", points = 9f),
                ),
                mvps = listOf(
                    PlayerCardItem(name = "Inzaghi", points = 10f),
                    PlayerCardItem(name = "Del Piero", points = 9f),
                ),
            ),
            savedBet = false,
            loadRecommendations = { },
            resetSavedBetState = { },
            setHomeBet = { },
            setAwayBet = { },
            submitBet = { },
            loadBets = { },
            loadStatistics = { },
            onShowSnackBar = { },
        )
    }
}
