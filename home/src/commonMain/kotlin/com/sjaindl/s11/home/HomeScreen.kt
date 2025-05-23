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
import com.sjaindl.s11.core.theme.HvtdpTheme
import com.sjaindl.s11.core.theme.spacing
import com.sjaindl.s11.home.news.MatchdayNewsScreen
import com.sjaindl.s11.home.news.NewsScreen
import com.sjaindl.s11.home.news.NewsState
import com.sjaindl.s11.home.news.NewsState.Content
import com.sjaindl.s11.home.stats.Mvps
import com.sjaindl.s11.home.stats.StatsState
import com.sjaindl.s11.home.stats.TopPlayersOfRound
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
    isAuthenticated: Boolean,
    onAuthenticated: (Boolean) -> Unit,
    newsState: NewsState,
    statsState: StatsState,
    loadStatistics: () -> Unit,
    loadNews: () -> Unit,
) {
    LaunchedEffect(isAuthenticated) {
        onAuthenticated(isAuthenticated)
    }

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(bottom = spacing.lg),
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

        NewsScreen(
            newsState = newsState,
            loadNews = loadNews,
            modifier = Modifier
                .padding(horizontal = spacing.md),
        )

        TopPlayersOfRound(
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

        MatchdayNewsScreen(
            newsState = newsState,
            loadNews = loadNews,
            modifier = Modifier
                .padding(horizontal = spacing.md),
        )
    }
}

@Composable
@Preview
fun HomeScreenPreview() {
    HvtdpTheme {
        HomeScreen(
            displayName = "User Name",
            isAuthenticated = true,
            onAuthenticated = { },
            newsState = Content(
                generalNews = "Ich bin der erste Test-News-Eintrag in der coolen neuen Starting 11 App!",
                matchdayNews = "Matchday Newseintrag!",
                matchdayImageDownloadUrl = null,
            ),
            statsState = StatsState.Content(
                topPlayersLastRound = listOf(
                    PlayerCardItem(name = "Del Piero", points = 10f),
                    PlayerCardItem(name = "Inzaghi", points = 9f),
                ),
                mvps = listOf(
                    PlayerCardItem(name = "Inzaghi", points = 10f),
                    PlayerCardItem(name = "Del Piero", points = 9f),
                ),
            ),
            loadNews = { },
            loadStatistics = { },
        )
    }
}
