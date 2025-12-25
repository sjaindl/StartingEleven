package com.sjaindl.s11.assistant.domain

import com.sjaindl.s11.assistant.data.remote.model.FlowiseResponse
import kotlinx.coroutines.flow.Flow

interface AssistantRepository {
    fun getCompletion(prompt: String, chatId: String?): Flow<FlowiseResponse>
}
