package com.sjaindl.s11.core.firestore

import dev.gitlive.firebase.storage.File
import platform.Foundation.NSURL

actual fun convertToFirebaseFile(filePath: String): File {
    return File(NSURL(fileURLWithPath = filePath))
}
