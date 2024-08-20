package com.sjaindl.s11.profile.firestore.user

import com.sjaindl.s11.profile.firestore.user.model.User
import dev.gitlive.firebase.auth.FirebaseAuth
import dev.gitlive.firebase.storage.File
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

interface UserRepository {
    suspend fun getCurrentUser(): User?
    suspend fun setUserName(uid: String, newName: String)
    suspend fun setUserPhotoRef(uid: String, file: File)
    suspend fun deleteUserPhotoRef(uid: String)
}

internal class UserRepositoryImpl(
    private val userDataSource: UserDataSource,
): UserRepository, KoinComponent {
    private val auth: FirebaseAuth by inject()

    override suspend fun getCurrentUser() = auth.currentUser?.uid?.let {
        userDataSource.getUser(uid = it)
    }

    override suspend fun setUserName(uid: String, newName: String) {
        userDataSource.setUserName(uid = uid, newName = newName)
    }

    override suspend fun setUserPhotoRef(uid: String, file: File) {
        userDataSource.setUserPhotoRef(uid = uid, file = file)
    }

    override suspend fun deleteUserPhotoRef(uid: String) {
        userDataSource.deleteUserPhotoRef(uid = uid)
    }
}
