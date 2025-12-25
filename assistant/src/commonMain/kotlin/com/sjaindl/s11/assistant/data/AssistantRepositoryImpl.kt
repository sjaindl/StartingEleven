package com.sjaindl.s11.assistant.data

import com.sjaindl.s11.assistant.data.remote.AssistantService
import com.sjaindl.s11.assistant.data.remote.model.FlowiseResponse
import com.sjaindl.s11.assistant.domain.AssistantRepository
import kotlinx.coroutines.flow.Flow

class AssistantRepositoryImpl(
    private val assistantService: AssistantService
) : AssistantRepository {

    override fun getCompletion(prompt: String, chatId: String?): Flow<FlowiseResponse> {
        return assistantService.getCompletion(prompt, chatId)
    }
}
