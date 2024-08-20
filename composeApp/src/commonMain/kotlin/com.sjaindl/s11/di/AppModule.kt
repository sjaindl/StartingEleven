package com.sjaindl.s11.di

import com.sjaindl.s11.firestore.faq.FaqDataSource
import com.sjaindl.s11.firestore.faq.FaqDataSourceImpl
import com.sjaindl.s11.firestore.faq.FaqRepository
import com.sjaindl.s11.firestore.faq.FaqRepositoryImpl
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.FirebaseFirestore
import dev.gitlive.firebase.storage.storage
import org.koin.dsl.module

val appModule = module {
    single { getFirebaseFirestore() }

    single<FaqRepository> { FaqRepositoryImpl(playerDataSource = get()) }
    single<FaqDataSource> { FaqDataSourceImpl(firestore = get()) }

    single {
        Firebase.storage
    }
}

expect fun getFirebaseFirestore(): FirebaseFirestore
