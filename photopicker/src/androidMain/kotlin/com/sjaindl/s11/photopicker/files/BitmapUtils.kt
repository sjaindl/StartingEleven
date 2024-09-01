package com.sjaindl.s11.photopicker.files

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.util.Log
import androidx.exifinterface.media.ExifInterface

object BitmapUtils {
    private const val TAG = "BitmapUtils"

    fun getBitmapFromUri(uri: Uri, contentResolver: ContentResolver): Bitmap? {
        try {
            val sourceBitmap = contentResolver.openInputStream(uri).use { inStream ->
                return@use BitmapFactory.decodeStream(inStream)
            }
            val exifInfo = contentResolver.openInputStream(uri)?.use { inStream ->
                ExifInterface(inStream)
            }

            return sourceBitmap.adjustRotation(exifInfo = exifInfo)
        } catch (exception: Exception) {
            Log.e(TAG, "getBitmapFromUri Exception: ${exception.message}")
            return null
        }
    }

    fun Bitmap.adjustRotation(exifInfo: ExifInterface?): Bitmap {
        val imageRotation = exifInfo?.rotationDegrees?.toFloat() ?: 0f

        if (imageRotation == 0f) {
            return this
        }

        val newBitmap = Bitmap.createBitmap(
            this,
            0,
            0,
            width,
            height,
            Matrix().apply {
                postRotate(imageRotation)
            },
            true,
        )

        if (newBitmap != this) {
            // source bitmap might be the same object!
            this.recycle()
        }

        return newBitmap
    }
}
