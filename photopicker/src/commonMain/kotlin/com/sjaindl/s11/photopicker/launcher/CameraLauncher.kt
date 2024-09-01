package com.sjaindl.s11.photopicker.launcher

import androidx.compose.runtime.Composable
import com.sjaindl.s11.photopicker.model.SharedImage

@Composable
expect fun rememberCameraLauncher(onResult: (SharedImage?) -> Unit): CameraLauncher

expect class CameraLauncher(
    onLaunch: () -> Unit
) {
    fun launch()
}
