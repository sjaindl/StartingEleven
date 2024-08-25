package com.sjaindl.s11.core.baseui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.QuestionAnswer
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.sjaindl.s11.core.navigation.Route
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
import startingeleven.core.generated.resources.faqs
import startingeleven.core.generated.resources.signOut

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun S11AppBar(
    userIsSignedIn: Boolean,
    currentRoute: Route?,
    canNavigateBack: Boolean,
    modifier: Modifier = Modifier,
    navigateUp: () -> Unit = { },
    navigateHome: () -> Unit = { },
    navigateToFaqs: () -> Unit = { },
    onClickProfile: () -> Unit = { },
    customActionIcon: ImageVector? = null,
    onCustomAction: () -> Unit = { },
) {
    val coroutineScope = rememberCoroutineScope()

    TopAppBar(
        title = {
            Text(
                text = stringResource(resource = currentRoute?.let {
                    Route.stringResForRoute(it)
                } ?: Res.string.appName),
                color = colorScheme.onPrimary,
            )
        },
        modifier = modifier,
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
            if (currentRoute?.isTopLevelRoute == true) {
                IconButton(
                    onClick = onClickProfile,
                ) {
                    Image(
                        imageVector = Icons.Filled.Person,
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(color = colorScheme.onPrimary),
                    )
                }
            }

            if (customActionIcon != null) {
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

            if (currentRoute?.isTopLevelRoute == true) {
                OverflowMenu(
                    menuItems = buildList {
                        add(
                            element = MenuItem(
                                text = stringResource(resource = Res.string.faqs),
                                onClick = navigateToFaqs,
                                icon = {
                                    Image(
                                        imageVector = Icons.Default.QuestionAnswer,
                                        contentDescription = stringResource(resource = Res.string.faqs),
                                        modifier = Modifier
                                            .padding(spacing.xxs),
                                    )
                                },
                            )
                        )

                        if (userIsSignedIn) {
                            add(
                                element = MenuItem(
                                    text = stringResource(resource = Res.string.signOut),
                                    onClick = {
                                        coroutineScope.launch {
                                            Firebase.auth.signOut()
                                            navigateHome()
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
            containerColor = colorScheme.primary,
        ),
    )
}

@Preview
@Composable
fun S11AppBarPreview() {
    HvtdpTheme {
        S11AppBar(
            userIsSignedIn = true,
            currentRoute = Route.Home,
            canNavigateBack = true,
            navigateUp = { },
            onClickProfile = { },
        )
    }
}
