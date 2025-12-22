package com.sjaindl.s11.ai.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sjaindl.s11.ai.data.remote.model.FlowiseResponse
import com.sjaindl.s11.ai.data.remote.model.SourceDocument
import com.sjaindl.s11.ai.data.remote.model.Tool
import com.sjaindl.s11.ai.domain.usecase.GetAiCompletionUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class ChatViewModel(
    private val getAiCompletionUseCase: GetAiCompletionUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()

    private var chatId: String? = null

    fun sendPrompt(prompt: String) {
        viewModelScope.launch {
            getAiCompletionUseCase(prompt = prompt, chatId = chatId)
                .onStart {
                    _uiState.value = _uiState.value.copy(
                        isLoading = true,
                        error = null,
                        messages = _uiState.value.messages + ChatMessage(
                            text = prompt,
                            isFromUser = true
                        )
                    )
                }
                .onEach { response ->
                    when (response) {
                        is FlowiseResponse.Token -> {
                            val lastMessage = _uiState.value.messages.lastOrNull()

                            if (lastMessage != null && !lastMessage.isFromUser) {
                                val updatedMessages = _uiState.value.messages.toMutableList()
                                updatedMessages[updatedMessages.lastIndex] =
                                    lastMessage.copy(text = lastMessage.text + response.data)
                                _uiState.value = _uiState.value.copy(messages = updatedMessages)
                            } else {
                                _uiState.value = _uiState.value.copy(
                                    messages = _uiState.value.messages + ChatMessage(
                                        text = response.data,
                                        isFromUser = false,
                                    )
                                )
                            }
                        }

                        is FlowiseResponse.Metadata -> {
                            chatId = response.data.chatId
                        }

                        is FlowiseResponse.UsedTools -> {
                            val lastMessage = _uiState.value.messages.lastOrNull()
                            if (lastMessage != null && !lastMessage.isFromUser) {
                                val updatedMessages = _uiState.value.messages.toMutableList()
                                updatedMessages[updatedMessages.lastIndex] =
                                    lastMessage.copy(usedTools = response.data)
                                _uiState.value = _uiState.value.copy(messages = updatedMessages)
                            }
                        }

                        is FlowiseResponse.SourceDocuments -> {
                            val lastMessage = _uiState.value.messages.lastOrNull()
                            if (lastMessage != null && !lastMessage.isFromUser) {
                                val updatedMessages = _uiState.value.messages.toMutableList()
                                updatedMessages[updatedMessages.lastIndex] =
                                    lastMessage.copy(sourceDocuments = response.data)
                                _uiState.value = _uiState.value.copy(messages = updatedMessages)
                            }
                        }

                        is FlowiseResponse.Error -> {
                            _uiState.value = _uiState.value.copy(error = response.data)
                        }

                        else -> Unit
                    }
                }
                .catch { 
                    _uiState.value = _uiState.value.copy(error = it.message)
                }
                .onCompletion { 
                    _uiState.value = _uiState.value.copy(isLoading = false)
                }
                .collect { }
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
)
