@file:JvmName("AndroidAssistantModule")

package com.sjaindl.s11.assistant.di

import com.sjaindl.s11.assistant.database.DriverFactory
import org.koin.dsl.module

actual val platformModule = module {
    single { DriverFactory(get()) }
}
