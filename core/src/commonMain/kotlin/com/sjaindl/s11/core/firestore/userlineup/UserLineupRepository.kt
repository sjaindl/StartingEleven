package com.sjaindl.s11.core.firestore.userlineup

import com.sjaindl.s11.core.firestore.userlineup.model.LineupData
import org.koin.core.component.KoinComponent

interface UserLineupRepository {
    suspend fun getUserLineup(uid: String): LineupData
    suspend fun setUserLineup(uid: String, userLineup: LineupData)
}

internal class UserLineupRepositoryImpl(
    private val userLineupDataSource: UserLineupDataSource,
): UserLineupRepository, KoinComponent {

    override suspend fun getUserLineup(uid: String): LineupData {
        return userLineupDataSource.getUserLineup(uid = uid)
    }

    override suspend fun setUserLineup(uid: String, userLineup: LineupData) {
        return userLineupDataSource.setUserLineup(uid = uid, userLineup = userLineup)
    }
}
