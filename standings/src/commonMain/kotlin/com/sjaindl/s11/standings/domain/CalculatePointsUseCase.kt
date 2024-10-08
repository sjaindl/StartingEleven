package com.sjaindl.s11.standings.domain

import com.sjaindl.s11.core.firestore.user.UserRepository
import com.sjaindl.s11.core.firestore.matchday.MatchDayRepository
import com.sjaindl.s11.standings.model.UserWithPoints

class CalculatePointsUseCase(
    private val userRepository: UserRepository,
    private val matchDayRepository: MatchDayRepository,
    private val calculatePointsForBetUseCase: CalculatePointsForBetUseCase,
    private val calculatePointsForMatchDayLineupUseCase: CalculatePointsForMatchDayLineupUseCase,
) {

    suspend fun calculate(onCalculateUser: (String) -> Unit): List<UserWithPoints> {
        val users = userRepository.getUsers()
        val matchDays = matchDayRepository.getMatchDays()

        return users.map { user ->
            onCalculateUser(user.userName)

            var totalPoints = 0F
            var pointsForRound = 0F
            var totalBetPoints = 0
            var betPointsForRound = 0

            matchDays.forEach { matchday ->

                // Points for matchday lineup
                pointsForRound = calculatePointsForMatchDayLineupUseCase.calculate(
                    uid = user.uid,
                    matchDay = matchday.docId
                )
                totalPoints += pointsForRound

                // Additional points for matchday bet
                val betPoints = calculatePointsForBetUseCase.calculate(
                    uid = user.uid,
                    matchDay = matchday.docId,
                )

                totalPoints += betPoints
                pointsForRound += betPoints
                totalBetPoints += betPoints
                betPointsForRound = betPoints
            }

            UserWithPoints(
                user = user,
                points = totalPoints,
                pointsLastRound = pointsForRound,
                betPoints = totalBetPoints,
                betPointsLastRound = betPointsForRound,
            )
        }
    }
}
