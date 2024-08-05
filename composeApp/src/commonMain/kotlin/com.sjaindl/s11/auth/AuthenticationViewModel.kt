package com.sjaindl.s11.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.FirebaseException
import dev.gitlive.firebase.auth.FirebaseAuthInvalidCredentialsException
import dev.gitlive.firebase.auth.auth
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class AuthenticationState {
    data object Initial: AuthenticationState()
    data object Loading: AuthenticationState()
    data object EmailSignInSuccess: AuthenticationState()
    data object EmailSignUpSuccess: AuthenticationState()
    data object GoogleSignInSuccess: AuthenticationState()
    data object FacebookSignInSuccess: AuthenticationState()
    data class Error(val message: String): AuthenticationState()
}

class AuthenticationViewModel : ViewModel() {

    private val tag = "AuthenticationViewModel"

    private val auth by lazy {
        Firebase.auth
    }

    private var _authenticationState: MutableStateFlow<AuthenticationState> = MutableStateFlow(AuthenticationState.Initial)
    var authenticationState = _authenticationState.asStateFlow()

    fun signInWithMail(email: String, password: String) = viewModelScope.launch {
        try {
            _authenticationState.value = AuthenticationState.Loading

            val user = auth.signInWithEmailAndPassword(email = email, password = password).user
            if (user != null) {
                Napier.d("Signed in user ${auth.currentUser?.displayName}")
                _authenticationState.value = AuthenticationState.EmailSignInSuccess
            } else {
                Napier.e("Sign-in failed unexpectedly")
                _authenticationState.value = AuthenticationState.Error(message = "Sign-in failed unexpectedly")
            }
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            Napier.e("Sign-in failed: Invalid credentials", e)
            _authenticationState.value = AuthenticationState.Error(message = e.message ?: e.toString())
        }
    }

    fun signUpWithMail(email: String, password: String, name: String) = viewModelScope.launch {
        try {
            _authenticationState.value = AuthenticationState.Loading

            val user = auth.createUserWithEmailAndPassword(email = email, password = password).user
            if (user != null) {
                Napier.d("Signed up user ${auth.currentUser?.displayName}")
                user.updateProfile(displayName = name)
                Napier.d(
                    message = "Set display name ${auth.currentUser?.displayName}",
                    throwable = null,
                    tag = tag,
                )
                _authenticationState.value = AuthenticationState.EmailSignInSuccess
            } else {
                Napier.e("Sign-up failed unexpectedly")
                _authenticationState.value = AuthenticationState.Error(message = "Sign-in failed unexpectedly")
            }
        } catch (e: FirebaseException) {
            Napier.e("Sign-up failed", e)
            _authenticationState.value = AuthenticationState.Error(message = e.message ?: e.toString())
        }
    }

    fun resetState() {
        _authenticationState.value = AuthenticationState.Initial
    }
}
