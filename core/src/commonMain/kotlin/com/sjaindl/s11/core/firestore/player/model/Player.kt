package com.sjaindl.s11.core.firestore.player.model

import kotlinx.serialization.Serializable

@Serializable
data class Player(
    val playerId: String,
    val name: String,
    val positions: List<Position>,
    val imageRef: String?,
    val downloadUrl: String?,
    val points: Map<String, Float>,
    val active: Boolean = true,
) {
    companion object {
        const val MISSING_PLAYER = "-1"
    }

    fun pointsOfSeason(season: String?) = points.filter {
        if (season == null) return@filter false
        it.key.startsWith(season)
    }
}
