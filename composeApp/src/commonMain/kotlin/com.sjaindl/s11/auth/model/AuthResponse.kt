package com.sjaindl.s11.auth.model

sealed interface AuthResponse {
    data class Success(val account: GoogleAccount) : AuthResponse
    data class Error(val message: String) : AuthResponse
    data object Cancelled : AuthResponse
}
