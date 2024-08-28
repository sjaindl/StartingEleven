package com.sjaindl.s11.core.firestore.user

import com.sjaindl.s11.core.firestore.user.model.User
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import dev.gitlive.firebase.storage.File
import kotlinx.coroutines.flow.Flow
import org.koin.core.component.KoinComponent

interface UserRepository {
    suspend fun getUsers(): List<User>
    fun getUsersFlow(): Flow<List<User>>
    suspend fun getCurrentUser(): User?
    suspend fun setUserName(uid: String, newName: String)
    suspend fun setUserPhotoRef(uid: String, file: File)
    suspend fun deleteUserPhotoRef(uid: String)
    suspend fun setFormation(uid: String, formationId: String)
}

internal class UserRepositoryImpl(
    private val userDataSource: UserDataSource,
): UserRepository, KoinComponent {
    private val auth by lazy {
        Firebase.auth
    }

    override suspend fun getUsers(): List<User> {
        return userDataSource.getUsers()
    }

    override fun getUsersFlow(): Flow<List<User>> {
        return userDataSource.getUsersFlow()
    }

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

    override suspend fun setFormation(uid: String, formationId: String) {
        userDataSource.setFormation(uid = uid, formationId = formationId)
    }
}
