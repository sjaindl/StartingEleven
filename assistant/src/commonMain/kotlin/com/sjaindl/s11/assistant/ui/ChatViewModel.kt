package com.sjaindl.s11.assistant.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sjaindl.s11.assistant.config.AssistantConfig
import com.sjaindl.s11.assistant.data.ChatMessageDataSource
import com.sjaindl.s11.assistant.data.remote.model.FlowiseResponse
import com.sjaindl.s11.assistant.data.remote.model.SourceDocument
import com.sjaindl.s11.assistant.data.remote.model.Tool
import com.sjaindl.s11.assistant.domain.usecase.GetAssistantCompletionUseCase
import com.sjaindl.s11.core.util.generateUUID
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ChatViewModel(
    private val getAssistantCompletionUseCase: GetAssistantCompletionUseCase,
    private val chatMessageDataSource: ChatMessageDataSource,
    private val config: AssistantConfig,
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState = _uiState.asStateFlow()

    private var chatId: String = generateUUID()

    init {
        viewModelScope.launch {
            _uiState.update {
                it.copy(messages = chatMessageDataSource.getAll())
            }
        }
    }

    fun sendPrompt(prompt: String) {
        viewModelScope.launch {
            val userMessage = ChatMessage(text = prompt, isFromUser = true)
            val userMessageId = chatMessageDataSource.insert(userMessage)
            _uiState.update {
                it.copy(
                    isLoading = true,
                    error = null,
                    messages = it.messages + userMessage.copy(id = userMessageId)
                )
            }

            var currentAiMessageId: Long? = null

            getAssistantCompletionUseCase(prompt = prompt, chatId = chatId)
                .onEach { response ->
                    _uiState.update { currentState ->
                        when (response) {
                            is FlowiseResponse.Token -> {
                                if (currentAiMessageId == null) {
                                    val aiMessage = ChatMessage(
                                        text = response.data,
                                        isFromUser = false,
                                        isTyping = true
                                    )
                                    val newId = chatMessageDataSource.insert(aiMessage)
                                    currentAiMessageId = newId
                                    currentState.copy(
                                        isLoading = false,
                                        messages = currentState.messages + aiMessage.copy(id = newId)
                                    )
                                } else {
                                    val index = currentState.messages.indexOfFirst { it.id == currentAiMessageId }
                                    if (index != -1) {
                                        val updatedMessages = currentState.messages.toMutableList()
                                        val currentMessage = updatedMessages[index]
                                        updatedMessages[index] = currentMessage.copy(text = currentMessage.text + response.data)
                                        currentState.copy(messages = updatedMessages)
                                    } else {
                                        currentState
                                    }
                                }
                            }

                            is FlowiseResponse.Metadata -> {
                                chatId = response.data.chatId
                                currentState
                            }

                            is FlowiseResponse.UsedTools -> {
                                if (config.showTools) {
                                    val index = currentState.messages.indexOfFirst { it.id == currentAiMessageId }
                                    if (index != -1) {
                                        val updatedMessages = currentState.messages.toMutableList()
                                        val currentMessage = updatedMessages[index]
                                        updatedMessages[index] = currentMessage.copy(usedTools = response.data)
                                        currentState.copy(messages = updatedMessages)
                                    } else {
                                        currentState
                                    }
                                } else {
                                    currentState
                                }
                            }

                            is FlowiseResponse.SourceDocuments -> {
                                if (config.showSourceDocuments) {
                                    val index = currentState.messages.indexOfFirst { it.id == currentAiMessageId }
                                    if (index != -1) {
                                        val updatedMessages = currentState.messages.toMutableList()
                                        val currentMessage = updatedMessages[index]
                                        updatedMessages[index] = currentMessage.copy(sourceDocuments = response.data)
                                        currentState.copy(messages = updatedMessages)
                                    } else {
                                        currentState
                                    }
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
                    delay(config.streamingDelayMilliseconds)
                }
                .catch { e ->
                    _uiState.update { it.copy(error = e.message, isLoading = false) }
                }
                .onCompletion {
                    currentAiMessageId?.let { id ->
                        val finalMessage = _uiState.value.messages.find { it.id == id }
                        if (finalMessage != null) {
                            val messageToUpdate = finalMessage.copy(isTyping = false)
                            chatMessageDataSource.update(id, messageToUpdate)
                            _uiState.update { currentState ->
                                val index = currentState.messages.indexOfFirst { it.id == id }
                                if (index != -1) {
                                    val updatedMessages = currentState.messages.toMutableList()
                                    updatedMessages[index] = messageToUpdate
                                    currentState.copy(isLoading = false, messages = updatedMessages)
                                } else {
                                     currentState.copy(isLoading = false)
                                }
                            }
                        } else {
                            _uiState.update { it.copy(isLoading = false) }
                        }
                    } ?: _uiState.update { it.copy(isLoading = false) }
                }
                .collect()
        }
    }

    fun resetChat() {
        viewModelScope.launch {
            chatMessageDataSource.clear()
            _uiState.update {
                it.copy(messages = emptyList())
            }
        }
    }
}

data class ChatUiState(
    val isLoading: Boolean = false,
    val messages: List<ChatMessage> = emptyList(),
    val error: String? = null,
)

data class ChatMessage(
    val id: Long? = null,
    val text: String,
    val isFromUser: Boolean,
    val usedTools: List<Tool>? = null,
    val sourceDocuments: List<SourceDocument>? = null,
    val isTyping: Boolean = false,
)
