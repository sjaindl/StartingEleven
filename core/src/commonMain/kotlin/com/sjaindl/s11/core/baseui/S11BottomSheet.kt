package com.sjaindl.s11.core.baseui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.contentColorFor
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.sjaindl.s11.core.theme.HvtdpTheme

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun S11BottomSheet(
    modifier: Modifier = Modifier,
    onDismissBottomSheet: () -> Unit,
    containerColor: Color = BottomSheetDefaults.ContainerColor,
    contentColor: Color = contentColorFor(containerColor),
    scrimColor: Color = BottomSheetDefaults.ScrimColor,
    content: @Composable ColumnScope.() -> Unit,
) {
    ModalBottomSheet(
        onDismissRequest = onDismissBottomSheet,
        modifier = modifier,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = containerColor,
        scrimColor = scrimColor,
        contentColor = contentColor,
    ) {
        HvtdpTheme {
            Column {
                content()

                Spacer(
                    modifier = Modifier.windowInsetsBottomHeight(WindowInsets.systemBars),
                )
            }
        }
    }
}
