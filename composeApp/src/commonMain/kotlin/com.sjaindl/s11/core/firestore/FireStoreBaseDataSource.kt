package com.sjaindl.s11.core.firestore

import dev.gitlive.firebase.firestore.DocumentSnapshot
import dev.gitlive.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

abstract class FireStoreBaseDataSource<T>(
    private val firestore: FirebaseFirestore,
) {
    protected abstract val collectionPath: String
    protected abstract val mapper: (DocumentSnapshot) -> T

    protected suspend fun getData(): List<T> {
        val snapshotResponse = firestore.collection(collectionPath = collectionPath).get()

        return snapshotResponse.documents.map {
            mapper(it)
        }
    }

    protected fun getDataFlow(): Flow<List<T>> {
        val snapshotResponse = firestore.collection(collectionPath = collectionPath).snapshots

        return snapshotResponse.map  { querySnapshot ->
            querySnapshot.documents.map { documentSnapshot ->
                mapper(documentSnapshot)
            }
        }
    }
}
