package com.sjaindl.s11.home.di

import com.sjaindl.s11.home.recommender.RecommendLineupUseCase
import org.koin.dsl.module

val homeModule = module {
    single<RecommendLineupUseCase> { RecommendLineupUseCase() }
}
