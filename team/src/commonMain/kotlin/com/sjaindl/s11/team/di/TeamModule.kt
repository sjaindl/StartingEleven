package com.sjaindl.s11.team.di

import com.sjaindl.s11.team.recommender.RecommendLineupUseCase
import org.koin.dsl.module

val teamModule = module {
    single<RecommendLineupUseCase> { RecommendLineupUseCase() }
}
