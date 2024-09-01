package com.sjaindl.s11.photopicker.permissions

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import platform.AVFoundation.AVAuthorizationStatus
import platform.AVFoundation.AVAuthorizationStatusAuthorized
import platform.AVFoundation.AVAuthorizationStatusDenied
import platform.AVFoundation.AVAuthorizationStatusNotDetermined
import platform.AVFoundation.AVCaptureDevice
import platform.AVFoundation.AVMediaTypeVideo
import platform.AVFoundation.authorizationStatusForMediaType
import platform.AVFoundation.requestAccessForMediaType
import platform.Foundation.NSURL
import platform.Photos.PHAuthorizationStatus
import platform.Photos.PHAuthorizationStatusAuthorized
import platform.Photos.PHAuthorizationStatusDenied
import platform.Photos.PHAuthorizationStatusNotDetermined
import platform.Photos.PHPhotoLibrary
import platform.UIKit.UIApplication
import platform.UIKit.UIApplicationOpenSettingsURLString

@Composable
actual fun createPermissionsManager(callback: PermissionCallback): PermissionsManager {
    return PermissionsManager(callback)
}

actual class PermissionsManager actual constructor(private val callback: PermissionCallback) :
    PermissionHandler {
    @Composable
    override fun askForPermission(permission: PermissionType) {
        when (permission) {
            PermissionType.CAMERA -> {
                val status = remember {
                    AVCaptureDevice.authorizationStatusForMediaType(mediaType = AVMediaTypeVideo)
                }

                askCameraPermission(status = status, permission = permission, callback = callback)
            }

            PermissionType.GALLERY -> {
                val status = remember {
                    PHPhotoLibrary.authorizationStatus()
                }

                askGalleryPermission(status = status, permission = permission, callback = callback)
            }
        }
    }

    private fun askCameraPermission(
        status: AVAuthorizationStatus,
        permission: PermissionType,
        callback: PermissionCallback,
    ) {
        when (status) {
            AVAuthorizationStatusAuthorized -> {
                callback.onPermissionStatus(
                    permissionType = permission,
                    status = PermissionStatus.GRANTED,
                )
            }

            AVAuthorizationStatusNotDetermined -> {
                return AVCaptureDevice.Companion.requestAccessForMediaType(mediaType = AVMediaTypeVideo) { isGranted ->
                    if (isGranted) {
                        callback.onPermissionStatus(
                            permissionType = permission,
                            status = PermissionStatus.GRANTED,
                        )
                    } else {
                        callback.onPermissionStatus(
                            permissionType = permission,
                            status = PermissionStatus.DENIED,
                        )
                    }
                }
            }

            AVAuthorizationStatusDenied -> {
                callback.onPermissionStatus(
                    permissionType = permission,
                    status = PermissionStatus.DENIED,
                )
            }

            else -> error("unknown camera status $status")
        }
    }

    private fun askGalleryPermission(
        status: PHAuthorizationStatus,
        permission: PermissionType,
        callback: PermissionCallback,
    ) {
        when (status) {
            PHAuthorizationStatusAuthorized -> {
                callback.onPermissionStatus(
                    permissionType = permission,
                    status = PermissionStatus.GRANTED,
                )
            }

            PHAuthorizationStatusNotDetermined -> {
                PHPhotoLibrary.Companion.requestAuthorization { newStatus ->
                    askGalleryPermission(
                        status = newStatus,
                        permission = permission,
                        callback = callback,
                    )
                }
            }

            PHAuthorizationStatusDenied -> {
                callback.onPermissionStatus(
                    permissionType = permission,
                    status = PermissionStatus.DENIED,
                )
            }

            else -> error("unknown gallery status $status")
        }
    }

    @Composable
    override fun isPermissionGranted(permission: PermissionType): Boolean {
        return when (permission) {
            PermissionType.CAMERA -> {
                val status = remember {
                    AVCaptureDevice.authorizationStatusForMediaType(mediaType = AVMediaTypeVideo)
                }

                status == AVAuthorizationStatusAuthorized
            }

            PermissionType.GALLERY -> {
                val status = remember {
                    PHPhotoLibrary.authorizationStatus()
                }
                status == PHAuthorizationStatusAuthorized
            }
        }
    }

    @Composable
    override fun launchSettings() {
        NSURL.URLWithString(UIApplicationOpenSettingsURLString)?.let {
            UIApplication.sharedApplication.openURL(url = it)
        }
    }
}
