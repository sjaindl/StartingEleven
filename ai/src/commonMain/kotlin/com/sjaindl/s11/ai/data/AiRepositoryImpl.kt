package com.sjaindl.s11.ai.data

import com.sjaindl.s11.ai.data.remote.AiService
import com.sjaindl.s11.ai.data.remote.model.FlowiseResponse
import com.sjaindl.s11.ai.domain.AiRepository
import kotlinx.coroutines.flow.Flow

class AiRepositoryImpl(
    private val aiService: AiService
) : AiRepository {

    override fun getCompletion(prompt: String, chatId: String?): Flow<FlowiseResponse> {
        return aiService.getCompletion(prompt, chatId)
    }
}
