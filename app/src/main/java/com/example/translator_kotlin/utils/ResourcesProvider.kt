package com.example.translator_kotlin.utils

import android.content.Context
import androidx.annotation.StringRes

class ResourcesProvider constructor(
    private val context: Context
) {
    fun getString(@StringRes stringResId: Int): String {
        return context.getString(stringResId)
    }
}