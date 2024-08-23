package com.sjaindl.s11.core.firestore.matchday

import com.sjaindl.s11.core.firestore.matchday.model.MatchDay
import kotlinx.coroutines.flow.Flow
import org.koin.core.component.KoinComponent

interface MatchDayRepository {
    suspend fun getMatchDays(): List<MatchDay>
    fun getMatchDaysFlow(): Flow<List<MatchDay>>
}

internal class MatchDayRepositoryImpl(
    private val matchDayDataSource: MatchDayDataSource,
): MatchDayRepository, KoinComponent {
    override suspend fun getMatchDays() = matchDayDataSource.getMatchDays()

    override fun getMatchDaysFlow() = matchDayDataSource.getMatchDaysFlow()
}
