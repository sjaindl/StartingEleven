package com.sjaindl.s11.core.di

import com.sjaindl.s11.core.EventRepository
import com.sjaindl.s11.core.EventRepositoryImpl
import com.sjaindl.s11.core.firestore.bets.BetsDataSource
import com.sjaindl.s11.core.firestore.bets.BetsDataSourceImpl
import com.sjaindl.s11.core.firestore.bets.BetsRepository
import com.sjaindl.s11.core.firestore.bets.BetsRepositoryImpl
import com.sjaindl.s11.core.firestore.config.ConfigDataSource
import com.sjaindl.s11.core.firestore.config.ConfigDataSourceImpl
import com.sjaindl.s11.core.firestore.config.ConfigRepository
import com.sjaindl.s11.core.firestore.config.ConfigRepositoryImpl
import com.sjaindl.s11.core.firestore.formations.FormationDataSource
import com.sjaindl.s11.core.firestore.formations.FormationDataSourceImpl
import com.sjaindl.s11.core.firestore.formations.FormationRepository
import com.sjaindl.s11.core.firestore.formations.FormationRepositoryImpl
import com.sjaindl.s11.core.firestore.matchday.MatchDayDataSource
import com.sjaindl.s11.core.firestore.matchday.MatchDayDataSourceImpl
import com.sjaindl.s11.core.firestore.matchday.MatchDayRepository
import com.sjaindl.s11.core.firestore.matchday.MatchDayRepositoryImpl
import com.sjaindl.s11.core.firestore.news.NewsDataSource
import com.sjaindl.s11.core.firestore.news.NewsDataSourceImpl
import com.sjaindl.s11.core.firestore.news.NewsRepository
import com.sjaindl.s11.core.firestore.news.NewsRepositoryImpl
import com.sjaindl.s11.core.firestore.player.PlayerDataSource
import com.sjaindl.s11.core.firestore.player.PlayerDataSourceImpl
import com.sjaindl.s11.core.firestore.player.PlayerRepository
import com.sjaindl.s11.core.firestore.player.PlayerRepositoryImpl
import com.sjaindl.s11.core.firestore.user.UserDataSource
import com.sjaindl.s11.core.firestore.user.UserDataSourceImpl
import com.sjaindl.s11.core.firestore.user.UserRepository
import com.sjaindl.s11.core.firestore.user.UserRepositoryImpl
import com.sjaindl.s11.core.firestore.userlineup.UserLineupDataSource
import com.sjaindl.s11.core.firestore.userlineup.UserLineupDataSourceImpl
import com.sjaindl.s11.core.firestore.userlineup.UserLineupRepository
import com.sjaindl.s11.core.firestore.userlineup.UserLineupRepositoryImpl
import com.sjaindl.s11.core.firestore.usermatchday.UserMatchDayDataSource
import com.sjaindl.s11.core.firestore.usermatchday.UserMatchDayDataSourceImpl
import com.sjaindl.s11.core.firestore.usermatchday.UserMatchDayRepository
import com.sjaindl.s11.core.firestore.usermatchday.UserMatchDayRepositoryImpl
import org.koin.dsl.module

val coreModule = module {
    single<BetsRepository> { BetsRepositoryImpl(betsDataSource = get()) }
    single<BetsDataSource> { BetsDataSourceImpl(firestore = get()) }

    single<ConfigRepository> { ConfigRepositoryImpl(configDataSource = get()) }
    single<ConfigDataSource> { ConfigDataSourceImpl(firestore = get()) }

    single<FormationRepository> { FormationRepositoryImpl(formationDataSource = get()) }
    single<FormationDataSource> { FormationDataSourceImpl(firestore = get()) }

    single<MatchDayRepository> { MatchDayRepositoryImpl(matchDayDataSource = get()) }
    single<MatchDayDataSource> { MatchDayDataSourceImpl(firestore = get()) }

    single<PlayerRepository> { PlayerRepositoryImpl(playerDataSource = get()) }
    single<PlayerDataSource> { PlayerDataSourceImpl(firestore = get()) }

    single<UserRepository> { UserRepositoryImpl(userDataSource = get()) }
    single<UserDataSource> { UserDataSourceImpl(firestore = get()) }

    single<UserLineupRepository> { UserLineupRepositoryImpl(userLineupDataSource = get()) }
    single<UserLineupDataSource> { UserLineupDataSourceImpl(firestore = get()) }

    single<UserMatchDayRepository> { UserMatchDayRepositoryImpl(userMatchDayDataSource = get()) }
    single<UserMatchDayDataSource> { UserMatchDayDataSourceImpl(firestore = get()) }

    single<NewsRepository> { NewsRepositoryImpl(newsDataSource = get()) }
    single<NewsDataSource> { NewsDataSourceImpl(firestore = get()) }

    single<EventRepository> { EventRepositoryImpl() }
}
