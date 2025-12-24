@file:JvmName("IosAiModule")

package com.sjaindl.s11.ai.di

import com.sjaindl.s11.ai.database.DriverFactory
import org.koin.dsl.module
import kotlin.jvm.JvmName

actual val platformModule = module {
    single { DriverFactory() }
}
