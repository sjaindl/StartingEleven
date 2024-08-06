package com.sjaindl.s11.auth

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.PasswordCredential
import androidx.credentials.PublicKeyCredential
import androidx.credentials.exceptions.GetCredentialException
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.sjaindl.s11.BuildConfig
import com.sjaindl.s11.auth.model.AuthResponse
import io.github.aakira.napier.Napier
import kotlinx.coroutines.launch

@Composable
actual fun GoogleSignIn(onResponse: (AuthResponse) -> Unit) {
    val credentialManager = CredentialManager.create(LocalContext.current)

    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    val googleIdOption = GetSignInWithGoogleOption.Builder(serverClientId = BuildConfig.googleServerClientId)
        .build()

    val request = GetCredentialRequest.Builder()
        .addCredentialOption(googleIdOption)
        .build()

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            try {
                val result = credentialManager.getCredential(
                    context = context,
                    request = request,
                )

                when (val credential = result.credential) {
                    is PublicKeyCredential -> {
                        // Share responseJson such as a GetCredentialResponse on your server to validate and authenticate
                        val responseJson = credential.authenticationResponseJson
                        Napier.d(message = "Received PublicKeyCredential: $responseJson")
                    }

                    is PasswordCredential -> {
                        // Send ID and password to your server to validate and authenticate.
                        val username = credential.id
                        val password = credential.password
                        Napier.d(message = "Received PasswordCredential: $username / $password")
                    }

                    is CustomCredential -> {
                        Napier.d(message = "Received CustomCredential: $credential")

                        if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                            val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(data = credential.data)

                            onResponse(
                                AuthResponse.Success(account = googleIdTokenCredential.googleAccount),
                            )
                        }
                    }
                }

            } catch (exception: GetCredentialException) {
                onResponse(AuthResponse.Error(exception.message ?: exception.toString()))
            }
        }
    }
}
