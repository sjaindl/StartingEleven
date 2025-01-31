package com.sjaindl.s11.team.recommender

import com.sjaindl.s11.core.firestore.formations.FormationRepository
import com.sjaindl.s11.core.firestore.player.PlayerRepository
import com.sjaindl.s11.core.firestore.player.model.Player
import com.sjaindl.s11.core.firestore.player.model.Position
import io.github.aakira.napier.Napier
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class RecommendLineupUseCase: KoinComponent {
    private val tag = "RecommendLineupUseCase"

    private val formationRepository: FormationRepository by inject()
    private val playerRepository: PlayerRepository by inject()

    suspend fun determineLineupRecommendation(): RecommendationState {
        try {
            val players = playerRepository.getPlayers(onlyActive = true)
            val formations = formationRepository.getFormations()

            val bestGoalKeeper = players.filter {
                it.position == Position.Goalkeeper
            }.maxBy {
                it.points.values.sum()
            }

            val defenders = filterAndSort(players = players, position = Position.Defender)
            val midfielders = filterAndSort(players = players, position = Position.Midfielder)
            val attackers = filterAndSort(players = players, position = Position.Attacker)

            val bestFormation = formations.maxByOrNull { formation ->
                if (defenders.size < formation.defense || midfielders.size < formation.midfield || attackers.size < formation.attack) {
                    return@maxByOrNull 0f
                }

                defenders.take(formation.defense).flatMap {
                    it.points.values
                }.sum() + midfielders.take(formation.midfield).flatMap {
                    it.points.values
                }.sum() + attackers.take(formation.attack).flatMap {
                    it.points.values
                }.sum()
            }

            if (bestFormation != null) {
                return RecommendationState.Recommendation(
                    goalkeeper = bestGoalKeeper,
                    defenders = defenders.take(bestFormation.defense),
                    midfielders = midfielders.take(bestFormation.midfield),
                    attackers = attackers.take(bestFormation.attack),
                )
            } else {
                return RecommendationState.NoRecommendation
            }
        } catch (exception: Exception) {
            val message = exception.message ?: exception.toString()
            Napier.e(message = message, throwable = exception, tag = tag)
            return RecommendationState.Error(message = message)
        }
    }

    private fun filterAndSort(players: List<Player>, position: Position) =
        players.filter {
            it.position == position
        }.sortedByDescending {
            it.points.values.sum()
        }
}
