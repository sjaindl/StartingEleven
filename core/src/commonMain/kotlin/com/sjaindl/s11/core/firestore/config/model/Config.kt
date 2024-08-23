package com.sjaindl.s11.core.firestore.config.model

import kotlinx.serialization.Serializable

@Serializable
data class Config(
    val bets: Boolean,
    val freeze: Boolean,
)
