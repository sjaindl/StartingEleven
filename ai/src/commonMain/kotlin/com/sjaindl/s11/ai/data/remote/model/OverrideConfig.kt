package com.sjaindl.s11.ai.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class OverrideConfig(
    val returnSourceDocuments: Boolean? = true,
)
