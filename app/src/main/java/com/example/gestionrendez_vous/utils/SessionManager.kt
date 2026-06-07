package com.example.gestionrendez_vous.utils

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("gestion_rdv_session", Context.MODE_PRIVATE)

    fun saveToken(token: String) {
        prefs.edit().putString("auth_token", token).apply()
    }

    fun getToken(): String? {
        return prefs.getString("auth_token", null)
    }

    fun isLoggedIn(): Boolean {
        return getToken() != null
    }

    fun logout() {
        prefs.edit().clear().apply()
    }
}
