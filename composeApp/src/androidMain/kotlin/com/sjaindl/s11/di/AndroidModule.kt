package com.sjaindl.s11.di

import androidx.credentials.CredentialManager
import com.facebook.CallbackManager
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val androidModule = module {
    single {
        CallbackManager.Factory.create()
    }

    single {
        CredentialManager.create(context = androidContext())
    }
}
