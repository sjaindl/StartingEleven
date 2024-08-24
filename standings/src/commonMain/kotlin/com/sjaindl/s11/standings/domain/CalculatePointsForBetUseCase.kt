package com.sjaindl.s11.standings.domain

import com.sjaindl.s11.core.firestore.bets.BetsRepository
import com.sjaindl.s11.core.firestore.usermatchday.UserMatchDayRepository
import kotlin.Int
import kotlin.String

private enum class Result {
    Win,
    Draw,
    Loss,
}

class CalculatePointsForBetUseCase(
    private val userMatchDayRepository: UserMatchDayRepository,
    private val betsRepository: BetsRepository,
) {

    companion object {
        const val POINTS_EXACT_BET = 5
        const val POINTS_FOR_CORRECT_TENDENCY = 3
    }

    suspend fun calculate(uid: String, matchDay: String): Int {
        val betForMatchDay = betsRepository.getBets().find {
            it.id == matchDay
        } ?: return 0

        val resultScoreHome = betForMatchDay.resultScoreHome ?: return 0
        val resultScoreAway = betForMatchDay.resultScoreAway ?: return 0

        val userMatchDay = userMatchDayRepository.getUserMatchDays(uid = uid).find {
            it.matchDay == matchDay
        } ?: return 0

        val userBetHome = userMatchDay.homeScore ?: return 0
        val userBetAway = userMatchDay.awayScore ?: return 0

        val isExactBet = resultScoreHome == userBetHome && resultScoreAway == userBetAway

        val result = if (resultScoreHome > resultScoreAway) {
            Result.Win
        } else if (resultScoreHome == resultScoreAway) {
            Result.Draw
        } else {
            Result.Loss
        }

        val bet = if (userBetHome > userBetAway) {
            Result.Win
        } else if (userBetHome == userBetAway) {
            Result.Draw
        } else {
            Result.Loss
        }

        return if (isExactBet) {
            POINTS_EXACT_BET
        } else if (result === bet) {
            POINTS_FOR_CORRECT_TENDENCY
        } else {
            0
        }
    }
}
