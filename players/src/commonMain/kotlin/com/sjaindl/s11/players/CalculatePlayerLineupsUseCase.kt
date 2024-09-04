package com.sjaindl.s11.players

import com.sjaindl.s11.core.firestore.user.UserRepository
import com.sjaindl.s11.core.firestore.userlineup.UserLineupRepository
import com.sjaindl.s11.core.firestore.usermatchday.UserMatchDayRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class CalculatePlayerLineupsUseCase: KoinComponent {

    private val userRepository: UserRepository by inject()
    private val userMatchDayRepository: UserMatchDayRepository by inject()
    private val userLineupRepository: UserLineupRepository by inject()

    suspend fun calculate(playerId: String): Int {
        val users = userRepository.getUsers()
        val lineupCount = users.count { user ->
            val lineup = userLineupRepository.getUserLineup(uid = user.uid)
            lineup.includesPlayer(playerId = playerId)
        }

        val userMatchDaysCount = users.sumOf { user ->
            val userMatchDays = userMatchDayRepository.getUserMatchDays(uid = user.uid)
            userMatchDays.count {
                it.includesPlayer(playerId = playerId)
            }
        }

        return lineupCount + userMatchDaysCount
    }
}
