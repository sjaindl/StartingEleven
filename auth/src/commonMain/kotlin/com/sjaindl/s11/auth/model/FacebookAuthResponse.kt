package com.sjaindl.s11.auth.model

sealed interface FacebookAuthResponse {
    data class Success(val accessToken: String) : FacebookAuthResponse
    data class Error(val message: String) : FacebookAuthResponse
    data object Cancelled : FacebookAuthResponse
}
