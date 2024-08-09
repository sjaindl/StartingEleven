package com.sjaindl.s11.auth

import androidx.activity.compose.LocalActivityResultRegistryOwner
import androidx.compose.runtime.Composable
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.sjaindl.s11.auth.model.FacebookAuthResponse
import io.github.aakira.napier.Napier

private val callbackManager by lazy {
    CallbackManager.Factory.create()
}

@Composable
actual fun FacebookSignIn(onResponse: (FacebookAuthResponse) -> Unit) {
    val activityResultRegistryOwner = LocalActivityResultRegistryOwner.current

    LoginManager.getInstance().registerCallback(
        callbackManager = callbackManager,
        callback = object : FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult) {
                Napier.d(message = "Facebook onSuccess: $result")
                onResponse(FacebookAuthResponse.Success(result.accessToken.token))
            }

            override fun onCancel() {
                Napier.d(message = "Facebook onCancel")
                onResponse(FacebookAuthResponse.Cancelled)
            }

            override fun onError(error: FacebookException) {
                Napier.e(message = "Facebook onError", throwable = error)
                onResponse(FacebookAuthResponse.Error(error.localizedMessage ?: error.toString()))
            }
        },
    )

    if (activityResultRegistryOwner != null) {
        LoginManager.getInstance().logInWithReadPermissions(
            activityResultRegistryOwner = activityResultRegistryOwner,
            callbackManager = callbackManager,
            permissions = listOf("email", "public_profile"),
        )
    } else {
        onResponse(FacebookAuthResponse.Error("ActivityResultRegistryOwner is null"))
    }
}
