package com.sjaindl.s11.photopicker

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.sjaindl.s11.core.theme.HvtdpTheme
import com.sjaindl.s11.photopicker.launcher.rememberCameraLauncher
import com.sjaindl.s11.photopicker.launcher.rememberGalleryLauncher
import com.sjaindl.s11.photopicker.permissions.PermissionCallback
import com.sjaindl.s11.photopicker.permissions.PermissionStatus
import com.sjaindl.s11.photopicker.permissions.PermissionType
import com.sjaindl.s11.photopicker.permissions.createPermissionsManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import startingeleven.photopicker.generated.resources.Res
import startingeleven.photopicker.generated.resources.permissionRationaleMessage
import startingeleven.photopicker.generated.resources.permissionRationaleNegativeButtonText
import startingeleven.photopicker.generated.resources.permissionRationalePositiveButtonText
import startingeleven.photopicker.generated.resources.permissionRationaleTitle

@Composable
fun PhotoPickerScreen(
    onByteArray: (ByteArray?) -> Unit,
    onDeletePhoto: () -> Unit,
) {
    HvtdpTheme {
        val coroutineScope = rememberCoroutineScope()

        var showPhotoPickerBottomSheet by remember {
            mutableStateOf(value = true)
        }

        var launchCamera by remember {
            mutableStateOf(value = false)
        }

        var launchGallery by remember {
            mutableStateOf(value = false)
        }

        var launchSettings by remember {
            mutableStateOf(value = false)
        }

        var showPermissionRationaleDialog by remember {
            mutableStateOf(value = false)
        }

        val permissionsManager = createPermissionsManager(
            object : PermissionCallback {
                override fun onPermissionStatus(
                    permissionType: PermissionType,
                    status: PermissionStatus,
                ) {
                    when (status) {
                        PermissionStatus.GRANTED -> {
                            when (permissionType) {
                                PermissionType.CAMERA -> launchCamera = true
                                PermissionType.GALLERY -> launchGallery = true
                            }
                        }

                        else -> showPermissionRationaleDialog = true
                    }
                }
            }
        )

        val cameraManager = rememberCameraLauncher {
            coroutineScope.launch {
                val byteArray = withContext(Dispatchers.Default) {
                    it?.toByteArray()
                }
                onByteArray(byteArray)
            }
        }

        val galleryManager = rememberGalleryLauncher {
            coroutineScope.launch {
                val byteArray = withContext(Dispatchers.Default) {
                    it?.toByteArray()
                }
                onByteArray(byteArray)
            }
        }

        if (showPhotoPickerBottomSheet) {
            PhotoPickerBottomSheet(
                onDismiss = {
                    showPhotoPickerBottomSheet = false
                    if (!launchSettings && !launchCamera && !launchGallery) {
                        onByteArray(null)
                    }
                },
                onPickPhoto = {
                    showPhotoPickerBottomSheet = false
                    launchGallery = true
                },
                onTakePhoto = {
                    showPhotoPickerBottomSheet = false
                    launchCamera = true
                },
                onDeletePhoto = {
                    showPhotoPickerBottomSheet = false
                    onDeletePhoto()
                },
                canDeletePhoto = false,
            )
        }

        if (launchGallery) {
            if (permissionsManager.isPermissionGranted(PermissionType.GALLERY)) {
                galleryManager.launch()
            } else {
                permissionsManager.askForPermission(PermissionType.GALLERY)
            }
            launchGallery = false
        }

        if (launchCamera) {
            if (permissionsManager.isPermissionGranted(PermissionType.CAMERA)) {
                cameraManager.launch()
            } else {
                permissionsManager.askForPermission(PermissionType.CAMERA)
            }
            launchCamera = false
        }

        if (launchSettings) {
            permissionsManager.launchSettings()
            launchSettings = false
        }

        if (showPermissionRationaleDialog) {
            AlertMessageDialog(
                title = stringResource(Res.string.permissionRationaleTitle),
                message = stringResource(Res.string.permissionRationaleMessage),
                positiveButtonText = stringResource(Res.string.permissionRationalePositiveButtonText),
                negativeButtonText = stringResource(Res.string.permissionRationaleNegativeButtonText),
                onPositiveClick = {
                    showPermissionRationaleDialog = false
                    launchSettings = true
                },
                onNegativeClick = {
                    showPermissionRationaleDialog = false
                },
            )
        }
    }
}

@Preview
@Composable
fun PhotoPickerScreenPreview() {
    HvtdpTheme {
        PhotoPickerScreen(
            onByteArray = { },
            onDeletePhoto = { },
        )
    }
}
