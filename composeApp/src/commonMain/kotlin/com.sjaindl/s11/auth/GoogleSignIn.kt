package com.sjaindl.s11.auth

import androidx.compose.runtime.Composable
import com.sjaindl.s11.auth.model.AuthResponse

@Composable
expect fun GoogleSignIn(
    onResponse: (AuthResponse) -> Unit,
)
