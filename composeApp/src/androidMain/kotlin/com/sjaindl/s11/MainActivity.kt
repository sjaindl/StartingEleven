package com.sjaindl.s11

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sjaindl.s11.core.theme.HvtdpTheme
import com.sjaindl.s11.permission.NotificationPermissionExplanation

class MainActivity : ComponentActivity() {

    private val tag = "MainActivity"

    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        Log.d(tag, "Notification permission granted: $isGranted")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val appViewModel = viewModel {
                AppViewModel()
            }

            var hidePermissionDialog by rememberSaveable {
                mutableStateOf(value = false)
            }

            val isAuthenticated by appViewModel.isAuthenticated.collectAsStateWithLifecycle()
            val askForNotificationPermission = NotificationManagerCompat.from(this).areNotificationsEnabled().not()

            val isPermissionNeeded = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU

            if (isAuthenticated && isPermissionNeeded && askForNotificationPermission && !hidePermissionDialog) {
                HvtdpTheme {
                    NotificationPermissionExplanation(
                        onGrantPermissionClick = {
                            permissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
                            hidePermissionDialog = true
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
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}
