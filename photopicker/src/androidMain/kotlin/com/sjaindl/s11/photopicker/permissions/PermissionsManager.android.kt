package com.sjaindl.s11.photopicker.permissions

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import kotlinx.coroutines.launch

@Composable
actual fun createPermissionsManager(callback: PermissionCallback): PermissionsManager {
    return remember {
        PermissionsManager(callback = callback)
    }
}

actual class PermissionsManager actual constructor(private val callback: PermissionCallback) : PermissionHandler {

    @OptIn(ExperimentalPermissionsApi::class)
    @Composable
    override fun askForPermission(permission: PermissionType) {
        val lifecycleOwner = LocalLifecycleOwner.current

        when (permission) {
            PermissionType.CAMERA -> {
                val cameraPermissionState = rememberPermissionState(permission = Manifest.permission.CAMERA)

                LaunchedEffect(cameraPermissionState) {
                    val permissionResult = cameraPermissionState.status
                    if (!permissionResult.isGranted) {
                        if (permissionResult.shouldShowRationale) {
                            callback.onPermissionStatus(
                                permissionType = permission,
                                status = PermissionStatus.SHOW_RATIONAL,
                            )
                        } else {
                            lifecycleOwner.lifecycleScope.launch {
                                cameraPermissionState.launchPermissionRequest()
                            }
                        }
                    } else {
                        callback.onPermissionStatus(
                            permissionType = permission,
                            status = PermissionStatus.GRANTED,
                        )
                    }
                }
            }

            PermissionType.GALLERY -> {
                // Granted by default because photo picker does not require any runtime permissions
                callback.onPermissionStatus(
                    permissionType = permission,
                    status = PermissionStatus.GRANTED,
                )
            }
        }
    }

    @OptIn(ExperimentalPermissionsApi::class)
    @Composable
    override fun isPermissionGranted(permission: PermissionType): Boolean {
        return when (permission) {
            PermissionType.CAMERA -> {
                val cameraPermissionState = rememberPermissionState(permission = Manifest.permission.CAMERA)
                cameraPermissionState.status.isGranted
            }

            PermissionType.GALLERY -> {
                // Granted by default because photo picker does not require any runtime permissions
                true
            }
        }
    }

    @Composable
    override fun launchSettings() {
        val context = LocalContext.current

        Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts(
                "package",
                context.packageName,
                null,
            )
        ).also {
            context.startActivity(it)
        }
    }
}
