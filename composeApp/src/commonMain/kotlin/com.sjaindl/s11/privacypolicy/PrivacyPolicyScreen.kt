package com.sjaindl.s11.privacypolicy

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.sjaindl.s11.core.WebView
import com.sjaindl.s11.core.theme.HvtdpTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun PrivacyPolicyScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize(),
    ) {
        WebView(
            url = "https://starting-eleven-2019.firebaseapp.com/privacy",
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
