package com.example.pawsitive.util

import android.content.Context
import android.content.SharedPreferences

class PreferencesManager(context: Context) {

    private val SHARED_PREFS_NAME = "PAWSITIVE_APP"

    private val preferences: SharedPreferences = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)

    fun getToken(): String? {
        return preferences.getString("token", null)
    }

    fun clear() {
        return preferences.edit().clear().apply()
    }
    fun saveToken(token: String) {
        preferences.edit().putString("token", token).apply()
    }

    fun getUserId(): String? {
        return preferences.getString("user_id", null)
    }

    fun setUserId(id: String) {
        return preferences.edit().putString("user_id", id).apply()
    }
}