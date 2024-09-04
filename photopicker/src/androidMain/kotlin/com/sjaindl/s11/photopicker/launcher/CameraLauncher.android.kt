package com.sjaindl.s11.photopicker.launcher

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.sjaindl.s11.photopicker.files.BitmapUtils
import com.sjaindl.s11.photopicker.model.SharedImage
import com.sjaindl.s11.picker.ComposeFileProvider

@Composable
actual fun rememberCameraLauncher(onResult: (SharedImage?) -> Unit): CameraLauncher {
    val context = LocalContext.current
    val contentResolver = context.contentResolver

    var tempPhotoUri by remember {
        mutableStateOf(value = Uri.EMPTY)
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success ->
            if (success) {
                onResult(
                    SharedImage(
                        BitmapUtils.getBitmapFromUri(
                            uri = tempPhotoUri,
                            contentResolver = contentResolver,
                        )
                    )
                )
            } else {
                onResult(null)
            }
        }
    )

    return remember {
        CameraLauncher(
            onLaunch = {
                tempPhotoUri = ComposeFileProvider.getImageUri(context = context)
                cameraLauncher.launch(input = tempPhotoUri)
            }
        )
    }
}

actual class CameraLauncher actual constructor(
    private val onLaunch: () -> Unit
) {
    actual fun launch() {
        onLaunch()
    }
}
