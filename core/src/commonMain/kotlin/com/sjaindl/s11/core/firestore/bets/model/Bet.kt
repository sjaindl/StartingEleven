package com.sjaindl.s11.core.firestore.bets.model

import kotlinx.serialization.Serializable

@Serializable
data class Bet(
    val id: String,
    val away: String,
    val home: String,
    val resultScoreAway: Int?,
    val resultScoreHome: Int?,
)
