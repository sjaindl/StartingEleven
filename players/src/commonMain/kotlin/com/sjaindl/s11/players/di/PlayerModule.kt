package com.sjaindl.s11.players.di

import com.sjaindl.s11.players.CalculatePlayerLineupsUseCase
import org.koin.dsl.module

val playerModule = module {
    single<CalculatePlayerLineupsUseCase> { CalculatePlayerLineupsUseCase() }
}
