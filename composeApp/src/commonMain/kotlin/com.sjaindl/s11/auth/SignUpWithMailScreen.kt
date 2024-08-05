package com.sjaindl.s11.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.sjaindl.s11.theme.HvtdpTheme
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import startingeleven.composeapp.generated.resources.Res
import startingeleven.composeapp.generated.resources.email
import startingeleven.composeapp.generated.resources.enterName
import startingeleven.composeapp.generated.resources.enterPassword
import startingeleven.composeapp.generated.resources.signUp

@Composable
fun SignUpWithMailScreen(
    email: String,
    modifier: Modifier = Modifier,
    onProceed: (email: String, password: String, name: String) -> Unit,
) {
    var name by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }

    val colors = ButtonDefaults.buttonColors(
        containerColor = colorScheme.primary,
    )

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
                Text(text = stringResource(resource = Res.string.email))
            },
            isError = !email.isValidMail(),
        )

        OutlinedTextField(
            value = name,
            onValueChange = {
                name = it
            },
            label = {
                Text(text = stringResource(resource = Res.string.enterName))
            },
            isError = !name.isValidName(),
        )

        OutlinedTextField(
            value = password,
            label = {
                Text(text = stringResource(resource = Res.string.enterPassword))
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
                onProceed(email, password, name)
            },
            enabled = email.isValidMail() && name.isValidName() && password.isValidPassword(),
            colors = colors,
        ) {
            Text(text = stringResource(resource = Res.string.signUp))
        }
    }
}

@Preview
@Composable
fun SignUpWithMailScreenPreview() {
    HvtdpTheme {

        SignUpWithMailScreen(
            email = "demo@test.com",
            onProceed = { _, _, _ -> },
        )
    }
}
