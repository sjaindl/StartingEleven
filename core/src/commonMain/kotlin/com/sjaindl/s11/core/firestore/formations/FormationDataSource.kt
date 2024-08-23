package com.sjaindl.s11.core.firestore.formations

import com.sjaindl.s11.core.CachedValue
import com.sjaindl.s11.core.firestore.FireStoreBaseDataSource
import com.sjaindl.s11.core.firestore.formations.model.Formation
import dev.gitlive.firebase.firestore.DocumentSnapshot
import dev.gitlive.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import org.koin.core.component.KoinComponent

interface FormationDataSource {
    suspend fun getFormations(): List<Formation>
    fun getFormationsFlow(): Flow<List<Formation>>
}

internal class FormationDataSourceImpl(
    firestore: FirebaseFirestore,
): FireStoreBaseDataSource<Formation>(firestore = firestore), FormationDataSource, KoinComponent {
    private var cache: CachedValue<List<Formation>>? = null

    override val collectionPath: String = "formations"

    override val mapper: (DocumentSnapshot) -> Formation = {
        it.data()
    }

    override suspend fun getFormations(): List<Formation> {
        val cachedValue = cache?.get()
        if (!cachedValue.isNullOrEmpty()) return cachedValue

        val formation = getCollection()
        cache = CachedValue(
            value = formation,
        )
        return formation
    }

    override fun getFormationsFlow() = getCollectionFlow()
}
