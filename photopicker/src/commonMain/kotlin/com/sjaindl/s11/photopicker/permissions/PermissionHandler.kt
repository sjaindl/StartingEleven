package com.sjaindl.s11.photopicker.permissions

import androidx.compose.runtime.Composable

interface PermissionHandler {
    @Composable
    fun askForPermission(permission: PermissionType)

    @Composable
    fun isPermissionGranted(permission: PermissionType): Boolean

    @Composable
    fun launchSettings()
}
