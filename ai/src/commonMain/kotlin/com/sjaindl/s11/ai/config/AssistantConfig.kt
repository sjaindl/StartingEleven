package com.sjaindl.s11.ai.config

import org.jetbrains.compose.resources.StringResource
import startingeleven.ai.generated.resources.Res
import startingeleven.ai.generated.resources.prompt_label

data class AssistantConfig(
    val provider: Provider,
    val appBarTitle: StringResource,
    val promptPlaceholder: StringResource = Res.string.prompt_label,
    val welcomeMessage: StringResource? = null,
    val sampleQuestions: List<StringResource> = emptyList(),
    val showTools: Boolean = true,
    val showSourceDocuments: Boolean = true,
    val displayResetAction: Boolean = true,
    val streamingDelayMilliseconds: Long = 4L,
    val messageCharLimit: Int = 250,
)

sealed class Provider {
    data class Flowise(val baseUrl: String) : Provider()
}
