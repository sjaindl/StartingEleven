package com.sjaindl.s11.core.firestore.usermatchday.model

import kotlinx.serialization.Serializable

@Serializable
data class UserMatchDay(
    val matchDay: String,
    val goalkeeper: String? = null,
    val defenders: List<String> = emptyList(),
    val midfielders: List<String> = emptyList(),
    val attackers: List<String> = emptyList(),
    val homeScore: Int? = null,
    val awayScore: Int? = null,
)
