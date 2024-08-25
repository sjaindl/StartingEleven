package com.sjaindl.s11.core.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Shape

@Composable
fun cardShape(): Shape {
    val cornerRadius = LocalDimensions.current.cardCornerRadius
    return remember(cornerRadius) { RoundedCornerShape(cornerRadius) }
}
