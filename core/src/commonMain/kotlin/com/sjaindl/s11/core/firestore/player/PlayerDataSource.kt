package com.sjaindl.s11.core.firestore.player

import com.sjaindl.s11.core.CachedValue
import com.sjaindl.s11.core.firestore.FireStoreBaseDataSource
import com.sjaindl.s11.core.firestore.player.model.Player
import dev.gitlive.firebase.firestore.DocumentSnapshot
import dev.gitlive.firebase.firestore.FirebaseFirestore
import dev.gitlive.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.Flow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

interface PlayerDataSource {
    suspend fun getPlayers(): List<Player>
    fun getPlayersFlow(): Flow<List<Player>>
}

internal class PlayerDataSourceImpl(
    firestore: FirebaseFirestore,
): FireStoreBaseDataSource<Player>(firestore = firestore), PlayerDataSource, KoinComponent {
    private val storage: FirebaseStorage by inject()

    private var cache: CachedValue<List<Player>>? = null

    override val collectionPath: String = "players"

    override val mapper: (DocumentSnapshot) -> Player = {
        it.data()
    }

    override suspend fun getPlayers(): List<Player> {
        val cachedValue = cache?.get()
        if (!cachedValue.isNullOrEmpty()) return cachedValue

        val players = getPlayerDataWithImage()
        cache = CachedValue(
            value = players,
        )
        return players
    }

    override fun getPlayersFlow() = getCollectionFlow()

    private suspend fun getPlayerDataWithImage() = getCollection().map {
        it.copy(
            downloadUrl = getPlayerImageDownloadUrl(it),
        )
    }

    private suspend fun getPlayerImageDownloadUrl(player: Player): String? {
        return player.imageRef?.let {
            storage.reference(location = it).getDownloadUrl()
        }
    }
}
