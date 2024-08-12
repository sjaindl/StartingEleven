package com.sjaindl.s11.core.player

import com.sjaindl.s11.core.firestore.FireStoreBaseDataSource
import com.sjaindl.s11.core.player.model.Player
import dev.gitlive.firebase.firestore.DocumentSnapshot
import dev.gitlive.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow

interface PlayerDataSource {
    suspend fun getPlayers(): List<Player>
    fun getPlayersFlow(): Flow<List<Player>>
}

internal class PlayerDataSourceImpl(
    firestore: FirebaseFirestore,
): FireStoreBaseDataSource<Player>(firestore = firestore), PlayerDataSource {
    override val collectionPath: String = "players"

    override val mapper: (DocumentSnapshot) -> Player = {
        it.data()
    }

    override suspend fun getPlayers() = getData()

    override fun getPlayersFlow() = getDataFlow()
}
