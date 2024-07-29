package com.sjaindl.baseui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import startingeleven.composeapp.generated.resources.Res
import startingeleven.composeapp.generated.resources.appName
import startingeleven.composeapp.generated.resources.back

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun S11AppBar(
    title: String,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit = { },
    showProfile: Boolean = false,
    onClickProfile: () -> Unit = { },
    customActionIcon: ImageVector? = null,
    onCustomAction: () -> Unit = { },
) {
    var showAnimationScreen by remember {
        mutableStateOf(value = false)
    }

    TopAppBar(
        title = {
            Text(
                text = title,
                modifier = Modifier.combinedClickable(
                    enabled = true,
                    onDoubleClick = {
                        showAnimationScreen = true
                    },
                    onClick = { }
                )
            )
        },
        modifier = Modifier,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = { navigateUp() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(Res.string.back),
                    )
                }
            }
        },
        actions = {
            if (showProfile) {
                /*
                TODO
                UserIconContainer(
                    onClickedProfile = onClickProfile,
                )

                 */
            } else if (customActionIcon != null) {
                Image(
                    imageVector = customActionIcon,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .clickable {
                            onCustomAction()
                        },
                )
            }
        },
        colors = topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
        ),
    )
}

@Preview
@Composable
fun TCAppBarPreview() {
    MaterialTheme {
        S11AppBar(
            title = stringResource(Res.string.appName),
            canNavigateBack = true,
            navigateUp = { },
            showProfile = true,
            onClickProfile = { },
        )
    }
}
