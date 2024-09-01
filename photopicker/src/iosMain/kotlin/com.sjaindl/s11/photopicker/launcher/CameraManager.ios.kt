package com.sjaindl.s11.photopicker.launcher

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.sjaindl.s11.photopicker.model.SharedImage
import platform.UIKit.UIApplication
import platform.UIKit.UIImage
import platform.UIKit.UIImagePickerController
import platform.UIKit.UIImagePickerControllerCameraCaptureMode
import platform.UIKit.UIImagePickerControllerDelegateProtocol
import platform.UIKit.UIImagePickerControllerEditedImage
import platform.UIKit.UIImagePickerControllerOriginalImage
import platform.UIKit.UIImagePickerControllerSourceType
import platform.UIKit.UINavigationControllerDelegateProtocol
import platform.darwin.NSObject

@Composable
actual fun rememberCameraLauncher(onResult: (SharedImage?) -> Unit): CameraLauncher {
    val imagePicker = UIImagePickerController()

    val cameraDelegate = remember {
        object : NSObject(), UIImagePickerControllerDelegateProtocol,
            UINavigationControllerDelegateProtocol {
            override fun imagePickerController(
                picker: UIImagePickerController, didFinishPickingMediaWithInfo: Map<Any?, *>
            ) {
                val image =
                    didFinishPickingMediaWithInfo.getValue(UIImagePickerControllerEditedImage) as? UIImage
                        ?: didFinishPickingMediaWithInfo.getValue(
                            UIImagePickerControllerOriginalImage
                        ) as? UIImage

                onResult.invoke(SharedImage(image = image))
                picker.dismissViewControllerAnimated(flag = true, completion = null)
            }
        }
    }

    return remember {
        CameraLauncher {
            imagePicker.setSourceType(sourceType = UIImagePickerControllerSourceType.UIImagePickerControllerSourceTypeCamera)
            imagePicker.setAllowsEditing(allowsEditing = true)
            imagePicker.setCameraCaptureMode(cameraCaptureMode = UIImagePickerControllerCameraCaptureMode.UIImagePickerControllerCameraCaptureModePhoto)
            imagePicker.setDelegate(delegate = cameraDelegate)
            UIApplication.sharedApplication.keyWindow?.rootViewController?.presentViewController(
                viewControllerToPresent = imagePicker,
                animated = true,
                completion = null,
            )
        }
    }
}

actual class CameraLauncher actual constructor(
    private val onLaunch: () -> Unit
) {
    actual fun launch() {
        onLaunch()
    }
}
