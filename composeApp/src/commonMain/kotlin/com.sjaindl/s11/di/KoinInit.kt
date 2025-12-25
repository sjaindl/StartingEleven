package com.sjaindl.s11.di

import com.sjaindl.s11.assistant.di.assistantModule
import com.sjaindl.s11.core.di.coreModule
import com.sjaindl.s11.players.di.playerModule
import com.sjaindl.s11.standings.di.standingsModule
import com.sjaindl.s11.team.di.teamModule
import org.koin.core.context.startKoin

fun initKoin() {
    startKoin {
        modules(
            appModule,
            assistantModule,
            coreModule,
            playerModule,
            standingsModule,
            teamModule,
        )
    }
}
