package com.sjaindl.s11.core.faq

import com.sjaindl.s11.core.faq.model.Faq
import kotlinx.coroutines.flow.Flow

interface FaqRepository {
    suspend fun getFaqs(): List<Faq>
    fun getFaqsFlow(): Flow<List<Faq>>
}

class FaqRepositoryImpl(
    private val playerDataSource: FaqDataSource,
): FaqRepository {
    override suspend fun getFaqs() = playerDataSource.getFaqs()

    override fun getFaqsFlow() = playerDataSource.getFaqsFlow()
}
