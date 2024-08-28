package com.sjaindl.s11.core.firestore.usermatchday

import com.sjaindl.s11.core.firestore.usermatchday.model.UserMatchDay
import org.koin.core.component.KoinComponent

interface UserMatchDayRepository {
    suspend fun getUserMatchDays(uid: String): List<UserMatchDay>
    suspend fun submitBet(uid: String, matchDay: String, homeScore: Int, awayScore: Int)
}

internal class UserMatchDayRepositoryImpl(
    private val userMatchDayDataSource: UserMatchDayDataSource,
): UserMatchDayRepository, KoinComponent {

    override suspend fun getUserMatchDays(uid: String): List<UserMatchDay> {
        return userMatchDayDataSource.getUserMatchDays(uid = uid)
    }

    override suspend fun submitBet(uid: String, matchDay: String, homeScore: Int, awayScore: Int) {
        return userMatchDayDataSource.submitBet(
            uid = uid,
            matchDay = matchDay,
            homeScore = homeScore,
            awayScore = awayScore,
        )
    }
}
