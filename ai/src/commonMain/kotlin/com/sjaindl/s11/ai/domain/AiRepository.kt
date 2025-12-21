package com.sjaindl.s11.ai.domain

import com.sjaindl.s11.ai.data.remote.model.FlowiseResponse
import kotlinx.coroutines.flow.Flow

interface AiRepository {
    fun getCompletion(prompt: String, chatId: String?): Flow<FlowiseResponse>
}
