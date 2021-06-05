package com.example.basiccomponents

import android.app.Application
import com.example.basiccomponents.network.di.networkModule
import com.example.basiccomponents.ui.di.uiModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(networkModule, uiModule)
        }
    }
}