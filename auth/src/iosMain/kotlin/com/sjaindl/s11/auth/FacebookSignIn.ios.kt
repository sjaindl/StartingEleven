package com.sjaindl.s11.auth

import androidx.compose.runtime.Composable
import androidx.compose.ui.interop.LocalUIViewController
import cocoapods.FBSDKLoginKit.FBSDKLoginConfiguration
import cocoapods.FBSDKLoginKit.FBSDKLoginManager
import cocoapods.FBSDKLoginKit.FBSDKLoginTrackingEnabled
import com.sjaindl.s11.auth.model.FacebookAuthResponse
import kotlinx.cinterop.ExperimentalForeignApi

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun FacebookSignIn(onResponse: (FacebookAuthResponse) -> Unit) {
    val uiViewController = LocalUIViewController.current

    val loginManager = FBSDKLoginManager()
    loginManager.logInWithPermissions(listOf("public_profile"), uiViewController) { result, error ->
        val token = result?.token()
        when {
            token != null -> onResponse(FacebookAuthResponse.Success(token.tokenString()))
            error != null -> onResponse(FacebookAuthResponse.Error(error.toString()))
            else -> onResponse(FacebookAuthResponse.Cancelled)
        }
    }
}

// TODO: Switch to this implementation as soon as https://github.com/facebook/facebook-ios-sdk/issues/2455 is fixed
@OptIn(ExperimentalForeignApi::class)
@Composable
fun FacebookLimitedSignIn(onResponse: (FacebookAuthResponse) -> Unit) {
    val uiViewController = LocalUIViewController.current

    val loginManager = FBSDKLoginManager()
    val config = FBSDKLoginConfiguration(
        permissions = listOf("email", "public_profile"),
        tracking = FBSDKLoginTrackingEnabled,
    )

    loginManager.logInFromViewController(uiViewController, config) { result, error ->
        val token = result?.token()

        when {
            token != null -> onResponse(FacebookAuthResponse.Success(token.tokenString()))
            error != null -> onResponse(FacebookAuthResponse.Error(error.toString()))
            else -> onResponse(FacebookAuthResponse.Cancelled)
        }
    }
}
