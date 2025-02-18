package com.sjaindl.s11.auth

import androidx.compose.runtime.Composable
import androidx.compose.ui.interop.LocalUIViewController
import cocoapods.FBSDKLoginKit.FBSDKLoginConfiguration
import cocoapods.FBSDKLoginKit.FBSDKLoginManager
import cocoapods.FBSDKLoginKit.FBSDKLoginTrackingLimited
import com.sjaindl.s11.auth.model.FacebookAuthResponse
import com.sjaindl.s11.crypto.CryptoUtils
import kotlinx.cinterop.ExperimentalForeignApi
import kotlin.random.Random

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun FacebookSignIn(onResponse: (FacebookAuthResponse) -> Unit) {
    val uiViewController = LocalUIViewController.current

    val loginManager = FBSDKLoginManager()
    val randomString = generateRandomString(length = 32)

    val config = FBSDKLoginConfiguration(
        permissions = listOf("public_profile", "email"),
        tracking = FBSDKLoginTrackingLimited,
        nonce = CryptoUtils.shared().sha256Hash(inputString = randomString),
    )

    loginManager.logInFromViewController(uiViewController, config) { result, error ->
        val token = result?.authenticationToken()

        when {
            token != null -> onResponse(FacebookAuthResponse.Success(token.tokenString(), randomString))
            error != null -> onResponse(FacebookAuthResponse.Error(error.toString()))
            else -> onResponse(FacebookAuthResponse.Cancelled)
        }
    }
}

private fun generateRandomString(length: Int): String {
    return buildString {
        repeat(length) {
            append(Random.nextInt(0, Char.MAX_VALUE.code).toChar())
        }
    }
}
