package com.example.weatherapp.util

import android.content.Context
import android.content.SharedPreferences

class SharedPreferencesManager(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

    fun saveCredentials(username: String, password: String) {
        with(sharedPreferences.edit()) {
            putString("USERNAME", username)
            putString("PASSWORD", password)
            apply()
        }
    }

    fun getUsername(): String? = sharedPreferences.getString("USERNAME", null)
    fun getPassword(): String? = sharedPreferences.getString("PASSWORD", null)
}
