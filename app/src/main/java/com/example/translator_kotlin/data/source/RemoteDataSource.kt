package com.example.translator_kotlin.data.source

import com.example.translator_kotlin.domain.model.Language
import com.example.translator_kotlin.domain.model.Translate

interface RemoteDataSource {

    fun loadRemoteLanguages(): List<String>

    suspend fun getTranslate(langSource: Language, langTarget: Language, text: String): Translate

    suspend fun downloadLanguageModel(language: Language)

}
