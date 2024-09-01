package com.sjaindl.s11.photopicker.files

import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import platform.Foundation.NSData
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask
import platform.Foundation.URLByAppendingPathComponent
import platform.Foundation.create
import platform.Foundation.writeToURL

actual class FileHandler {
    actual fun getTemporaryFilePath(byteArray: ByteArray): String? {
        val fileManager = NSFileManager.defaultManager

        val documentsDirectory = fileManager.URLsForDirectory(
            directory = NSDocumentDirectory,
            inDomains = NSUserDomainMask,
        ).first() as NSURL

        val fileURL = documentsDirectory.URLByAppendingPathComponent(pathComponent = FILE_NAME)
        val nsData = byteArrayToNSData(byteArray = byteArray)

        return fileURL?.let {
            nsData.writeToURL(
                url = it,
                atomically = true,
            )

            it.path
        }
    }

    @OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
    fun byteArrayToNSData(byteArray: ByteArray): NSData {
        return byteArray.usePinned { pinned ->
            NSData.create(
                bytes = pinned.addressOf(index = 0),
                length = byteArray.size.toULong(),
            )
        }
    }
}
