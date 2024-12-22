package com.sjaindl.s11.di

import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import dev.gitlive.firebase.firestore.FirebaseFirestore

actual fun getFirebaseFirestore(): FirebaseFirestore {
    return FirebaseFirestore(native = Firebase.firestore(database = "s11-prod"))
}
