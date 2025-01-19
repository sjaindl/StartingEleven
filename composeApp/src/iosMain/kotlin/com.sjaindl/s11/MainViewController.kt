package com.sjaindl.s11

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.window.ComposeUIViewController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sjaindl.s11.core.theme.HvtdpTheme
import com.sjaindl.s11.permission.NotificationPermissionExplanation
import io.github.aakira.napier.Napier
import platform.UserNotifications.UNAuthorizationOptionAlert
import platform.UserNotifications.UNAuthorizationOptionBadge
import platform.UserNotifications.UNAuthorizationOptionSound
import platform.UserNotifications.UNAuthorizationOptions
import platform.UserNotifications.UNAuthorizationStatusAuthorized
import platform.UserNotifications.UNUserNotificationCenter

fun MainViewController() = ComposeUIViewController {

    val appViewModel = viewModel {
        AppViewModel()
    }

    var hidePermissionDialog by rememberSaveable {
        mutableStateOf(value = false)
    }

    var askForNotificationPermission by rememberSaveable {
        mutableStateOf(value = false)
    }

    val notificationCenter = UNUserNotificationCenter.currentNotificationCenter()
    notificationCenter.getNotificationSettingsWithCompletionHandler { settings ->
        val authorized = settings?.authorizationStatus == UNAuthorizationStatusAuthorized
        askForNotificationPermission = authorized.not()
    }

    val isAuthenticated by appViewModel.isAuthenticated.collectAsState()

    if (isAuthenticated && askForNotificationPermission && !hidePermissionDialog) {
        HvtdpTheme {
            NotificationPermissionExplanation(
                onGrantPermissionClick = {
                    val authOptions: UNAuthorizationOptions = UNAuthorizationOptionAlert + UNAuthorizationOptionBadge + UNAuthorizationOptionSound
                    notificationCenter.requestAuthorizationWithOptions(
                        options = authOptions,
                        completionHandler = { granted, error ->
                            Napier.d("Push notification permission granted: $granted")
                            error?.let {
                                Napier.d("Push notification error: $it")
                            }
                            hidePermissionDialog = true
                        }
                    )


                },
                onDismissClick = {
                    hidePermissionDialog = true
                }
            )
        }
    } else {
        App()
    }
}
