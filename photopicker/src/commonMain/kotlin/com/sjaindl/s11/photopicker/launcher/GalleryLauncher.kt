package com.sjaindl.s11.photopicker.launcher

import androidx.compose.runtime.Composable
import com.sjaindl.s11.photopicker.model.SharedImage

@Composable
expect fun rememberGalleryLauncher(onResult: (SharedImage?) -> Unit): GalleryLauncher

expect class GalleryLauncher(
    onLaunch: () -> Unit,
) {
    fun launch()
}
