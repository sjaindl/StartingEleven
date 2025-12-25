@file:JvmName("IosAssistantModule")

package com.sjaindl.s11.assistant.di

import com.sjaindl.s11.assistant.database.DriverFactory
import org.koin.dsl.module
import kotlin.jvm.JvmName

actual val platformModule = module {
    single { DriverFactory() }
}
