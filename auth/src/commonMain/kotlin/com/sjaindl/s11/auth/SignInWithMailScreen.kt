package com.sjaindl.s11.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sjaindl.s11.auth.EmailAuthenticationState.EmailSignInSuccess
import com.sjaindl.s11.auth.EmailAuthenticationState.EmailSignUpSuccess
import com.sjaindl.s11.auth.EmailAuthenticationState.Error
import com.sjaindl.s11.auth.EmailAuthenticationState.Initial
import com.sjaindl.s11.auth.EmailAuthenticationState.Loading
import com.sjaindl.s11.core.baseui.ErrorScreen
import com.sjaindl.s11.core.baseui.LoadingScreen
import com.sjaindl.s11.core.theme.HvtdpTheme
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import startingeleven.auth.generated.resources.Res
import startingeleven.auth.generated.resources.email
import startingeleven.auth.generated.resources.enterPassword
import startingeleven.auth.generated.resources.signIn

@Composable
fun SignInWithMailScreen(
    email: String,
    modifier: Modifier = Modifier,
    onSuccess: () -> Unit,
) {
    val authenticationViewModel = viewModel {
        EmailAuthenticationViewModel()
    }

    val authenticationState by authenticationViewModel.authenticationState.collectAsState()

    var password by remember {
        mutableStateOf("")
    }

    val colors = ButtonDefaults.buttonColors(
        containerColor = colorScheme.primary,
    )

    when (val state = authenticationState) {
        Initial -> {
            Column(
                modifier = modifier
                    .padding(all = 32.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.End,
            ) {

                OutlinedTextField(
                    value = email,
                    onValueChange = { },
                    enabled = false,
                    label = {
                        Text(text = stringResource(Res.string.email))
                    },
                    isError = !email.isValidMail()
                )

                OutlinedTextField(
                    value = password,
                    label = {
                        Text(text = stringResource(Res.string.enterPassword))
                    },
                    onValueChange = {
                        password = it
                    },
                    isError = !password.isValidPassword(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    visualTransformation = PasswordVisualTransformation(),
                )

                FilledTonalButton(
                    onClick = {
                        authenticationViewModel.signInWithMail(
                            email = email,
                            password = password,
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp),
                    enabled = email.isValidMail() && password.isValidPassword(),
                    colors = colors,
                ) {
                    Text(text = stringResource(resource = Res.string.signIn))
                }
            }
        }

        Loading -> {
            LoadingScreen()
        }

        is Error -> {
            ErrorScreen(
                text = state.message,
                onButtonClick = {
                    authenticationViewModel.resetState()
                    password = ""
                },
            )
        }

        EmailSignInSuccess, EmailSignUpSuccess -> {
            LaunchedEffect(authenticationState) {
                authenticationViewModel.resetState()
                onSuccess()
            }
        }
    }
}

@Preview
@Composable
fun SignInWithMailScreenPreview() {
    HvtdpTheme {
        SignInWithMailScreen(
            email = "demo@test.com",
            onSuccess = { },
        )
    }
}
