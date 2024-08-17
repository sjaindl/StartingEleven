package com.sjaindl.s11.auth.model

sealed interface GoogleAuthResponse {
    data class Success(val account: GoogleAccount) : GoogleAuthResponse
    data class Error(val message: String) : GoogleAuthResponse
    data object Cancelled : GoogleAuthResponse
}
