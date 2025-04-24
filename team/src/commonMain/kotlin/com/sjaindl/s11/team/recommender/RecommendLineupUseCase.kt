package com.sjaindl.s11.team.recommender

import com.sjaindl.s11.core.firestore.config.ConfigRepository
import com.sjaindl.s11.core.firestore.formations.FormationRepository
import com.sjaindl.s11.core.firestore.player.PlayerRepository
import com.sjaindl.s11.core.firestore.player.model.Player
import com.sjaindl.s11.core.firestore.player.model.Position
import io.github.aakira.napier.Napier
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class RecommendLineupUseCase: KoinComponent {
    private val tag = "RecommendLineupUseCase"

    private val configRepository: ConfigRepository by inject()
    private val formationRepository: FormationRepository by inject()
    private val playerRepository: PlayerRepository by inject()

    suspend fun determineLineupRecommendation(): RecommendationState {
        try {
            val season = configRepository.getConfig()?.season
            val players = playerRepository.getPlayers(onlyActive = true)
            val formations = formationRepository.getFormations()

            val bestGoalKeeper = players.filter {
                it.positions.contains(Position.Goalkeeper)
            }.maxBy { player ->
                player.pointsOfSeason(season = season).values.sum()
            }

            val takenPlayers = mutableSetOf(bestGoalKeeper)

            val defenders = filterAndSort(players = players, position = Position.Defender, season = season)
            val midfielders = filterAndSort(players = players, position = Position.Midfielder, season = season)
            val attackers = filterAndSort(players = players, position = Position.Attacker, season = season)

            var bestSum = 0.0f
            var best: MutableMap<Position, List<Player>> = mutableMapOf()

            formations.forEach { formation ->
                if (defenders.size < formation.defense || midfielders.size < formation.midfield || attackers.size < formation.attack) {
                    return@forEach
                }

                val chosenDefenders = defenders.filterNot {
                    takenPlayers.contains(it)
                }.take(formation.defense)
                takenPlayers.addAll(chosenDefenders)

                val chosenMidfielders = midfielders.filterNot {
                    takenPlayers.contains(it)
                }.take(formation.midfield)
                takenPlayers.addAll(chosenMidfielders)

                val chosenAttackers = attackers.filterNot {
                    takenPlayers.contains(it)
                }.take(formation.attack)

                takenPlayers.removeAll(chosenMidfielders + chosenDefenders)

                val sum = (chosenDefenders + chosenMidfielders + chosenAttackers).flatMap {
                    it.pointsOfSeason(season = season).values
                }.sum()

                if (sum > bestSum) {
                    bestSum = sum
                    best[Position.Defender] = chosenDefenders
                    best[Position.Midfielder] = chosenMidfielders
                    best[Position.Attacker] = chosenAttackers
                }
            }

            val bestDefenders = best[Position.Defender]
            val bestMidfielders = best[Position.Midfielder]
            val bestAttackers = best[Position.Attacker]

            return if (bestDefenders != null && bestMidfielders != null && bestAttackers != null) {
                RecommendationState.Recommendation(
                    goalkeeper = bestGoalKeeper,
                    defenders = bestDefenders,
                    midfielders = bestMidfielders,
                    attackers = bestAttackers,
                )
            } else {
                RecommendationState.NoRecommendation
            }
        } catch (exception: Exception) {
            val message = exception.message ?: exception.toString()
            Napier.e(message = message, throwable = exception, tag = tag)
            return RecommendationState.Error(message = message)
        }
    }

    private fun filterAndSort(players: List<Player>, position: Position, season: String?) =
        players.filter {
            it.positions.contains(position)
        }.sortedByDescending {
            it.pointsOfSeason(season = season).values.sum()
        }
}
