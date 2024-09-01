package com.sjaindl.s11.core.firestore

import dev.gitlive.firebase.firestore.DocumentReference
import dev.gitlive.firebase.firestore.DocumentSnapshot
import dev.gitlive.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

abstract class FireStoreBaseDataSource<T>(
    protected val firestore: FirebaseFirestore,
) {
    protected abstract val collectionPath: String
    protected abstract val mapper: (DocumentSnapshot) -> T

    protected suspend fun getCollection(): List<T> {
        val snapshotResponse = firestore.collection(collectionPath = collectionPath).get()

        return snapshotResponse.documents.mapNotNull { documentSnapshot ->
            if (documentSnapshot.exists) {
                mapper(documentSnapshot)
            } else {
                null
            }
        }
    }

    protected fun getCollectionFlow(): Flow<List<T>> {
        val snapshotResponse = firestore.collection(collectionPath = collectionPath).snapshots

        return snapshotResponse.map  { querySnapshot ->
            querySnapshot.documents.mapNotNull { documentSnapshot ->
                if (documentSnapshot.exists) {
                    mapper(documentSnapshot)
                } else {
                    null
                }
            }
        }
    }

    protected suspend fun getDocument(path: String): T? {
        val document = getDocumentRef(path = path).get()

        return if (document.exists) {
            mapper(document)
        } else {
            null
        }
    }

    protected fun getDocumentFlow(path: String): Flow<T?> {
        val collectionRef = firestore.collection(collectionPath = collectionPath)
        val documentSnapshots = collectionRef.document(documentPath = path).snapshots

        return documentSnapshots.map  { documentSnapshot ->
            if (documentSnapshot.exists) {
                mapper(documentSnapshot)
            } else {
                null
            }
        }
    }

    protected fun getDocumentRef(path: String): DocumentReference {
        val collectionRef = firestore.collection(collectionPath = collectionPath)
        return collectionRef.document(documentPath = path)
    }
}
