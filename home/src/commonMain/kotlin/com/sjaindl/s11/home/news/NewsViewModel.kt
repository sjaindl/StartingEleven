package com.sjaindl.s11.home.news

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sjaindl.s11.core.firestore.news.NewsRepository
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

sealed class NewsState {
    data object Initial: NewsState()
    data object Loading: NewsState()
    data object NoNews: NewsState()
    data class Content(
        val text: String,
    ): NewsState()
    data class Error(val message: String): NewsState()
}

class NewsViewModel : ViewModel(), KoinComponent {

    private val tag = "NewsViewModel"

    private val newsRepository: NewsRepository by inject()

    private var _newsState: MutableStateFlow<NewsState> = MutableStateFlow(value = NewsState.Initial)
    var newsState = _newsState.asStateFlow()

    init {
        loadNews()
    }

    fun loadNews() = viewModelScope.launch {
        _newsState.value = NewsState.Loading

        try {
            newsRepository.getNewsFlow().collectLatest { news ->
                _newsState.value = if (news != null) {
                     NewsState.Content(
                        text = news.text,
                    )
                } else {
                    NewsState.NoNews
                }
            }
        } catch (exception: Exception) {
            val message = exception.message ?: exception.toString()
            Napier.e(message = message, throwable = exception, tag = tag)
            _newsState.value = NewsState.Error(message = message)
        }
    }
}
