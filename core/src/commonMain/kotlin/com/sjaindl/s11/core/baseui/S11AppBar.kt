package com.sjaindl.s11.core.baseui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.sjaindl.s11.core.theme.HvtdpTheme
import com.sjaindl.s11.core.theme.spacing
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import startingeleven.core.generated.resources.Res
import startingeleven.core.generated.resources.appName
import startingeleven.core.generated.resources.back
import startingeleven.core.generated.resources.signOut

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun S11AppBar(
    title: String,
    userIsSignedIn: Boolean,
    isTopLevelAppBar: Boolean,
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

    val coroutineScope = rememberCoroutineScope()

    TopAppBar(
        title = {
            Text(
                text = title,
                modifier = Modifier.combinedClickable(
                    enabled = true,
                    onDoubleClick = {
                        showAnimationScreen = true
                    },
                    onClick = {

                    }
                ),
                color = colorScheme.onPrimary,
            )
        },
        modifier = Modifier,
        navigationIcon = {
            if (canNavigateBack) {
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
            }
        },
        actions = {
            if (showProfile) {
                IconButton(
                    onClick = onClickProfile,
                ) {
                    Image(
                        imageVector = Icons.Filled.Person,
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(color = colorScheme.onPrimary),
                    )
                }
            } else if (customActionIcon != null) {
                IconButton(
                    onClick = onCustomAction,
                ) {
                    Image(
                        imageVector = customActionIcon,
                        contentDescription = null,
                        modifier = Modifier
                            .padding(end = 8.dp),
                    )
                }
            }

            if (isTopLevelAppBar) {
                OverflowMenu(
                    menuItems = buildList {
                        if (userIsSignedIn) {
                            add(
                                element = MenuItem(
                                    text = stringResource(resource = Res.string.signOut),
                                    onClick = {
                                        coroutineScope.launch {
                                            Firebase.auth.signOut()
                                        }
                                    },
                                    icon = {
                                        Image(
                                            imageVector = Icons.AutoMirrored.Filled.Logout,
                                            contentDescription = stringResource(resource = Res.string.signOut),
                                            modifier = Modifier
                                                .padding(spacing.xxs),
                                        )
                                    },
                                )
                            )
                        }
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
fun S11AppBarPreview() {
    HvtdpTheme {
        S11AppBar(
            title = stringResource(Res.string.appName),
            userIsSignedIn = true,
            isTopLevelAppBar = true,
            canNavigateBack = true,
            navigateUp = { },
            showProfile = true,
            onClickProfile = { },
        )
    }
}
