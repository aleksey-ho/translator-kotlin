package com.example.translator_kotlin.utils

import android.content.Context
import com.example.translator_kotlin.App

class SharedPreferenceHelper constructor(
    val context: Context
) {

    fun saveBoolean(Key: String, value: Boolean) {
        val editor = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit()
        editor.putBoolean(Key, value)
        editor.apply()
    }

    fun getBooleanValue(Key: String, defaultValue: Boolean): Boolean {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).getBoolean(Key, defaultValue)
    }

    companion object {
        private val PREFS_NAME = App::class.java.name + ".preferences"
        const val DATA_USAGE_WARNING = "DATA_USAGE_WARNING"
    }

}