package com.sjaindl.s11.core.firestore.player

import com.sjaindl.s11.core.CachedValue
import com.sjaindl.s11.core.firestore.FireStoreBaseDataSource
import com.sjaindl.s11.core.firestore.player.model.Player
import dev.gitlive.firebase.firestore.DocumentSnapshot
import dev.gitlive.firebase.firestore.FirebaseFirestore
import dev.gitlive.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

interface PlayerDataSource {
    suspend fun getPlayers(onlyActive: Boolean): List<Player>
    fun getPlayersFlow(onlyActive: Boolean): Flow<List<Player>>
}

internal class PlayerDataSourceImpl(
    firestore: FirebaseFirestore,
): FireStoreBaseDataSource<Player>(firestore = firestore), PlayerDataSource, KoinComponent {
    private val storage: FirebaseStorage by inject()

    private var activeCache: CachedValue<List<Player>>? = null
    private var allPlayersCache: CachedValue<List<Player>>? = null

    override val collectionPath: String = "players"

    override val mapper: (DocumentSnapshot) -> Player = {
        it.data()
    }

    override suspend fun getPlayers(onlyActive: Boolean): List<Player> {
        val cachedValue = if (onlyActive) {
            activeCache?.get()
        } else {
            allPlayersCache?.get()
        }

        if (!cachedValue.isNullOrEmpty()) return cachedValue

        val players = getPlayerDataWithImage(onlyActive = onlyActive)

        if (onlyActive) {
            activeCache = CachedValue(
                value = players,
            )
        } else {
            allPlayersCache = CachedValue(
                value = players,
            )
        }

        return players
    }

    override fun getPlayersFlow(onlyActive: Boolean) = getCollectionFlow().map {
        it.filter {  player ->
            player.active || !onlyActive
        }
    }

    private suspend fun getPlayerDataWithImage(onlyActive: Boolean) = getCollection().filter {
        it.active || !onlyActive
    }.map {
        it.copy(
            downloadUrl = getPlayerImageDownloadUrl(it),
        )
    }

    private suspend fun getPlayerImageDownloadUrl(player: Player): String? {
        return if (player.imageRef.isNullOrEmpty()) {
            null
        } else {
            storage.reference(location = player.imageRef).getDownloadUrl()
        }
    }
}
