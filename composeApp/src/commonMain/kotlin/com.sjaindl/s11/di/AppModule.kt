package com.sjaindl.s11.di

import com.sjaindl.s11.core.faq.FaqDataSource
import com.sjaindl.s11.core.faq.FaqDataSourceImpl
import com.sjaindl.s11.core.faq.FaqRepository
import com.sjaindl.s11.core.faq.FaqRepositoryImpl
import com.sjaindl.s11.core.player.PlayerDataSource
import com.sjaindl.s11.core.player.PlayerDataSourceImpl
import com.sjaindl.s11.core.player.PlayerRepository
import com.sjaindl.s11.core.player.PlayerRepositoryImpl
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.FirebaseFirestore
import dev.gitlive.firebase.storage.storage
import org.koin.dsl.module

val appModule = module {
    single { getFirebaseFirestore() }

    single<PlayerRepository> { PlayerRepositoryImpl(playerDataSource = get()) }
    single<PlayerDataSource> { PlayerDataSourceImpl(firestore = get()) }

    single<FaqRepository> { FaqRepositoryImpl(playerDataSource = get()) }
    single<FaqDataSource> { FaqDataSourceImpl(firestore = get()) }

    single {
        Firebase.storage
    }
}

expect fun getFirebaseFirestore(): FirebaseFirestore
