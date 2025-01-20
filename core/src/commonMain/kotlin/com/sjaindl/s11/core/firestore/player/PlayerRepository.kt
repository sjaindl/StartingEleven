package com.sjaindl.s11.core.firestore.player

import com.sjaindl.s11.core.firestore.player.model.Player
import kotlinx.coroutines.flow.Flow

interface PlayerRepository {
    suspend fun getPlayers(onlyActive: Boolean): List<Player>
    fun getPlayersFlow(onlyActive: Boolean): Flow<List<Player>>
}

class PlayerRepositoryImpl(
    private val playerDataSource: PlayerDataSource,
): PlayerRepository {
    override suspend fun getPlayers(onlyActive: Boolean) = playerDataSource.getPlayers(onlyActive = onlyActive)

    override fun getPlayersFlow(onlyActive: Boolean) = playerDataSource.getPlayersFlow(onlyActive = onlyActive)
}
