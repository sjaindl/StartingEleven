package com.sjaindl.s11.core.firestore.news.model

import kotlinx.serialization.Serializable

@Serializable
data class News(
    val text: String,
)
