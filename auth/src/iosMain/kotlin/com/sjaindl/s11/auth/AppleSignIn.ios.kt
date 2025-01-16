package com.sjaindl.s11.auth

import androidx.compose.runtime.Composable
import com.sjaindl.s11.applesignin.AppleSignInManager
import com.sjaindl.s11.auth.model.AppleAuthResponse
import kotlinx.cinterop.ExperimentalForeignApi

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun AppleSignIn(onResponse: (AppleAuthResponse) -> Unit) {
    AppleSignInManager.shared().signIn(
        onSuccess = { identityToken, nonce ->
            onResponse(AppleAuthResponse.Success(identityToken, nonce))
        },
        onFailure = {
            onResponse(AppleAuthResponse.Error(it))
        }
    )
}
