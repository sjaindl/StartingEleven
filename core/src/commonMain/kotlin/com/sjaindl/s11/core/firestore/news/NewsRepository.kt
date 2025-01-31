package com.sjaindl.s11.core.firestore.news

import com.sjaindl.s11.core.firestore.news.model.News
import kotlinx.coroutines.flow.Flow

interface NewsRepository {
    suspend fun getNews(): News?
    fun getNewsFlow(): Flow<News?>
}

class NewsRepositoryImpl(
    private val newsDataSource: NewsDataSource,
): NewsRepository {
    override suspend fun getNews() = newsDataSource.getNews()

    override fun getNewsFlow() = newsDataSource.getNewsFlow()
}
