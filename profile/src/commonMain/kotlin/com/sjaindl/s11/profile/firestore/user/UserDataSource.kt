package com.sjaindl.s11.profile.firestore.user

import com.sjaindl.s11.core.CachedValue
import com.sjaindl.s11.core.firestore.FireStoreBaseDataSource
import com.sjaindl.s11.profile.firestore.user.model.User
import dev.gitlive.firebase.firestore.DocumentSnapshot
import dev.gitlive.firebase.firestore.FirebaseFirestore
import dev.gitlive.firebase.storage.File
import dev.gitlive.firebase.storage.FirebaseStorage
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

interface UserDataSource {
    suspend fun getUser(uid: String): User?
    suspend fun setUserName(uid: String, newName: String)
    suspend fun setUserPhotoRef(uid: String, file: File)
    suspend fun deleteUserPhotoRef(uid: String)
}

internal class UserDataSourceImpl(
    firestore: FirebaseFirestore,
): FireStoreBaseDataSource<User>(firestore = firestore), UserDataSource, KoinComponent {
    private val storage: FirebaseStorage by inject()

    private var cache: CachedValue<User>? = null

    override val collectionPath: String = "users"

    override val mapper: (DocumentSnapshot) -> User = {
        it.data()
    }

    override suspend fun getUser(uid: String): User? {
        val cachedValue = cache?.get()
        if (cachedValue != null) return cachedValue

        val user = getUserWithImage(uid = uid)
        cache = CachedValue(
            value = user,
        )

        return user
    }

    override suspend fun setUserName(uid: String, newName: String) {
        getUser(uid = uid)?.let { user ->
            if (user.userName == newName) return

            val newUser = user.copy(userName = newName)

            val userDocRef = getDocumentRef(path = uid)
            userDocRef.set(data = newUser, merge = true)
        }
    }

    override suspend fun setUserPhotoRef(uid: String, file: File) {
        val photoRef = "/users/$uid"
        val storageRef = storage.reference(location = photoRef)

        // TODO: ev. make resumable
        storageRef.putFile(file = file)

        getUser(uid = uid)?.let { user ->
            if (user.photoRef == photoRef) return

            val newUser = user.copy(photoRef = photoRef)

            val userDocRef = getDocumentRef(path = uid)
            userDocRef.set(data = newUser, merge = true)
        }
    }

    override suspend fun deleteUserPhotoRef(uid: String) {
        val photoRef = "/users/$uid"
        val storageRef = storage.reference(location = photoRef)

        storageRef.delete()

        getUser(uid = uid)?.let { user ->
            val newUser = user.copy(photoRef = null)

            val userDocRef = getDocumentRef(path = uid)
            userDocRef.set(data = newUser, merge = true)
        }
    }

    private suspend fun getUserWithImage(uid: String): User? {
        val user = getDocument(path = uid)
        return user?.copy(
            photoUrl = getUserImageDownloadUrl(user),
        )
    }

    private suspend fun getUserImageDownloadUrl(user: User): String? {
        return user.photoRef?.let {
            storage.reference(location = it).getDownloadUrl()
        }
    }
}
