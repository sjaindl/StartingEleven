package com.sjaindl.s11.team.bet

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.sjaindl.s11.core.theme.HvtdpTheme
import com.sjaindl.s11.core.theme.spacing
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import startingeleven.team.generated.resources.Res
import startingeleven.team.generated.resources.betGame
import startingeleven.team.generated.resources.submitBet
import startingeleven.team.generated.resources.yourBet

@Composable
fun BetUI(
    homeTeam: String,
    awayTeam: String,
    homeBet: Int,
    awayBet: Int,
    enabled: Boolean,
    onHomeBetChanged: (Int) -> Unit,
    onAwayBetChanged: (Int) -> Unit,
    onSubmitBet: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .padding(horizontal = spacing.md),
        verticalArrangement = Arrangement.spacedBy(space = spacing.s),
    ) {
        Text(
            text = stringResource(resource = Res.string.betGame),
            style = typography.bodyLarge,
            fontWeight = FontWeight.Bold,
        )

        Text(text = stringResource(resource = Res.string.yourBet))

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(space = spacing.xl),
            verticalAlignment = Alignment.Top,
        ) {
            TextField(
                value = homeBet.toString(),
                onValueChange = { newScore ->
                    newScore.toIntOrNull()?.let {
                        onHomeBetChanged(it)
                    }
                },
                modifier = Modifier
                    .weight(0.5f),
                enabled = enabled,
                label = {
                    Text(text = homeTeam)
                },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                ),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                )
            )

            TextField(
                value = awayBet.toString(),
                onValueChange = { awayScore ->
                    awayScore.toIntOrNull()?.let {
                        onAwayBetChanged(it)
                    }
                },
                modifier = Modifier
                    .weight(0.5f),
                enabled = enabled,
                label = {
                    Text(text = awayTeam)
                },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                ),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                )
            )
        }

        OutlinedButton(
            onClick = onSubmitBet,
            modifier = Modifier
                .fillMaxWidth(),
            enabled = enabled,
        ) {
            Text(text = stringResource(resource = Res.string.submitBet))
        }
    }
}

@Preview
@Composable
fun BetUIPreview() {
    HvtdpTheme {
        BetUI(
            homeTeam = "HV TDP Stainz",
            awayTeam = "SV Geistthal",
            homeBet = 1,
            awayBet = 0,
            enabled = true,
            onHomeBetChanged = { },
            onAwayBetChanged = { },
            onSubmitBet = { },
            modifier = Modifier
                .padding(8.dp),
        )
    }
}
