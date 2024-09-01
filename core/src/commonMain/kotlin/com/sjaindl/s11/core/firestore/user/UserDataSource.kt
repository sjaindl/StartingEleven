package com.sjaindl.s11.core.firestore.user

import com.sjaindl.s11.core.CachedValue
import com.sjaindl.s11.core.firestore.FireStoreBaseDataSource
import com.sjaindl.s11.core.firestore.user.model.User
import dev.gitlive.firebase.firestore.DocumentSnapshot
import dev.gitlive.firebase.firestore.FirebaseFirestore
import dev.gitlive.firebase.storage.File
import dev.gitlive.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Clock
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

interface UserDataSource {
    suspend fun getUsers(): List<User>
    fun getUsersFlow(): Flow<List<User>>
    suspend fun getUser(uid: String): User?
    suspend fun setUserName(uid: String, newName: String)
    suspend fun setUserPhotoRef(uid: String, file: File)
    suspend fun deleteUserPhotoRef(uid: String)
    suspend fun setFormation(uid: String, formationId: String)
}

internal class UserDataSourceImpl(
    firestore: FirebaseFirestore,
): FireStoreBaseDataSource<User>(firestore = firestore), UserDataSource, KoinComponent {
    private val storage: FirebaseStorage by inject()

    private var userCache: MutableMap<String, CachedValue<User>?> = mutableMapOf()
    private var usersCache: CachedValue<List<User>>? = null

    override val collectionPath: String = "users"

    override val mapper: (DocumentSnapshot) -> User = {
        it.data()
    }

    override suspend fun getUsers(): List<User> {
        val cachedValue = usersCache?.get()
        if (!cachedValue.isNullOrEmpty()) return cachedValue

        val users = getCollection().map {
            it.copy(photoRefDownloadUrl = getUserImageDownloadUrl(user = it))
        }

        usersCache = CachedValue(
            value = users,
        )
        return users
    }

    override fun getUsersFlow(): Flow<List<User>> = getCollectionFlow()

    override suspend fun getUser(uid: String): User? {
        val cachedValue = userCache[uid]?.get()
        if (cachedValue != null) return cachedValue

        val user = getUserWithImage(uid = uid)
        userCache[uid] = CachedValue(
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

            userCache[uid] = CachedValue(
                value = newUser,
            )
        }
    }

    override suspend fun setUserPhotoRef(uid: String, file: File) {
        val photoRef = "/users/$uid"
        val storageRef = storage.reference(location = photoRef)

        storageRef.putFile(file = file)

        getUser(uid = uid)?.let { user ->
            val newUser = user.copy(
                photoRef = photoRef,
                profilePhotoRefTimestamp = Clock.System.now().toString(),
            )

            val userDocRef = getDocumentRef(path = uid)
            userDocRef.set(data = newUser, merge = true)

            userCache[uid] = CachedValue(
                value = newUser,
            )
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

            userCache[uid] = CachedValue(
                value = newUser,
            )
        }
    }

    override suspend fun setFormation(uid: String, formationId: String) {
        getUser(uid = uid)?.let { user ->
            if (user.formation == formationId) return

            val newUser = user.copy(formation = formationId)

            val userDocRef = getDocumentRef(path = uid)
            userDocRef.set(data = newUser, merge = true)

            userCache[uid] = CachedValue(
                value = newUser,
            )
        }
    }

    private suspend fun getUserWithImage(uid: String): User? {
        val user = getDocument(path = uid)
        return user?.let {
            it.copy(
                photoRefDownloadUrl = getUserImageDownloadUrl(user = it),
            )
        }
    }

    private suspend fun getUserImageDownloadUrl(user: User): String? {
        return user.photoRef?.takeIf {
            it.isNotEmpty()
        }?.let {
            storage.reference(location = it).getDownloadUrl()
        }
    }
}
