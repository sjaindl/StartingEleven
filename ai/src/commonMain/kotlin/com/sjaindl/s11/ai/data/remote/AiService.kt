package com.sjaindl.s11.ai.data.remote

import com.sjaindl.s11.ai.data.remote.model.FlowiseResponse
import kotlinx.coroutines.flow.Flow

interface AiService {
    fun getCompletion(prompt: String, chatId: String?): Flow<FlowiseResponse>
}
