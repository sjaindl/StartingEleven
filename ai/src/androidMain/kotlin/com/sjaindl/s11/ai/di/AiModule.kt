@file:JvmName("AndroidAiModule")

package com.sjaindl.s11.ai.di

import com.sjaindl.s11.ai.database.DriverFactory
import org.koin.dsl.module

actual val platformModule = module {
    single { DriverFactory(get()) }
}
