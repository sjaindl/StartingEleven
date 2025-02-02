package com.sjaindl.s11.standings.domain

import com.sjaindl.s11.core.firestore.config.ConfigRepository
import com.sjaindl.s11.core.firestore.player.PlayerRepository
import com.sjaindl.s11.core.firestore.player.model.Player
import com.sjaindl.s11.core.firestore.usermatchday.UserMatchDayRepository

class CalculatePointsForMatchDayLineupUseCase(
    private val configRepository: ConfigRepository,
    private val playerRepository: PlayerRepository,
    private val userMatchDayRepository: UserMatchDayRepository,
) {

    companion object {
        private const val REQUIRED_NUM_OF_PLAYERS = 11F
    }

    suspend fun calculate(
        uid: String,
        matchDay: String,
    ): Float {
        val season = configRepository.getConfig()?.season
        val userMatchDays = userMatchDayRepository.getUserMatchDays(uid = uid)
        val players = playerRepository.getPlayers(onlyActive = false)

        val lineupAtMatchDay = userMatchDays.find {
            it.matchDay == matchDay
        }

        var pointsForRound = 0F

        if (lineupAtMatchDay != null) {
            val goalkeeperPoints = pointsForPlayer(
                players = players,
                playerId = lineupAtMatchDay.goalkeeper,
                matchDay = matchDay,
                season = season,
            )
            pointsForRound += goalkeeperPoints

            lineupAtMatchDay.defenders.forEach { playerId ->
                val points = pointsForPlayer(players = players, playerId = playerId, matchDay = matchDay, season = season)
                pointsForRound += points
            }

            lineupAtMatchDay.midfielders.forEach { playerId ->
                val points = pointsForPlayer(players = players, playerId = playerId, matchDay = matchDay, season = season)
                pointsForRound += points
            }

            lineupAtMatchDay.attackers.forEach { playerId ->
                val points = pointsForPlayer(players = players, playerId = playerId, matchDay = matchDay, season = season)
                pointsForRound += points
            }

            val playersInFormation = (if (lineupAtMatchDay.goalkeeper != null) 1 else 0) +
                    lineupAtMatchDay.defenders.size +
                    lineupAtMatchDay.midfielders.size +
                    lineupAtMatchDay.attackers.size

            val penaltyForMissingPlayers = REQUIRED_NUM_OF_PLAYERS - playersInFormation
            pointsForRound -= penaltyForMissingPlayers
        } else {
            // Penalty points for missing lineup at matchday
            pointsForRound -= REQUIRED_NUM_OF_PLAYERS
        }

        return pointsForRound
    }

    private fun pointsForPlayer(players: List<Player>, playerId: String?, matchDay: String, season: String?): Float {
        if (playerId == null) return 0F

        val player = players.find {
            it.playerId == playerId
        } ?: return 0F

        return player.pointsOfSeason(season = season)[matchDay] ?: 0F
    }
}
