package com.sjaindl.s11.ai.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sjaindl.s11.ai.data.remote.model.FlowiseResponse
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
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.getValue

@KoinViewModel
class ChatViewModel() : ViewModel(), KoinComponent {

    private val getAiCompletionUseCase: GetAiCompletionUseCase by inject()

    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()

    private var chatId: String? = null

    fun sendPrompt(prompt: String) {
        viewModelScope.launch {
            getAiCompletionUseCase(prompt = prompt, chatId = chatId)
                .onStart {
                    _uiState.value = _uiState.value.copy(
                        isLoading = true,
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

                        else -> Unit
                    }
                }
                .catch {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        messages = _uiState.value.messages + ChatMessage(
                            text = "Error: ${it.message}",
                            isFromUser = false
                        )
                    )
                }
                .onCompletion { 
                    _uiState.value = _uiState.value.copy(isLoading = false)
                }
                .collect {

                }
        }
    }
}

data class ChatUiState(
    val isLoading: Boolean = false,
    val messages: List<ChatMessage> = emptyList(),
)

data class ChatMessage(
    val text: String,
    val isFromUser: Boolean,
)
