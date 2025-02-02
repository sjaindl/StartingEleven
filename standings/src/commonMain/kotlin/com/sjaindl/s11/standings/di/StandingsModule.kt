package com.sjaindl.s11.standings.di

import com.sjaindl.s11.standings.domain.CalculatePointsForBetUseCase
import com.sjaindl.s11.standings.domain.CalculatePointsForMatchDayLineupUseCase
import com.sjaindl.s11.standings.domain.CalculatePointsUseCase
import org.koin.dsl.module

val standingsModule = module {

    single<CalculatePointsUseCase> {
        CalculatePointsUseCase(
            userRepository = get(),
            matchDayRepository = get(),
            calculatePointsForBetUseCase = get(),
            calculatePointsForMatchDayLineupUseCase = get(),
        )
    }

    single<CalculatePointsForMatchDayLineupUseCase> {
        CalculatePointsForMatchDayLineupUseCase(
            configRepository = get(),
            playerRepository = get(),
            userMatchDayRepository = get(),
        )
    }

    single<CalculatePointsForBetUseCase> {
        CalculatePointsForBetUseCase(
            userMatchDayRepository = get(),
            betsRepository = get(),
        )
    }
}
