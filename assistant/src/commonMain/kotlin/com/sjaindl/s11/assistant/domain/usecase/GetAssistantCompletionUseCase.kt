package com.sjaindl.s11.assistant.domain.usecase

import com.sjaindl.s11.assistant.data.remote.model.FlowiseResponse
import com.sjaindl.s11.assistant.domain.AssistantRepository
import kotlinx.coroutines.flow.Flow

class GetAssistantCompletionUseCase(private val repository: AssistantRepository) {
    operator fun invoke(prompt: String, chatId: String?): Flow<FlowiseResponse> {
        return repository.getCompletion(prompt, chatId)
    }
}
