package com.sjaindl.s11.ai.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sjaindl.s11.ai.data.remote.model.FlowiseResponse
import com.sjaindl.s11.ai.data.remote.model.SourceDocument
import com.sjaindl.s11.ai.data.remote.model.Tool
import com.sjaindl.s11.ai.domain.usecase.GetAiCompletionUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ChatViewModel(
    private val getAiCompletionUseCase: GetAiCompletionUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()

    private var chatId: String? = null

    fun sendPrompt(prompt: String) {
        viewModelScope.launch {
            // Add user message and set loading state before starting the flow
            _uiState.update {
                it.copy(
                    isLoading = true,
                    error = null,
                    messages = it.messages + ChatMessage(
                        text = prompt,
                        isFromUser = true,
                    )
                )
            }

            getAiCompletionUseCase(prompt = prompt, chatId = chatId)
                .onEach { response ->
                    _uiState.update { currentState ->
                        when (response) {
                            is FlowiseResponse.Token -> {
                                val lastMessage = currentState.messages.lastOrNull()

                                // If last message is from AI, append token to it
                                if (lastMessage != null && !lastMessage.isFromUser) {
                                    val updatedMessages = currentState.messages.toMutableList()
                                    updatedMessages[updatedMessages.lastIndex] =
                                        lastMessage.copy(text = lastMessage.text + response.data)
                                    currentState.copy(messages = updatedMessages)
                                } else {
                                    // Otherwise, add a new AI message
                                    currentState.copy(
                                        isLoading = false,
                                        messages = currentState.messages + ChatMessage(
                                            text = response.data,
                                            isFromUser = false,
                                            isTyping = true,
                                        )
                                    )
                                }
                            }

                            is FlowiseResponse.Metadata -> {
                                chatId = response.data.chatId
                                currentState
                            }

                            is FlowiseResponse.UsedTools -> {
                                val lastMessage = currentState.messages.lastOrNull()
                                if (lastMessage != null && !lastMessage.isFromUser) {
                                    val updatedMessages = currentState.messages.toMutableList()
                                    updatedMessages[updatedMessages.lastIndex] =
                                        lastMessage.copy(usedTools = response.data)
                                    currentState.copy(messages = updatedMessages)
                                } else {
                                    currentState
                                }
                            }

                            is FlowiseResponse.SourceDocuments -> {
                                val lastMessage = currentState.messages.lastOrNull()
                                if (lastMessage != null && !lastMessage.isFromUser) {
                                    val updatedMessages = currentState.messages.toMutableList()
                                    updatedMessages[updatedMessages.lastIndex] =
                                        lastMessage.copy(sourceDocuments = response.data)
                                    currentState.copy(messages = updatedMessages)
                                } else {
                                    currentState
                                }
                            }

                            is FlowiseResponse.Error -> {
                                currentState.copy(error = response.data)
                            }

                            else -> currentState
                        }
                    }

                    delay(4)
                }
                .catch { e ->
                    _uiState.update { it.copy(error = e.message) }
                }
                .onCompletion {
                    _uiState.update { currentState ->
                        val messages = currentState.messages.toMutableList()
                        val lastAiMessageIndex = messages.indexOfLast { !it.isFromUser }

                        if (lastAiMessageIndex != -1) {
                            val lastAiMessage = messages[lastAiMessageIndex]
                            messages[lastAiMessageIndex] = lastAiMessage.copy(isTyping = false)
                        }

                        currentState.copy(
                            isLoading = false,
                            messages = messages,
                        )
                    }
                }
                .collect()
        }
    }
}

data class ChatUiState(
    val isLoading: Boolean = false,
    val messages: List<ChatMessage> = emptyList(),
    val error: String? = null,
)

data class ChatMessage(
    val text: String,
    val isFromUser: Boolean,
    val usedTools: List<Tool>? = null,
    val sourceDocuments: List<SourceDocument>? = null,
    val isTyping: Boolean = false,
)
