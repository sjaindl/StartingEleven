package com.sjaindl.s11.privacypolicy

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.multiplatform.webview.web.WebView
import com.multiplatform.webview.web.rememberWebViewState
import com.sjaindl.s11.core.theme.HvtdpTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun PrivacyPolicyScreen() {
    val webViewState = rememberWebViewState(url = "https://starting-eleven-2019.firebaseapp.com/privacy")

    Column(
        modifier = Modifier
            .fillMaxSize(),
    ) {
        WebView(
            state = webViewState,
            modifier = Modifier
                .fillMaxSize(),
        )
    }
}

@Preview
@Composable
fun PrivacyPolicyScreenPreview() {
    HvtdpTheme {
        PrivacyPolicyScreen()
    }
}