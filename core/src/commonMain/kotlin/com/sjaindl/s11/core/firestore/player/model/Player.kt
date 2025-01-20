package com.sjaindl.s11.core.firestore.player.model

import kotlinx.serialization.Serializable

@Serializable
data class Player(
    val playerId: String,
    val name: String,
    val position: Position,
    val imageRef: String?,
    val downloadUrl: String?,
    val points: Map<String, Float>,
    val active: Boolean = true,
) {
    companion object {
        const val MISSING_PLAYER = "-1"
    }
}
