package com.sjaindl.s11.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Facebook
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import com.sjaindl.s11.auth.SocialAuthenticationState.Error
import com.sjaindl.s11.auth.SocialAuthenticationState.FacebookSignInSuccess
import com.sjaindl.s11.auth.SocialAuthenticationState.GoogleSignInSuccess
import com.sjaindl.s11.auth.SocialAuthenticationState.Initial
import com.sjaindl.s11.auth.SocialAuthenticationState.Loading
import com.sjaindl.s11.auth.model.FacebookAuthResponse
import com.sjaindl.s11.auth.model.GoogleAuthResponse
import com.sjaindl.s11.core.BackHandler
import com.sjaindl.s11.core.baseui.ErrorScreen
import com.sjaindl.s11.core.baseui.LoadingScreen
import com.sjaindl.s11.core.theme.HvtdpTheme
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import startingeleven.auth.generated.resources.Res
import startingeleven.auth.generated.resources.ic_google
import startingeleven.auth.generated.resources.signInCancel
import startingeleven.auth.generated.resources.signInWithEmail
import startingeleven.auth.generated.resources.signInWithFacebook
import startingeleven.auth.generated.resources.signInWithGoogle

@Composable
fun SignInChooserScreen(
    authenticationState: SocialAuthenticationState,
    onSignInWithMail: () -> Unit,
    onRetry: () -> Unit,
    onSuccess: () -> Unit,
    handleGoogleSignIn: (GoogleAuthResponse, String) -> Unit,
    handleFacebookSignIn: (FacebookAuthResponse, String) -> Unit,
    resetState: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val cancelMessage = stringResource(resource = Res.string.signInCancel)

    var signInWithGoogle by remember {
        mutableStateOf(value = false)
    }

    var signInWithFacebook by remember {
        mutableStateOf(value = false)
    }

    BackHandler(
        isEnabled = true,
        onBack = { },
    )

    if (signInWithGoogle) {
        GoogleSignIn {
            signInWithGoogle = false

            handleGoogleSignIn(it, cancelMessage)
        }
    }

    if (signInWithFacebook) {
        signInWithFacebook = false

        FacebookSignIn {
            handleFacebookSignIn(it, cancelMessage)
        }
    }

    when (val state = authenticationState) {
        Initial -> {
            SignInChooserScreenContent(
                signInWithGoogle = {
                    signInWithGoogle = true
                },
                signInWithFacebook = {
                    signInWithFacebook = true
                },
                signInWithMail = onSignInWithMail,
                modifier = modifier,
            )
        }

        Loading -> {
            LoadingScreen()
        }

        is Error -> {
            ErrorScreen(
                text = state.message,
                onButtonClick = {
                    resetState()
                    onRetry()
                },
            )
        }

        FacebookSignInSuccess, GoogleSignInSuccess -> {
            LaunchedEffect(authenticationState) {
                resetState()
                onSuccess()
            }
        }
    }
}


@Composable
fun SignInChooserScreenContent(
    signInWithGoogle: () -> Unit,
    signInWithFacebook: () -> Unit,
    signInWithMail: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .padding(horizontal = 32.dp)
            .padding(bottom = 32.dp),
        verticalArrangement = Arrangement.Bottom,
    ) {
        SignInProviderButton(
            containerColor = Color(red = 219, green = 68, blue = 55),
            text = stringResource(resource = Res.string.signInWithGoogle),
            onClick = signInWithGoogle,
        ) {
            Image(
                painter = painterResource(Res.drawable.ic_google),
                contentDescription = stringResource(resource = Res.string.signInWithGoogle),
                modifier = Modifier
                    .size(20.dp),
                colorFilter = ColorFilter.tint(colorScheme.onPrimary),
            )
        }

        SignInProviderButton(
            containerColor = Color(red = 66, green = 103, blue = 178),
            text = stringResource(resource = Res.string.signInWithFacebook),
            onClick = signInWithFacebook,
        ) {
            Image(
                imageVector = Icons.Filled.Facebook,
                contentDescription = stringResource(resource = Res.string.signInWithFacebook),
                modifier = Modifier.size(24.dp),
                colorFilter = ColorFilter.tint(colorScheme.onPrimary),
            )
        }

        SignInProviderButton(
            containerColor = colorScheme.primary,
            text = stringResource(resource = Res.string.signInWithEmail),
            onClick = signInWithMail,
        ) {
            Image(
                imageVector = Icons.Filled.Email,
                contentDescription = stringResource(resource = Res.string.signInWithEmail),
                modifier = Modifier.size(24.dp),
                colorFilter = ColorFilter.tint(colorScheme.onPrimary),
            )
        }
    }
}

@Composable
private fun SignInProviderButton(
    containerColor: Color,
    text: String,
    onClick: () -> Unit,
    image: @Composable() (() -> Unit)
) {
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
        ),
        onClick = onClick,
    ) {
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            image()
            Spacer(modifier = Modifier.weight(0.1f))
            Text(text = text)
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Preview
@Composable
fun SignInChooserScreenPreview() {
    HvtdpTheme {
        SignInChooserScreen(
            onSignInWithMail = { },
            onRetry = { },
            onSuccess = { },
            authenticationState = SocialAuthenticationState.Initial,
            handleGoogleSignIn = { _, _ -> },
            handleFacebookSignIn = { _, _ -> },
            resetState = { },
        )
    }
}
