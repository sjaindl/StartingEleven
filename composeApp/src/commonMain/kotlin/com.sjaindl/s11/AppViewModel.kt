package com.sjaindl.s11

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sjaindl.s11.core.firestore.user.UserRepository
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class AppViewModel : ViewModel(), KoinComponent {

    private val userRepository: UserRepository by inject<UserRepository>()

    private var _userName = MutableStateFlow(
        value = Firebase.auth.currentUser?.displayName,
    )
    var userName = _userName.asStateFlow()

    private var _isAuthenticated = MutableStateFlow(
        value = Firebase.auth.currentUser != null,
    )
    var isAuthenticated = _isAuthenticated.asStateFlow()

    init {
        setObservers()
    }

    private fun setObservers() {
        viewModelScope.launch {
            Firebase.auth.authStateChanged.distinctUntilChanged().collectLatest { user ->
               _isAuthenticated.value  = user != null
            }
        }

        viewModelScope.launch {
            Firebase.auth.authStateChanged.distinctUntilChanged().collectLatest { user ->
                if (user != null) {
                    val dbUser = userRepository.getCurrentUser()
                    if (dbUser == null) {
                        userRepository.createUser(user)
                    }

                    _userName.value = userRepository.getCurrentUser()?.userName ?: user.displayName
                } else {
                    _userName.value = null
                }
            }
        }
    }
}
