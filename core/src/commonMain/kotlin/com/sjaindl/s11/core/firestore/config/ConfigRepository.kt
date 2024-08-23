package com.sjaindl.s11.core.firestore.config

import com.sjaindl.s11.core.firestore.config.model.Config
import kotlinx.coroutines.flow.Flow

interface ConfigRepository {
    suspend fun getConfig(): Config?
    fun getConfigFlow(): Flow<Config?>
}

class ConfigRepositoryImpl(
    private val configDataSource: ConfigDataSource,
): ConfigRepository {
    override suspend fun getConfig() = configDataSource.getConfig()

    override fun getConfigFlow() = configDataSource.getConfigFlow()
}
