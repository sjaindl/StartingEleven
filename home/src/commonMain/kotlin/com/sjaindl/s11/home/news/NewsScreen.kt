package com.sjaindl.s11.home.news

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sjaindl.s11.core.baseui.ErrorScreen
import com.sjaindl.s11.core.baseui.LoadingScreen
import com.sjaindl.s11.core.theme.HvtdpTheme
import com.sjaindl.s11.home.news.NewsState.Content
import com.sjaindl.s11.home.news.NewsState.Error
import com.sjaindl.s11.home.news.NewsState.Initial
import com.sjaindl.s11.home.news.NewsState.Loading
import com.sjaindl.s11.home.news.NewsState.NoNews
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun NewsScreen(
    newsState: NewsState,
    loadNews: () -> Unit,
    modifier: Modifier = Modifier,
) {
    when (newsState) {
        Initial, Loading -> {
            LoadingScreen()
        }

        is Content -> {
            Text(
                text = newsState.text,
                modifier = modifier,
            )
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
fun NewsScreenPreview() {
    HvtdpTheme {
        NewsScreen(
            newsState = Content(
                text = "Ich bin der erste Test-News-Eintrag in der coolen neuen Starting 11 App!"
            ),
            loadNews = { },
            modifier = Modifier
                .padding(8.dp),
        )
    }
}
