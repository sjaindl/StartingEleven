package com.sjaindl.s11.ai.domain.usecase

import com.sjaindl.s11.ai.data.remote.model.FlowiseResponse
import com.sjaindl.s11.ai.domain.AiRepository
import kotlinx.coroutines.flow.Flow

class GetAiCompletionUseCase(private val repository: AiRepository) {
    operator fun invoke(prompt: String, chatId: String?): Flow<FlowiseResponse> {
        return repository.getCompletion(prompt, chatId)
    }
}
