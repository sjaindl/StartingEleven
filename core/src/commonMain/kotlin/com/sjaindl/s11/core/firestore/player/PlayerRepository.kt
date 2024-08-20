package com.sjaindl.s11.core.firestore.player

import com.sjaindl.s11.core.firestore.player.model.Player
import kotlinx.coroutines.flow.Flow

interface PlayerRepository {
    suspend fun getPlayers(): List<Player>
    fun getPlayersFlow(): Flow<List<Player>>
}

class PlayerRepositoryImpl(
    private val playerDataSource: PlayerDataSource,
): PlayerRepository {
    override suspend fun getPlayers() = playerDataSource.getPlayers()

    override fun getPlayersFlow() = playerDataSource.getPlayersFlow()
}
