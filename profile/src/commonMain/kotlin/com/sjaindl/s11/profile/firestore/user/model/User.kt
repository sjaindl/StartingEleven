package com.sjaindl.s11.profile.firestore.user.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val email: String,
    val formation: String,
    val isAdmin: Boolean,
    val photoRef: String?,
    val photoUrl: String?,
    val providerId: String?,
    val uid: String,
    val userName: String,
)
