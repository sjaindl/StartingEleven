package com.sjaindl.s11.core.firestore.bets

import com.sjaindl.s11.core.CachedValue
import com.sjaindl.s11.core.firestore.FireStoreBaseDataSource
import com.sjaindl.s11.core.firestore.bets.model.Bet
import dev.gitlive.firebase.firestore.DocumentSnapshot
import dev.gitlive.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import org.koin.core.component.KoinComponent

interface BetsDataSource {
    suspend fun getBets(): List<Bet>
    fun getBetsFlow(): Flow<List<Bet>>
}

internal class BetsDataSourceImpl(
    firestore: FirebaseFirestore,
): FireStoreBaseDataSource<Bet>(firestore = firestore), BetsDataSource, KoinComponent {
    private var cache: CachedValue<List<Bet>>? = null

    override val collectionPath: String = "bets"

    override val mapper: (DocumentSnapshot) -> Bet = {
        it.data()
    }

    override suspend fun getBets(): List<Bet> {
        val cachedValue = cache?.get()
        if (!cachedValue.isNullOrEmpty()) return cachedValue

        val bets = getCollection()
        cache = CachedValue(
            value = bets,
        )
        return bets
    }

    override fun getBetsFlow() = getCollectionFlow()
}
