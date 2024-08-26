package com.sjaindl.s11.core.model

import androidx.compose.runtime.Composable

data class OverFlowMenuItem(
    val text: String,
    val onClick: () -> Unit,
    val icon:  @Composable (() -> Unit)? = null,
)
