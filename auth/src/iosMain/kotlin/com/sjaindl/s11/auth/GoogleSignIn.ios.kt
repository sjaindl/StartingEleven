package com.sjaindl.s11.auth

import androidx.compose.runtime.Composable
import androidx.compose.ui.interop.LocalUIViewController
import cocoapods.GoogleSignIn.GIDSignIn
import cocoapods.GoogleSignIn.GIDSignInResult
import com.sjaindl.s11.auth.model.GoogleAccount
import com.sjaindl.s11.auth.model.GoogleAuthResponse
import com.sjaindl.s11.auth.model.GoogleProfile
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSError

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun GoogleSignIn(onResponse: (GoogleAuthResponse) -> Unit) {
    val uiViewController = LocalUIViewController.current

    GIDSignIn.sharedInstance.signInWithPresentingViewController(uiViewController) { result, error ->
        when {
            result != null -> onResponse(GoogleAuthResponse.Success(result.toGoogleAccount))
            error != null -> onResponse(GoogleAuthResponse.Error(error.fullErrorMessage))
            else -> onResponse(GoogleAuthResponse.Cancelled)
        }
    }
}

@OptIn(ExperimentalForeignApi::class)
private val GIDSignInResult.toGoogleAccount: GoogleAccount
    get() = GoogleAccount(
        idToken = user.idToken?.tokenString.orEmpty(),
        accessToken = user.accessToken.tokenString,
        googleProfile = GoogleProfile(
            name = user.profile?.name.orEmpty(),
            familyName = user.profile?.familyName.orEmpty(),
            givenName = user.profile?.givenName.orEmpty(),
            email = user.profile?.email.orEmpty(),
            pictureUrl = user.profile?.imageURLWithDimension(100u)?.absoluteString,
        ),
    )

private val NSError.fullErrorMessage: String
    get() {
        val underlyingErrors = underlyingErrors.joinToString(", ") { it.toString() }
        val recoveryOptions = localizedRecoveryOptions?.joinToString(", ") { it.toString() }

        return listOfNotNull(
            "code: $code",
            domain?.let { "domain: $domain" },
            "description: $localizedDescription",
            localizedFailureReason?.let { "reason: $localizedFailureReason" },
            localizedRecoverySuggestion?.let { "suggestion: $localizedRecoverySuggestion" },
            "underlyingErrors: $underlyingErrors",
            "recoveryOptions: $recoveryOptions".takeIf { recoveryOptions != null },
        ).joinToString("\n")
    }
