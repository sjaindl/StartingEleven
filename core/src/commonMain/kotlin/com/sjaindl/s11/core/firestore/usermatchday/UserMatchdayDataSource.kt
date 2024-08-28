package com.sjaindl.s11.core.firestore.usermatchday

import com.sjaindl.s11.core.CachedValue
import com.sjaindl.s11.core.firestore.FireStoreBaseDataSource
import com.sjaindl.s11.core.firestore.usermatchday.model.UserMatchDay
import dev.gitlive.firebase.firestore.DocumentSnapshot
import dev.gitlive.firebase.firestore.FirebaseFirestore
import org.koin.core.component.KoinComponent

interface UserMatchDayDataSource {
    suspend fun getUserMatchDays(uid: String): List<UserMatchDay>
    suspend fun submitBet(uid: String, matchDay: String, homeScore: Int, awayScore: Int)
}

internal class UserMatchDayDataSourceImpl(
    firestore: FirebaseFirestore,
): FireStoreBaseDataSource<UserMatchDay>(firestore = firestore), UserMatchDayDataSource, KoinComponent {
    private var cache: MutableMap<String, CachedValue<List<UserMatchDay>>> = mutableMapOf()

    override val collectionPath: String = "users"

    override val mapper: (DocumentSnapshot) -> UserMatchDay = {
        it.data()
    }

    override suspend fun getUserMatchDays(uid: String): List<UserMatchDay> {
        val cachedValue = cache[uid]?.get()
        if (!cachedValue.isNullOrEmpty()) return cachedValue

        val userMatchDays = getDocumentRef(path = uid)
            .collection(collectionPath = "matchdays")
            .get().documents.map {
                mapper(it).copy(matchDay = it.id)
            }

        cache[uid] = CachedValue(
            value = userMatchDays,
        )

        return userMatchDays
    }

    override suspend fun submitBet(uid: String, matchDay: String, homeScore: Int, awayScore: Int) {
        val userMatchDayDocRef = getDocumentRef(path = uid)
            .collection(collectionPath = "matchdays")
            .document(documentPath = matchDay)

        val docData = userMatchDayDocRef.get().data<UserMatchDay>()
        val newDocData = docData.copy(
            homeScore = homeScore,
            awayScore = awayScore,
        )

        userMatchDayDocRef.set(data = newDocData, merge = true)
    }
}
