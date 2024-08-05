package com.sjaindl.s11.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sjaindl.s11.theme.HvtdpTheme
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import startingeleven.composeapp.generated.resources.Res
import startingeleven.composeapp.generated.resources.enterMail
import startingeleven.composeapp.generated.resources.signIn
import startingeleven.composeapp.generated.resources.signUp

@Composable
fun SignInWithMailHomeScreen(
    signIn: (email: String) -> Unit,
    signUp: (email: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var email by remember {
        mutableStateOf(value = "")
    }

    val colors = ButtonDefaults.buttonColors(
        containerColor = colorScheme.primary,
    )

    Column(
        modifier = modifier
            .padding(all = 32.dp),
        horizontalAlignment = Alignment.End
    ) {

        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
            },
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text(text = stringResource(resource = Res.string.enterMail))
            },
            isError = !email.isValidMail(),
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(32.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Spacer(modifier = Modifier.weight(1f))

            FilledTonalButton(
                onClick = {
                    signIn(email)
                },
                enabled = email.isValidMail(),
                colors = colors,
            ) {
                Text(text = stringResource(resource = Res.string.signIn))
            }

            Spacer(modifier = Modifier.weight(1f))

            FilledTonalButton(
                onClick = {
                    signUp(email)
                },
                enabled = email.isValidMail(),
                colors = colors,
            ) {
                Text(text = stringResource(resource = Res.string.signUp))
            }

            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Preview
@Composable
fun SignInWithMailLandingScreenPreview() {
    HvtdpTheme {
        SignInWithMailHomeScreen(
            signIn = { _ -> },
            signUp = { _ -> },
        )
    }
}
