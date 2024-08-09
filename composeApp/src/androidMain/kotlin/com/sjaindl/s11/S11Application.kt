package com.sjaindl.s11

import android.app.Application
import android.content.Context
import com.google.firebase.FirebaseApp
import com.sjaindl.s11.di.androidModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class S11Application : Application() {

    companion object {
        lateinit var appContext: Context
    }

    override fun onCreate() {
        super.onCreate()

        appContext = applicationContext

        FirebaseApp.initializeApp(applicationContext)

        startKoin {
            androidContext(this@S11Application)
            modules(modules = androidModule)
        }
    }
}
