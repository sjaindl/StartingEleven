package com.sjaindl.s11.permission

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sjaindl.s11.core.theme.HvtdpTheme
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import startingeleven.composeapp.generated.resources.Res
import startingeleven.composeapp.generated.resources.push_notification_description
import startingeleven.composeapp.generated.resources.push_notification_grant_button
import startingeleven.composeapp.generated.resources.push_notification_not_now_button
import startingeleven.composeapp.generated.resources.push_notification_title

@Composable
fun NotificationPermissionExplanation(
    onGrantPermissionClick: () -> Unit,
    onDismissClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(resource = Res.string.push_notification_title),
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = stringResource(resource = Res.string.push_notification_description),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(horizontal = 8.dp),
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = onGrantPermissionClick,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(text = stringResource(resource = Res.string.push_notification_grant_button))
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    OutlinedButton(
                        onClick = onDismissClick,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(text = stringResource(resource = Res.string.push_notification_not_now_button))
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun NotificationPermissionExplanationPreview() {
    HvtdpTheme {
        NotificationPermissionExplanation(
            onGrantPermissionClick = { },
            onDismissClick = { },
        )
    }
}
