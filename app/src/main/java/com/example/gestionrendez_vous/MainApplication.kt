package com.example.gestionrendez_vous

import android.app.Application
import com.example.gestionrendez_vous.di.AppContainer

class MainApplication : Application() {
    
    // Application-level instance of AppContainer
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
    }
}
