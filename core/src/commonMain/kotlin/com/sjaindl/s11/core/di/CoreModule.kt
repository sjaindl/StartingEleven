package com.sjaindl.s11.core.di

import com.sjaindl.s11.core.firestore.player.PlayerDataSource
import com.sjaindl.s11.core.firestore.player.PlayerDataSourceImpl
import com.sjaindl.s11.core.firestore.player.PlayerRepository
import com.sjaindl.s11.core.firestore.player.PlayerRepositoryImpl
import org.koin.dsl.module

val coreModule = module {
    single<PlayerRepository> { PlayerRepositoryImpl(playerDataSource = get()) }
    single<PlayerDataSource> { PlayerDataSourceImpl(firestore = get()) }
}
