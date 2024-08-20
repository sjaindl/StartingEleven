package com.sjaindl.s11.profile.di

import com.sjaindl.s11.profile.firestore.user.UserDataSource
import com.sjaindl.s11.profile.firestore.user.UserDataSourceImpl
import com.sjaindl.s11.profile.firestore.user.UserRepository
import com.sjaindl.s11.profile.firestore.user.UserRepositoryImpl
import org.koin.dsl.module

val profileModule = module {
    single<UserRepository> { UserRepositoryImpl(userDataSource = get()) }
    single<UserDataSource> { UserDataSourceImpl(firestore = get()) }
}
