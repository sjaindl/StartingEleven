package com.sjaindl.s11.core.firestore.user.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val uid: String,
    val email: String,
    val userName: String,
    val photoRef: String?,
    val photoUrl: String?,
    val providerId: String?,
    val formation: String,
    val isAdmin: Boolean = false,
)
