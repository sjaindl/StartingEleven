package com.sjaindl.s11.core.firestore.userlineup.model

import kotlinx.serialization.Serializable

@Serializable
data class LineupData(
    val goalkeeper: String? = null,
    val defenders: List<String> = emptyList(),
    val midfielders: List<String> = emptyList(),
    val attackers: List<String> = emptyList(),
) {
    val size: Int
        get() = defenders.size + midfielders.size + attackers.size + if (goalkeeper != null) 1 else 0

    val isComplete = size == 11

    val allLinedUpPlayers: List<String>
        get() = (defenders + midfielders + attackers).toMutableList().apply {
            goalkeeper?.let {
              add(it)
            }
        }

    fun includesPlayer(playerId: String): Boolean {
        return goalkeeper == playerId
                || defenders.contains(element = playerId)
                || midfielders.contains(element = playerId)
                || attackers.contains(element = playerId)
    }
}
