package com.sjaindl.s11.core.baseui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.sjaindl.s11.core.theme.HvtdpTheme
import com.sjaindl.s11.core.theme.spacing
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun LoadingScreen(
    paddingValues: PaddingValues = PaddingValues(horizontal = spacing.md),
    modifier: Modifier = Modifier
        .padding(paddingValues = paddingValues)
        .fillMaxSize()
        .wrapContentSize(),
) {
    CircularProgressIndicator(
        modifier = modifier,
    )
}

@Preview
@Composable
private fun LoadingScreenPreview() {
    HvtdpTheme {
        LoadingScreen()
    }
}
