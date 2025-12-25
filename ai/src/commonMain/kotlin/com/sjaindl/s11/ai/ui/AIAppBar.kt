package com.sjaindl.s11.ai.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.sjaindl.s11.core.theme.HvtdpTheme
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import startingeleven.ai.generated.resources.Res
import startingeleven.ai.generated.resources.back

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AIAppBar(
    onResetChat: () -> Unit,
    modifier: Modifier = Modifier,
    navigateUp: () -> Unit = { },
) {
    TopAppBar(
        title = {
            Text(
                text = "HV TDP Assistant",
                color = colorScheme.onPrimary,
            )
        },
        modifier = modifier,
        navigationIcon = {
            IconButton(
                onClick = {
                    navigateUp()
                }
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(Res.string.back),
                    tint = colorScheme.onPrimary,
                )
            }
        },
        actions = {
            IconButton(
                onClick = onResetChat,
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Reset",
                    tint = colorScheme.onPrimary,
                )
            }
        },
        colors = topAppBarColors(
            containerColor = colorScheme.primary,
        ),
    )
}

@Preview
@Composable
fun ChatBotAppBarPreview() {
    HvtdpTheme {
        AIAppBar(
            onResetChat = { },
            navigateUp = { },
        )
    }
}
