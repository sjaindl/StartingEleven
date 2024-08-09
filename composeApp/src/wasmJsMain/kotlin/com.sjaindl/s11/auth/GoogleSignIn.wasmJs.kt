package com.sjaindl.s11.auth

import androidx.compose.runtime.Composable
import com.sjaindl.s11.auth.model.GoogleAuthResponse

@Composable
actual fun GoogleSignIn(onResponse: (GoogleAuthResponse) -> Unit) {
}