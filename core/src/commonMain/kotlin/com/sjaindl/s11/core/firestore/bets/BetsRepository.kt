package com.sjaindl.s11.core.firestore.bets

import com.sjaindl.s11.core.firestore.bets.model.Bet
import kotlinx.coroutines.flow.Flow

interface BetsRepository {
    suspend fun getBets(): List<Bet>
    fun getBetsFlow(): Flow<List<Bet>>
}

class BetsRepositoryImpl(
    private val betsDataSource: BetsDataSource,
): BetsRepository {
    override suspend fun getBets() = betsDataSource.getBets()

    override fun getBetsFlow() = betsDataSource.getBetsFlow()
}
