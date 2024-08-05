package com.sjaindl.s11.core

import androidx.compose.runtime.Composable
import androidx.activity.compose.BackHandler as AndroidBackHandler

@Composable
actual fun BackHandler(isEnabled: Boolean, onBack: () -> Unit) {
    AndroidBackHandler(enabled = isEnabled) {
        onBack()
    }
}
