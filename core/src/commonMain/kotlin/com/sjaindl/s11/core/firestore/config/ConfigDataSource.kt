package com.sjaindl.s11.core.firestore.config

import com.sjaindl.s11.core.CachedValue
import com.sjaindl.s11.core.firestore.FireStoreBaseDataSource
import com.sjaindl.s11.core.firestore.config.model.Config
import dev.gitlive.firebase.firestore.DocumentSnapshot
import dev.gitlive.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import org.koin.core.component.KoinComponent

interface ConfigDataSource {
    suspend fun getConfig(): Config?
    fun getConfigFlow(): Flow<Config?>
}

internal class ConfigDataSourceImpl(
    firestore: FirebaseFirestore,
): FireStoreBaseDataSource<Config>(firestore = firestore), ConfigDataSource, KoinComponent {
    private var cache: CachedValue<Config>? = null

    override val collectionPath: String = "config"

    override val mapper: (DocumentSnapshot) -> Config = {
        it.data()
    }

    override suspend fun getConfig(): Config? {
        val cachedValue = cache?.get()
        if (cachedValue != null) return cachedValue

        val config = getDocument(path = "config")
        cache = CachedValue(
            value = config,
        )
        return config
    }

    override fun getConfigFlow() = getDocumentFlow(path = "config")
}
