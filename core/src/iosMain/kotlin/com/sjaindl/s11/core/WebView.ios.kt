package com.sjaindl.s11.core

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.interop.UIKitView
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSURL
import platform.Foundation.NSURLRequest
import platform.WebKit.WKWebView

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun WebView(url: String) {
    UIKitView(
        factory = {
            WKWebView().apply {
                loadRequest(NSURLRequest(uRL = NSURL(string = url)))
            }
        },
        modifier = Modifier.fillMaxSize()
    )
}
