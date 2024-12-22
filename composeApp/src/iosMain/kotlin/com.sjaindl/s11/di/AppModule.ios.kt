package com.sjaindl.s11.di

import cocoapods.FirebaseFirestoreInternal.FIRFirestore
import dev.gitlive.firebase.firestore.FirebaseFirestore
import kotlinx.cinterop.ExperimentalForeignApi

@OptIn(ExperimentalForeignApi::class)
actual fun getFirebaseFirestore(): FirebaseFirestore {
    return FirebaseFirestore(FIRFirestore.firestoreForDatabase(database = "s11-prod"))
}
