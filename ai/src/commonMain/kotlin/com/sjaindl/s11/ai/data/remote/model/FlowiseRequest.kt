package com.sjaindl.s11.ai.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class FlowiseRequest(
    val question: String,
    val chatId: String? = null,
    val overrideConfig: OverrideConfig? = null,
    val streaming: Boolean? = true,
)
