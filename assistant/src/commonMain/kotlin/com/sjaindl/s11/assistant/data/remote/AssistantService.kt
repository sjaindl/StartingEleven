package com.sjaindl.s11.assistant.data.remote

import com.sjaindl.s11.assistant.data.remote.model.FlowiseResponse
import kotlinx.coroutines.flow.Flow

interface AssistantService {
    fun getCompletion(prompt: String, chatId: String?): Flow<FlowiseResponse>
}
