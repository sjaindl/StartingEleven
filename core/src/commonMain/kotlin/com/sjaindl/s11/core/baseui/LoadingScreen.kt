package com.sjaindl.s11.core.baseui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sjaindl.s11.core.theme.HvtdpTheme
import com.sjaindl.s11.core.theme.spacing
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun LoadingScreen(
    loadingInfo: String? = null,
    paddingValues: PaddingValues = PaddingValues(horizontal = spacing.md),
    modifier: Modifier = Modifier
        .padding(paddingValues = paddingValues)
        .fillMaxSize()
        .wrapContentSize(),
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        CircularProgressIndicator()

        loadingInfo?.let {
            Text(
                text = it,
                modifier = Modifier.padding(top = 8.dp),
            )
        }
    }
}

@Preview
@Composable
private fun LoadingScreenPreview() {
    HvtdpTheme {
        LoadingScreen()
    }
}
