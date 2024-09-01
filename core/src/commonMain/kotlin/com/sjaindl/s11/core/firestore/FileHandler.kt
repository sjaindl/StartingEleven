package com.sjaindl.s11.core.firestore

import dev.gitlive.firebase.storage.File

expect fun convertToFirebaseFile(filePath: String): File
