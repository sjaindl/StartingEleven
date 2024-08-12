package com.sjaindl.s11.di

import org.koin.core.context.startKoin

fun initKoin(){
    startKoin {
        modules(appModule)
    }
}
