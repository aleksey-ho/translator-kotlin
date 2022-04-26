package com.example.translator_kotlin.data.source

import com.example.translator_kotlin.data.LangDirection
import com.example.translator_kotlin.data.source.local.entity.LanguageEntity
import com.example.translator_kotlin.data.source.local.entity.TranslateEntity
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {

    suspend fun getLanguages(): List<LanguageEntity>

    suspend fun getRecentlyUsedSourceLanguage(): LanguageEntity

    suspend fun getRecentlyUsedTargetLanguage(): LanguageEntity

    suspend fun getRecentlyUsedSourceLanguages(): List<LanguageEntity>

    suspend fun getRecentlyUsedTargetLanguages(): List<LanguageEntity>

    val translatesInHistory: Flow<List<TranslateEntity>>

    val favoriteTranslates: Flow<List<TranslateEntity>>

    suspend fun updateLanguages(list: List<String>)

    suspend fun updateLanguageUsage(language: LanguageEntity, direction: LangDirection)

    suspend fun addTranslate(
        textSource: String,
        textTranslate: String,
        languageSource: LanguageEntity,
        languageTarget: LanguageEntity
    )

    suspend fun saveAsFavorite(translate: TranslateEntity)

    suspend fun removeFromFavorites(translate: TranslateEntity)

    suspend fun clearHistory()

    suspend fun clearFavorites()

    suspend fun initFirstSelectedLanguages()

}
