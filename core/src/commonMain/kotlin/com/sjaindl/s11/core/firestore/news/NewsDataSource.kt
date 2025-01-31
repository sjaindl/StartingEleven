package com.sjaindl.s11.core.firestore.news

import com.sjaindl.s11.core.CachedValue
import com.sjaindl.s11.core.firestore.FireStoreBaseDataSource
import com.sjaindl.s11.core.firestore.news.model.News
import dev.gitlive.firebase.firestore.DocumentSnapshot
import dev.gitlive.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import org.koin.core.component.KoinComponent

interface NewsDataSource {
    suspend fun getNews(): News?
    fun getNewsFlow(): Flow<News?>
}

internal class NewsDataSourceImpl(
    firestore: FirebaseFirestore,
): FireStoreBaseDataSource<News>(firestore = firestore), NewsDataSource, KoinComponent {
    private var cache: CachedValue<News>? = null

    override val collectionPath: String = "news"

    override val mapper: (DocumentSnapshot) -> News = {
        it.data()
    }

    override suspend fun getNews(): News? {
        val cachedValue = cache?.get()
        if (cachedValue != null) return cachedValue

        val news = getDocument(path = "news")
        cache = CachedValue(
            value = news,
        )

        return news
    }

    override fun getNewsFlow() = getDocumentFlow(path = "news")
}
