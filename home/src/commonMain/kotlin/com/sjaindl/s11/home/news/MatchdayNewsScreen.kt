package com.sjaindl.s11.home.news

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sjaindl.s11.core.baseui.ErrorScreen
import com.sjaindl.s11.core.baseui.FallbackImage
import com.sjaindl.s11.core.baseui.LoadingScreen
import com.sjaindl.s11.core.theme.HvtdpTheme
import com.sjaindl.s11.core.theme.spacing
import com.sjaindl.s11.home.news.NewsState.Content
import com.sjaindl.s11.home.news.NewsState.Error
import com.sjaindl.s11.home.news.NewsState.Initial
import com.sjaindl.s11.home.news.NewsState.Loading
import com.sjaindl.s11.home.news.NewsState.NoNews
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun MatchdayNewsScreen(
    newsState: NewsState,
    loadNews: () -> Unit,
    modifier: Modifier = Modifier,
) {
    when (newsState) {
        Initial, Loading -> {
            LoadingScreen()
        }

        is Content -> {
            Column(
                modifier = modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(spacing.xl),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                FallbackImage(photoRefDownloadUrl = newsState.matchdayImageDownloadUrl)

                Text(
                    text = newsState.matchdayNews,
                )
            }
        }

        is Error -> {
            ErrorScreen(
                modifier = modifier,
                text = newsState.message,
                onButtonClick = loadNews,
            )
        }

        NoNews -> {
            // no content
        }
    }
}

@Preview
@Composable
fun MatchdayNewsScreenPreview() {
    HvtdpTheme {
        MatchdayNewsScreen(
            newsState = Content(
                generalNews = "Ich bin der erste Test-News-Eintrag in der coolen neuen Starting 11 App!",
                matchdayNews = "Ich bin ein Matchday News-Eintrag!",
                matchdayImageDownloadUrl = null,
            ),
            loadNews = { },
            modifier = Modifier
                .padding(8.dp),
        )
    }
}
