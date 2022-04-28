package com.example.translator_kotlin.domain.repository

import com.example.translator_kotlin.data.LangDirection
import com.example.translator_kotlin.domain.model.Language
import com.example.translator_kotlin.domain.model.Translate
import kotlinx.coroutines.flow.Flow

/**
 * To make an interaction between [RepositoryImp] & [UseCase]
 * */
interface Repository {

    suspend fun getLanguages(): List<Language>

    suspend fun getRecentlyUsedSourceLanguage(): Language

    suspend fun getRecentlyUsedTargetLanguage(): Language

    suspend fun getRecentlyUsedSourceLanguages(): List<Language>

    suspend fun getRecentlyUsedTargetLanguages(): List<Language>

    val translatesInHistory: Flow<List<Translate>>

    val favoriteTranslates: Flow<List<Translate>>

    suspend fun getTranslate(langSource: Language, langTarget: Language, text: String): Translate

    suspend fun loadLanguages()

    suspend fun updateLanguages(list: List<String>)

    suspend fun updateLanguageUsage(language: Language, direction: LangDirection)

    suspend fun addTranslate(
        textSource: String,
        textTranslate: String,
        languageSource: Language,
        languageTarget: Language
    )

    suspend fun saveAsFavorite(translate: Translate)

    suspend fun removeFromFavorites(translate: Translate)

    suspend fun clearHistory()

    suspend fun clearFavorites()

    suspend fun downloadLanguageModels()

    suspend fun downloadLanguageModel(language: Language)

}
