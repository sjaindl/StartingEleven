package com.sjaindl.s11.auth.model

sealed interface AppleAuthResponse {
    data class Success(val identityToken: String?, val nonce: String?) : AppleAuthResponse
    data class Error(val message: String?) : AppleAuthResponse
    data object Cancelled : AppleAuthResponse
}
