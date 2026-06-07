package com.example.gestionrendez_vous.di

import android.content.Context
import com.example.gestionrendez_vous.data.local.AppDatabase
import com.example.gestionrendez_vous.data.remote.RetrofitClient
import com.example.gestionrendez_vous.utils.SessionManager

// Manual Dependency Injection Container
class AppContainer(private val context: Context) {
    
    // Local DB
    private val database: AppDatabase by lazy { AppDatabase.getDatabase(context) }
    val appDao by lazy { database.appDao() }

    // Remote API
    val apiService by lazy { RetrofitClient.apiService }

    // Utils
    val sessionManager: SessionManager by lazy { SessionManager(context) }
}
