package com.sjaindl.s11.standings.model

import com.sjaindl.s11.core.firestore.user.model.User
import kotlinx.serialization.Serializable

@Serializable
data class UserWithPoints(
    val user: User,
    val points: Float,
    val pointsLastRound: Float,
    val betPoints: Int,
    val betPointsLastRound: Int,
)
