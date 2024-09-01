package com.sjaindl.s11.core.firestore

import androidx.core.net.toUri
import java.io.File
import dev.gitlive.firebase.storage.File as FirebaseFile

actual fun convertToFirebaseFile(filePath: String): FirebaseFile {
    val file = File(filePath)

    return FirebaseFile(file.toUri())
}
