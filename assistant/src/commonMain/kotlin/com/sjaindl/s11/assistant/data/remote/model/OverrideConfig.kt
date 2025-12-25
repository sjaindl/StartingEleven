package com.sjaindl.s11.assistant.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class OverrideConfig(
    val returnSourceDocuments: Boolean? = true,
)
