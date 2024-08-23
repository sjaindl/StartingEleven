package com.sjaindl.s11.core.firestore.matchday

import com.sjaindl.s11.core.CachedValue
import com.sjaindl.s11.core.firestore.FireStoreBaseDataSource
import com.sjaindl.s11.core.firestore.matchday.model.MatchDay
import dev.gitlive.firebase.firestore.DocumentSnapshot
import dev.gitlive.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import org.koin.core.component.KoinComponent

interface MatchDayDataSource {
    suspend fun getMatchDays(): List<MatchDay>
    fun getMatchDaysFlow(): Flow<List<MatchDay>>
}

internal class MatchDayDataSourceImpl(
    firestore: FirebaseFirestore,
): FireStoreBaseDataSource<MatchDay>(firestore = firestore), MatchDayDataSource, KoinComponent {
    private var cache: CachedValue<List<MatchDay>>? = null

    override val collectionPath: String = "matchDays"

    override val mapper: (DocumentSnapshot) -> MatchDay = {
        it.data<MatchDay>().copy(docId = it.id)
    }

    override suspend fun getMatchDays(): List<MatchDay> {
        val cachedValue = cache?.get()
        if (cachedValue != null) return cachedValue

        val matchDays = getCollection()
        cache = CachedValue(
            value = matchDays,
        )

        return matchDays
    }

    override fun getMatchDaysFlow() = getCollectionFlow()
}
