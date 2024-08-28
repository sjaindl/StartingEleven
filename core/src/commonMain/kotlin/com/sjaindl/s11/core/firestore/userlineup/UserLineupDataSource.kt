package com.sjaindl.s11.core.firestore.userlineup

import com.sjaindl.s11.core.CachedValue
import com.sjaindl.s11.core.firestore.FireStoreBaseDataSource
import com.sjaindl.s11.core.firestore.userlineup.model.LineupData
import dev.gitlive.firebase.firestore.DocumentSnapshot
import dev.gitlive.firebase.firestore.FirebaseFirestore
import org.koin.core.component.KoinComponent

interface UserLineupDataSource {
    suspend fun getUserLineup(uid: String): LineupData
    suspend fun setUserLineup(uid: String, userLineup: LineupData)
}

internal class UserLineupDataSourceImpl(
    firestore: FirebaseFirestore,
): FireStoreBaseDataSource<LineupData>(firestore = firestore), UserLineupDataSource, KoinComponent {
    private var cache: MutableMap<String, CachedValue<LineupData>> = mutableMapOf()

    override val collectionPath: String = "users"

    override val mapper: (DocumentSnapshot) -> LineupData = {
        it.data()
    }

    override suspend fun getUserLineup(uid: String): LineupData {
        val cachedValue = cache[uid]?.get()
        if (cachedValue != null) return cachedValue

        val userLineup = getDocumentRef(path = uid)
            .collection(collectionPath = "lineup")
            .document(documentPath = "lineupData")
            .get()
            .data<LineupData>()

        cache[uid] = CachedValue(
            value = userLineup,
        )
        return userLineup
    }

    override suspend fun setUserLineup(uid: String, userLineup: LineupData) {
        val userLineupDoc = getDocumentRef(path = uid)
            .collection(collectionPath = "lineup")
            .document(documentPath = "lineupData")

        userLineupDoc.set(data = userLineup, merge = true)

        cache[uid] = CachedValue(
            value = userLineup,
        )
    }
}
