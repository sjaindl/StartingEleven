package com.sjaindl.s11.photopicker.launcher

import android.content.ContentResolver
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.sjaindl.s11.photopicker.files.BitmapUtils
import com.sjaindl.s11.photopicker.model.SharedImage

@Composable
actual fun rememberGalleryLauncher(onResult: (SharedImage?) -> Unit): GalleryLauncher {
    val context = LocalContext.current
    val contentResolver = context.contentResolver

    val galleryLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            uri?.let {
                onResult.invoke(
                    SharedImage(
                        bitmap = BitmapUtils.getBitmapFromUri(
                            uri = uri,
                            contentResolver = contentResolver,
                        )
                    )
                )
            }
        }

    return remember {
        GalleryLauncher(
            onLaunch = {
                galleryLauncher.launch(
                    PickVisualMediaRequest(
                        mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly,
                    )
                )
            }
        )
    }
}

actual class GalleryLauncher actual constructor(private val onLaunch: () -> Unit) {
    actual fun launch() {
        onLaunch()
    }
}
