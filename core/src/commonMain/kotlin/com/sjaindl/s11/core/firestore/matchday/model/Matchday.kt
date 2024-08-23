package com.sjaindl.s11.core.firestore.matchday.model

import kotlinx.serialization.Serializable

@Serializable
data class MatchDay(
    val id: Int,
    val docId: String,
    val opponent: String?,
)
