package com.sjaindl.s11.core.firestore.formations.model

import kotlinx.serialization.Serializable

@Serializable
data class Formation(
    val formation: String,
    val defense: Int,
    val midfield: Int,
    val attack: Int,
)
