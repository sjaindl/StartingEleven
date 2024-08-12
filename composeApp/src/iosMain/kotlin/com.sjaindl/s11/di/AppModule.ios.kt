package com.sjaindl.s11.di

import cocoapods.FirebaseFirestore.*
import dev.gitlive.firebase.firestore.FirebaseFirestore

var globalFireStore: FIRFirestore? = null

actual fun getFirebaseFirestore(): FirebaseFirestore {
    // FIRFirestore.firestoreForDatabase("") // preview feature not supported yet
    // Firestore.firestore(database: "s11-prod")
    // https://firebase.google.com/docs/reference/swift/firebasefirestore/api/reference/Classes/Firestore#/c:objc(cs)FIRFirestore(cm)firestoreForApp:database:

    return FirebaseFirestore(ios = globalFireStore ?: FIRFirestore.firestore())
}
