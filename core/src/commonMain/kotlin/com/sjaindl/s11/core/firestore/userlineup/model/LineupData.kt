package com.sjaindl.s11.core.firestore.userlineup.model

import kotlinx.serialization.Serializable

@Serializable
data class LineupData(
    val goalkeeper: String? = null,
    val defenders: List<String> = emptyList(),
    val midfielders: List<String> = emptyList(),
    val attackers: List<String> = emptyList(),
)
