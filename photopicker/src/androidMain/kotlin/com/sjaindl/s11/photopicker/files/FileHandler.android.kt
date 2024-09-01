package com.sjaindl.s11.photopicker.files

import android.content.Context
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.io.File
import java.io.FileOutputStream

actual class FileHandler: KoinComponent {
    private val context: Context by inject()

    actual fun getTemporaryFilePath(byteArray: ByteArray): String? {
        val file = File(context.cacheDir, FILE_NAME)
        FileOutputStream(file).use { outputStream ->
            outputStream.write(byteArray)
        }

        return file.absolutePath
    }
}
