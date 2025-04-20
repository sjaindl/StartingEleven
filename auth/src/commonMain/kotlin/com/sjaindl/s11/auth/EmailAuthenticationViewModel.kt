package com.sjaindl.s11.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sjaindl.s11.auth.EmailAuthenticationState.EmailSignInSuccess
import com.sjaindl.s11.auth.EmailAuthenticationState.Error
import com.sjaindl.s11.auth.EmailAuthenticationState.Initial
import com.sjaindl.s11.auth.EmailAuthenticationState.Loading
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.FirebaseException
import dev.gitlive.firebase.auth.FirebaseAuthException
import dev.gitlive.firebase.auth.FirebaseAuthInvalidCredentialsException
import dev.gitlive.firebase.auth.FirebaseAuthInvalidUserException
import dev.gitlive.firebase.auth.auth
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

sealed class EmailAuthenticationState {
    data object Initial: EmailAuthenticationState()
    data object Loading: EmailAuthenticationState()
    data object EmailSignInSuccess: EmailAuthenticationState()
    data object EmailSignUpSuccess: EmailAuthenticationState()
    data class Error(val message: String): EmailAuthenticationState()
}

@KoinViewModel
class EmailAuthenticationViewModel : ViewModel() {

    private val tag = "EmailAuthenticationViewModel"

    private val auth by lazy {
        Firebase.auth
    }

    private var _authenticationState: MutableStateFlow<EmailAuthenticationState> = MutableStateFlow(
        Initial
    )
    var authenticationState = _authenticationState.asStateFlow()

    fun signInWithMail(email: String, password: String) = viewModelScope.launch {
        try {
            _authenticationState.value = Loading

            val user = auth.signInWithEmailAndPassword(email = email, password = password).user
            if (user != null) {
                Napier.d("Signed in user ${auth.currentUser?.displayName}")
                _authenticationState.value = EmailSignInSuccess
            } else {
                Napier.e("Sign-in failed unexpectedly")
                _authenticationState.value = Error(message = "Sign-in failed unexpectedly")
            }
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            Napier.e("Sign-in failed: Invalid credentials", e)
            _authenticationState.value = Error(message = e.message ?: e.toString())
        } catch (e: FirebaseAuthInvalidUserException) {
            Napier.e("Sign-in failed: Invalid user", e)
            _authenticationState.value = Error(message = e.message ?: e.toString())
        } catch (e: FirebaseAuthException) {
            // on iOS FirebaseAuthException is directly thrown
            Napier.e("Sign-in failed", e)
            _authenticationState.value = Error(message = e.message ?: e.toString())
        }
    }

    fun signUpWithMail(email: String, password: String, name: String) = viewModelScope.launch {
        try {
            _authenticationState.value = Loading

            val user = auth.createUserWithEmailAndPassword(email = email, password = password).user
            if (user != null) {
                Napier.d("Signed up user ${auth.currentUser?.displayName}")
                user.updateProfile(displayName = name)
                Napier.d(
                    message = "Set display name ${auth.currentUser?.displayName}",
                    throwable = null,
                    tag = tag,
                )
                _authenticationState.value = EmailSignInSuccess
            } else {
                Napier.e("Sign-up failed unexpectedly")
                _authenticationState.value = Error(message = "Sign-in failed unexpectedly")
            }
        } catch (e: FirebaseException) {
            Napier.e("Sign-up failed", e)
            _authenticationState.value = Error(message = e.message ?: e.toString())
        } catch (e: FirebaseAuthException) {
            // on iOS FirebaseAuthException is directly thrown
            Napier.e("Sign-up failed", e)
            _authenticationState.value = Error(message = e.message ?: e.toString())
        }
    }

    fun resetState() {
        _authenticationState.value = Initial
    }
}
