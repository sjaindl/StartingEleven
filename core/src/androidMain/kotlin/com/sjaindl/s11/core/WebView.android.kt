package com.sjaindl.s11.core

import android.view.ViewGroup
import android.webkit.WebView
import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView

@Composable
actual fun WebView(url: String) {
    AndroidView(
        factory = {
            WebView(it).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                )
                with(settings) {
                    javaScriptEnabled = true
                    domStorageEnabled = true
                }
                clearHistory()
            }
        }, update = {
            it.loadUrl(url)
        }
    )
}
