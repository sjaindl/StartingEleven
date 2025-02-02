package com.sjaindl.s11.core.firestore.bets

import com.sjaindl.s11.core.CachedValue
import com.sjaindl.s11.core.firestore.FireStoreBaseDataSource
import com.sjaindl.s11.core.firestore.bets.model.Bet
import com.sjaindl.s11.core.firestore.config.ConfigRepository
import dev.gitlive.firebase.firestore.DocumentSnapshot
import dev.gitlive.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

interface BetsDataSource {
    suspend fun getBets(): List<Bet>
    fun getBetsFlow(): Flow<List<Bet>>
}

internal class BetsDataSourceImpl(
    firestore: FirebaseFirestore,
): FireStoreBaseDataSource<Bet>(firestore = firestore), BetsDataSource, KoinComponent {

    private val configRepository: ConfigRepository by inject()

    private var cache: CachedValue<List<Bet>>? = null

    override val collectionPath: String = "bets"

    override val mapper: (DocumentSnapshot) -> Bet = {
        it.data()
    }

    override suspend fun getBets(): List<Bet> {
        val cachedValue = cache?.get()
        if (!cachedValue.isNullOrEmpty()) return cachedValue

        val bets = getCollection().filter {
            it.id.substringBefore("_") == configRepository.getConfig()?.season
        }.sortedBy {
            it.id.substringAfter(delimiter = "_").toIntOrNull()
        }

        cache = CachedValue(
            value = bets,
        )
        return bets
    }

    override fun getBetsFlow() = getCollectionFlow().map {
        it.filter { bet ->
            bet.id.substringBefore("_") == configRepository.getConfig()?.season
        }
    }
}
