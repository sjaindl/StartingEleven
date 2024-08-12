package com.sjaindl.s11.core.faq

import com.sjaindl.s11.core.faq.model.Faq
import com.sjaindl.s11.core.firestore.FireStoreBaseDataSource
import dev.gitlive.firebase.firestore.DocumentSnapshot
import dev.gitlive.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow

interface FaqDataSource {
    suspend fun getFaqs(): List<Faq>
    fun getFaqsFlow(): Flow<List<Faq>>
}

internal class FaqDataSourceImpl(
    firestore: FirebaseFirestore,
): FireStoreBaseDataSource<Faq>(firestore = firestore), FaqDataSource {
    override val collectionPath: String = "faq"

    override val mapper: (DocumentSnapshot) -> Faq = {
        it.data()
    }

    override suspend fun getFaqs() = getData()

    override fun getFaqsFlow() = getDataFlow()
}
