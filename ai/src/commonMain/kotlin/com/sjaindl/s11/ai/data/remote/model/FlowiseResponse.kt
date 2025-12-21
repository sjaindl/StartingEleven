package com.sjaindl.s11.ai.data.remote.model

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator
import kotlinx.serialization.json.JsonElement

@OptIn(ExperimentalSerializationApi::class)
@Serializable
@JsonClassDiscriminator("event")
sealed class FlowiseResponse {
    @Serializable
    @SerialName("start")
    data object Start : FlowiseResponse()

    @Serializable
    @SerialName("token")
    data class Token(val data: String) : FlowiseResponse()

    @Serializable
    @SerialName("usedTools")
    data class UsedTools(val data: List<Tool>) : FlowiseResponse()

    @Serializable
    @SerialName("sourceDocuments")
    data class SourceDocuments(val data: List<SourceDocument>) : FlowiseResponse()

    @Serializable
    @SerialName("metadata")
    data class Metadata(val data: ChatMetadata) : FlowiseResponse()

    @Serializable
    @SerialName("end")
    data object End : FlowiseResponse()

    @Serializable
    @SerialName("error")
    data class Error(val data: String) : FlowiseResponse()
}

@Serializable
data class Tool(
    val tool: String,
    val toolInput: JsonElement,
    val toolOutput: String,
)

@Serializable
data class ChatMetadata(
    val chatId: String,
    val chatMessageId: String,
    val question: String,
    val sessionId: String,
    val memoryType: String,
)

@Serializable
data class SourceDocument(
    val pageContent: String,
    val metadata: JsonElement,
)
