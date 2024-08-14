package com.sjaindl.s11.core.player.model

import kotlinx.serialization.Serializable

@Serializable
data class Player(
    val playerId: String,
    val name: String,
    val position: Position,
    val imageRef: String?,
    val downloadUrl: String?,
    val points: Map<String, Int>,
)
