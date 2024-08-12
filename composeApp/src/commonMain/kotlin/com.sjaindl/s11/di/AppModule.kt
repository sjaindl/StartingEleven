package com.sjaindl.s11.di

import com.sjaindl.s11.core.faq.FaqDataSource
import com.sjaindl.s11.core.faq.FaqDataSourceImpl
import com.sjaindl.s11.core.faq.FaqRepository
import com.sjaindl.s11.core.faq.FaqRepositoryImpl
import com.sjaindl.s11.core.player.PlayerDataSource
import com.sjaindl.s11.core.player.PlayerDataSourceImpl
import com.sjaindl.s11.core.player.PlayerRepository
import com.sjaindl.s11.core.player.PlayerRepositoryImpl
import dev.gitlive.firebase.firestore.FirebaseFirestore
import org.koin.dsl.module

val appModule = module {
    single { getFirebaseFirestore() }

    single<PlayerRepository> { PlayerRepositoryImpl(playerDataSource = get()) }
    single<PlayerDataSource> { PlayerDataSourceImpl(firestore = get()) }

    single<FaqRepository> { FaqRepositoryImpl(playerDataSource = get()) }
    single<FaqDataSource> { FaqDataSourceImpl(firestore = get()) }
}

expect fun getFirebaseFirestore(): FirebaseFirestore
