package com.sjaindl.s11.di

import com.sjaindl.s11.ai.di.aiModule
import com.sjaindl.s11.core.di.coreModule
import com.sjaindl.s11.players.di.playerModule
import com.sjaindl.s11.standings.di.standingsModule
import com.sjaindl.s11.team.di.teamModule
import org.koin.core.context.startKoin

fun initKoin() {
    startKoin {
        modules(
            appModule,
            aiModule,
            coreModule,
            playerModule,
            standingsModule,
            teamModule,
        )
    }
}
