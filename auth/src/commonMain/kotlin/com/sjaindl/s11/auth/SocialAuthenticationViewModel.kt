package com.sjaindl.s11.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sjaindl.s11.auth.SocialAuthenticationState.Error
import com.sjaindl.s11.auth.SocialAuthenticationState.FacebookSignInSuccess
import com.sjaindl.s11.auth.SocialAuthenticationState.GoogleSignInSuccess
import com.sjaindl.s11.auth.SocialAuthenticationState.Initial
import com.sjaindl.s11.auth.model.FacebookAuthResponse
import com.sjaindl.s11.auth.model.GoogleAuthResponse
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.FacebookAuthProvider
import dev.gitlive.firebase.auth.FirebaseAuthException
import dev.gitlive.firebase.auth.FirebaseAuthInvalidCredentialsException
import dev.gitlive.firebase.auth.GoogleAuthProvider
import dev.gitlive.firebase.auth.auth
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class SocialAuthenticationState {
    data object Initial: SocialAuthenticationState()
    data object Loading: SocialAuthenticationState()
    data object GoogleSignInSuccess: SocialAuthenticationState()
    data object FacebookSignInSuccess: SocialAuthenticationState()
    data class Error(val message: String): SocialAuthenticationState()
}

class SocialAuthenticationViewModel : ViewModel() {

    private var _authenticationState: MutableStateFlow<SocialAuthenticationState> = MutableStateFlow(
        Initial
    )
    var authenticationState = _authenticationState.asStateFlow()

    fun handleGoogleSignIn(googleAuthResponse: GoogleAuthResponse, cancelMessage: String) {
        viewModelScope.launch {
            when(googleAuthResponse) {
                GoogleAuthResponse.Cancelled -> {
                    _authenticationState.value = Error(message = cancelMessage)
                }
                is GoogleAuthResponse.Error -> {
                    _authenticationState.value = Error(message = googleAuthResponse.message)
                }
                is GoogleAuthResponse.Success -> {
                    Firebase.auth.signInWithCredential(
                        GoogleAuthProvider.credential(
                            idToken = googleAuthResponse.account.idToken,
                            accessToken = googleAuthResponse.account.accessToken,
                        )
                    )
                    _authenticationState.value = GoogleSignInSuccess
                }
            }
        }
    }

    fun handleFacebookSignIn(facebookAuthResponse: FacebookAuthResponse, cancelMessage: String) {
        viewModelScope.launch {
            when(facebookAuthResponse) {
                FacebookAuthResponse.Cancelled -> {
                    _authenticationState.value = Error(message = cancelMessage)
                }
                is FacebookAuthResponse.Error -> {
                    _authenticationState.value = Error(message = facebookAuthResponse.message)
                }
                is FacebookAuthResponse.Success -> {
                    try {
                        Firebase.auth.signInWithCredential(
                            FacebookAuthProvider.credential(
                                accessToken = facebookAuthResponse.accessToken,
                            )
                        )
                        _authenticationState.value = FacebookSignInSuccess
                    } catch (e: FirebaseAuthInvalidCredentialsException) {
                        Napier.e(message = "Firebase Auth Error: FirebaseAuthInvalidCredentialsException", throwable = e)
                        _authenticationState.value = Error(message = "${e.message}")
                    } catch (e: FirebaseAuthException) {
                        Napier.e("Sign-in failed", e)
                        _authenticationState.value = Error(message = e.message ?: e.toString())
                    }
                }
            }
        }
    }

    fun resetState() {
        _authenticationState.value = Initial
    }
}
