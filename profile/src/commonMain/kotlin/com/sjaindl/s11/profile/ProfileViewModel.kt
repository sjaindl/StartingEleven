package com.sjaindl.s11.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sjaindl.s11.core.firestore.user.UserRepository
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.FirebaseAuthRecentLoginRequiredException
import dev.gitlive.firebase.auth.auth
import dev.gitlive.firebase.storage.File
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

sealed class UserState {
    data object Initial : UserState()
    data object Loading : UserState()
    data object NoUser : UserState()
    data class User(
        val uid: String,
        val name: String?,
        val email: String?,
        val photoUrl: String?,
        val photoRefDownloadUrl: String?,
        val profilePhotoRefTimestamp: String?,
    ) : UserState()

    data class Error(val message: String) : UserState()
}

@KoinViewModel
class ProfileViewModel : ViewModel(), KoinComponent {

    private val tag = "ProfileViewModel"

    private val userRepository: UserRepository by inject()

    private var _userState: MutableStateFlow<UserState> = MutableStateFlow(
        UserState.Initial
    )
    var userState = _userState.asStateFlow()

    init {
        loadUser()
    }

    fun deleteAccount(completed: () -> Unit) {
        viewModelScope.launch {
            with(Firebase.auth) {
                currentUser?.let {
                    try {
                        currentUser?.delete()
                        userRepository.deleteUser(uid = it.uid)
                    } catch (exception: FirebaseAuthRecentLoginRequiredException) {
                        Napier.e(message = "Could not delete user", throwable = exception)
                    }

                    signOut()
                    completed()
                }
            }
        }
    }

    fun loadUser() = viewModelScope.launch {
        _userState.value = UserState.Loading

        val user = userRepository.getCurrentUser()

        if (user != null) {
            _userState.value = UserState.User(
                uid = user.uid,
                name = user.userName,
                email = user.email,
                photoUrl = user.photoUrl,
                photoRefDownloadUrl = user.photoRefDownloadUrl,
                profilePhotoRefTimestamp = user.profilePhotoRefTimestamp,
            )
        } else {
            _userState.value = UserState.NoUser
        }
    }

    fun changeUserName(uid: String, newName: String) = viewModelScope.launch {
        val currentUser = userState.value as? UserState.User ?: return@launch
        if (currentUser.name == newName) return@launch

        _userState.value = UserState.Loading

        try {
            userRepository.setUserName(uid = uid, newName = newName)

            _userState.value = UserState.User(
                uid = uid,
                name = newName,
                email = currentUser.email,
                photoUrl = currentUser.photoUrl,
                photoRefDownloadUrl = currentUser.photoRefDownloadUrl,
                profilePhotoRefTimestamp = currentUser.profilePhotoRefTimestamp,
            )
        } catch (exception: Exception) {
            val message = exception.message ?: exception.toString()
            Napier.e(message = message, throwable = exception, tag = tag)
            _userState.value = UserState.Error(message = message)
        }
    }

    fun uploadProfilePhoto(uid: String, file: File) = viewModelScope.launch {
        val currentUser = userState.value as? UserState.User ?: return@launch

        _userState.value = UserState.Loading

        try {
            userRepository.setUserPhotoRef(uid = uid, file = file)
            val user = userRepository.getCurrentUser()

            _userState.value = UserState.User(
                uid = uid,
                name = currentUser.name,
                email = currentUser.email,
                photoUrl = user?.photoUrl,
                photoRefDownloadUrl = user?.photoRefDownloadUrl,
                profilePhotoRefTimestamp = user?.profilePhotoRefTimestamp,
            )
        } catch (exception: Exception) {
            val message = exception.message ?: exception.toString()
            Napier.e(message = message, throwable = exception, tag = tag)
            _userState.value = UserState.Error(message = message)
        }
    }

    fun deleteProfilePhoto(uid: String) = viewModelScope.launch {
        val currentUser = userState.value as? UserState.User ?: return@launch

        _userState.value = UserState.Loading

        try {
            userRepository.deleteUserPhotoRef(uid = uid)

            _userState.value = UserState.User(
                uid = uid,
                name = currentUser.name,
                email = currentUser.email,
                photoUrl = null,
                photoRefDownloadUrl = null,
                profilePhotoRefTimestamp = null,
            )
        } catch (exception: Exception) {
            val message = exception.message ?: exception.toString()
            Napier.e(message = message, throwable = exception, tag = tag)
            _userState.value = UserState.Error(message = message)
        }
    }
}
