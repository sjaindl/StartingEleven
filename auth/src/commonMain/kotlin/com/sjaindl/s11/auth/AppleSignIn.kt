package com.sjaindl.s11.auth

import androidx.compose.runtime.Composable
import com.sjaindl.s11.auth.model.AppleAuthResponse

@Composable
expect fun AppleSignIn(onResponse: (AppleAuthResponse) -> Unit)
