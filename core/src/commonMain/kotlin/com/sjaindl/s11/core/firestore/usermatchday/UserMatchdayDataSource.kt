package com.sjaindl.s11.core.firestore.usermatchday

import com.sjaindl.s11.core.CachedValue
import com.sjaindl.s11.core.firestore.FireStoreBaseDataSource
import com.sjaindl.s11.core.firestore.config.ConfigRepository
import com.sjaindl.s11.core.firestore.usermatchday.model.UserMatchDay
import dev.gitlive.firebase.firestore.DocumentSnapshot
import dev.gitlive.firebase.firestore.FirebaseFirestore
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

interface UserMatchDayDataSource {
    suspend fun getUserMatchDays(uid: String): List<UserMatchDay>
    suspend fun submitBet(uid: String, matchDay: String, homeScore: Int, awayScore: Int)
}

internal class UserMatchDayDataSourceImpl(
    firestore: FirebaseFirestore,
): FireStoreBaseDataSource<UserMatchDay>(firestore = firestore), UserMatchDayDataSource, KoinComponent {
    private val configRepository: ConfigRepository by inject()

    private var cache: MutableMap<String, CachedValue<List<UserMatchDay>>> = mutableMapOf()

    override val collectionPath: String = "users"

    override val mapper: (DocumentSnapshot) -> UserMatchDay = {
        it.data()
    }

    override suspend fun getUserMatchDays(uid: String): List<UserMatchDay> {
        val cachedValue = cache[uid]?.get()
        if (cachedValue != null) return cachedValue

        val userMatchDays = getDocumentRef(path = uid)
            .collection(collectionPath = "matchdays")
            .get().documents.mapNotNull { documentSnapshot ->
                if (documentSnapshot.exists) {
                    mapper(documentSnapshot).copy(matchDay = documentSnapshot.id)
                } else {
                    null
                }
            }.filter {
                it.matchDay.startsWith(prefix = configRepository.getConfig()?.season ?: it.matchDay)
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

        val userMatchDayDoc = userMatchDayDocRef.get()

        val docData = if (userMatchDayDoc.exists) {
            userMatchDayDoc.data<UserMatchDay>()
        } else {
            UserMatchDay(
                matchDay = matchDay,
                homeScore = homeScore,
                awayScore = awayScore,
            )
        }

        val newDocData = docData.copy(
            homeScore = homeScore,
            awayScore = awayScore,
        )

        userMatchDayDocRef.set(data = newDocData, merge = true)
    }
}
