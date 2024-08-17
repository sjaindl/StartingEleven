package com.sjaindl.s11.auth

import androidx.compose.runtime.Composable
import com.sjaindl.s11.auth.model.FacebookAuthResponse

@Composable
expect fun FacebookSignIn(onResponse: (FacebookAuthResponse) -> Unit)
