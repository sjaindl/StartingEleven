package com.sjaindl.s11.firestore.faq.model

import kotlinx.serialization.Serializable

@Serializable
data class Faq(
    val answer: String,
    val question: String,
)
