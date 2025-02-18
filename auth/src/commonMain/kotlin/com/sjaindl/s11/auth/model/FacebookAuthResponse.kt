package com.sjaindl.s11.auth.model

sealed interface FacebookAuthResponse {
	data class Success(val accessToken: String, val nonce: String? = null, val limited: Boolean = true) : FacebookAuthResponse
    data class Error(val message: String) : FacebookAuthResponse
    data object Cancelled : FacebookAuthResponse
}
