package com.sjaindl.s11.faq

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sjaindl.s11.firestore.faq.FaqRepository
import com.sjaindl.s11.firestore.faq.model.Faq
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

sealed class FaqState {
    data object Initial: FaqState()
    data object Loading: FaqState()
    data class Content(val faqs: List<Faq>): FaqState()
    data class Error(val message: String): FaqState()
}

@KoinViewModel
class FaqViewModel : ViewModel(), KoinComponent {

    private val tag = "FaqViewModel"

    private val faqRepository: FaqRepository by inject()

    private var _faqState: MutableStateFlow<FaqState> = MutableStateFlow(
        FaqState.Initial
    )
    var faqState = _faqState.asStateFlow()

    init {
        loadFaq()
    }

    fun loadFaq() = viewModelScope.launch {
        _faqState.value = FaqState.Loading

        try {
            val faqs = faqRepository.getFaqs()
            _faqState.value = FaqState.Content(faqs = faqs)
        } catch (exception: Exception) {
            val message = exception.message ?: exception.toString()
            Napier.e(message = message, throwable = exception, tag = tag)
            _faqState.value = FaqState.Error(message = message)
        }
    }
}
