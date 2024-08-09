package com.sjaindl.s11.auth.model

data class GoogleAccount(
    val idToken: String,
    val accessToken: String?,
    val googleProfile: GoogleProfile,
)

data class GoogleProfile(
    val name: String?,
    val familyName: String?,
    val givenName: String?,
    val email: String?,
    val pictureUrl: String?,
)
