package com.example.gestionrendez_vous.utils

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE)

    fun createLoginSession(email: String, name: String) {
        val editor = prefs.edit()
        editor.putBoolean(Constants.KEY_IS_LOGGED_IN, true)
        editor.putString(Constants.KEY_USER_EMAIL, email)
        editor.putString(Constants.KEY_USER_NAME, name)
        editor.apply()
    }

    fun isLoggedIn(): Boolean {
        return prefs.getBoolean(Constants.KEY_IS_LOGGED_IN, false)
    }

    fun getUserEmail(): String? {
        return prefs.getString(Constants.KEY_USER_EMAIL, null)
    }

    fun logout() {
        val editor = prefs.edit()
        editor.clear()
        editor.apply()
    }
}
