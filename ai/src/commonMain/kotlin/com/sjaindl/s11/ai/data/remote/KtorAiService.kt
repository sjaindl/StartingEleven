package com.sjaindl.s11.ai.data.remote

import com.sjaindl.s11.ai.data.remote.model.FlowiseRequest
import com.sjaindl.s11.ai.data.remote.model.FlowiseResponse
import com.sjaindl.s11.ai.data.remote.model.OverrideConfig
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsChannel
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import io.ktor.utils.io.readUTF8Line
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.json.Json
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class KtorAiService : AiService, KoinComponent {

    private val json: Json by inject()

    private val client: HttpClient by inject()

    override fun getCompletion(prompt: String, chatId: String?): Flow<FlowiseResponse> = flow {
        val response = client.post("https://www.hvtdpstainz.at/flowise/api/v1/prediction/3d0fc477-d898-4a7d-8474-67348965eb28") {
            contentType(ContentType.Application.Json)
            setBody(
                FlowiseRequest(
                    question = prompt,
                    chatId = chatId,
                    overrideConfig = OverrideConfig()
                )
            )
        }

        val channel = response.bodyAsChannel()
        while (!channel.isClosedForRead) {
            val line = channel.readUTF8Line()
            if (line?.startsWith("data:") == true) {
                val data = line.removePrefix("data:").trim()
                val flowiseResponse = json.decodeFromString<FlowiseResponse>(data)
                emit(flowiseResponse)
            }
        }
    }
}
