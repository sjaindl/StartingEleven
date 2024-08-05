package com.sjaindl.s11

import android.app.Application
import android.content.Context
import com.google.firebase.FirebaseApp

class HvtdpApplication : Application() {

    companion object {
        lateinit var appContext: Context
    }

    override fun onCreate() {
        super.onCreate()

        appContext = applicationContext

        FirebaseApp.initializeApp(applicationContext)
    }
}
