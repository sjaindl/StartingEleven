package com.sjaindl.s11.di

import com.sjaindl.s11.core.di.coreModule
import com.sjaindl.s11.profile.di.profileModule
import org.koin.core.context.startKoin

fun initKoin(){
    startKoin {
        modules(appModule, profileModule, coreModule)
    }
}
