package com.example.translator_kotlin.data.repository

import com.example.translator_kotlin.data.LangDirection
import com.example.translator_kotlin.data.mapper.plain
import com.example.translator_kotlin.data.mapper.plainLanguageEntityList
import com.example.translator_kotlin.data.mapper.plainTranslateEntityList
import com.example.translator_kotlin.data.mapper.toEntity
import com.example.translator_kotlin.data.source.LocalDataSource
import com.example.translator_kotlin.data.source.RemoteDataSource
import com.example.translator_kotlin.domain.model.Language
import com.example.translator_kotlin.domain.model.Translate
import com.example.translator_kotlin.domain.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class RepositoryImpl(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) : Repository {

    private val cachedTranslates: MutableList<Translate> = ArrayList()

    override suspend fun getTranslate(
        langSource: Language,
        langTarget: Language,
        text: String
    ): Translate {
        val item = cachedTranslates.firstOrNull {
            it.textSource == text && langSource == it.languageSource && langTarget == it.languageTarget
        }
        if (item != null) {
            return cachedTranslates[cachedTranslates.indexOf(item)]
        } else {
            val translate = remoteDataSource.getTranslate(langSource, langTarget, text)
            cachedTranslates.add(translate)
            return translate
        }
    }

    override suspend fun getLanguages(): List<Language> {
        return localDataSource.getLanguages().plainLanguageEntityList()
    }

    override suspend fun loadLanguages() {
        val languages = remoteDataSource.loadRemoteLanguages()
        localDataSource.updateLanguages(languages)
        localDataSource.initFirstSelectedLanguages()
    }

    override suspend fun updateLanguages(list: List<String>) {
        return localDataSource.updateLanguages(list)
    }

    override suspend fun getRecentlyUsedSourceLanguage(): Language {
        return localDataSource.getRecentlyUsedSourceLanguage().plain()
    }

    override suspend fun getRecentlyUsedTargetLanguage(): Language {
        return localDataSource.getRecentlyUsedTargetLanguage().plain()
    }

    override suspend fun getRecentlyUsedSourceLanguages(): List<Language> {
        return localDataSource.getRecentlyUsedSourceLanguages().plainLanguageEntityList()
    }

    override suspend fun getRecentlyUsedTargetLanguages(): List<Language> {
        return localDataSource.getRecentlyUsedTargetLanguages().plainLanguageEntityList()
    }

    override suspend fun updateLanguageUsage(language: Language, direction: LangDirection) {
        return localDataSource.updateLanguageUsage(language.toEntity(), direction)
    }

    override suspend fun addTranslate(
        textSource: String,
        textTranslate: String,
        languageSource: Language,
        languageTarget: Language
    ) {
        localDataSource.addTranslate(
            textSource,
            textTranslate,
            languageSource.toEntity(),
            languageTarget.toEntity()
        )
    }

    override val translatesInHistory: Flow<List<Translate>>
        get() = localDataSource.translatesInHistory.map { it.plainTranslateEntityList() }

    override val favoriteTranslates: Flow<List<Translate>>
        get() = localDataSource.favoriteTranslates.map { it.plainTranslateEntityList() }

    override suspend fun saveAsFavorite(translate: Translate) {
        return localDataSource.saveAsFavorite(translate.toEntity())
    }

    override suspend fun removeFromFavorites(translate: Translate) {
        return localDataSource.removeFromFavorites(translate.toEntity())
    }

    override suspend fun clearHistory() {
        localDataSource.clearHistory()
    }

    override suspend fun clearFavorites() {
        localDataSource.clearFavorites()
    }

    override suspend fun downloadLanguageModels() {
        val recentlyUsedSourceLanguage = localDataSource.getRecentlyUsedSourceLanguage().plain()
        downloadLanguageModel(recentlyUsedSourceLanguage)
        val recentlyUsedTargetLanguage = localDataSource.getRecentlyUsedTargetLanguage().plain()
        downloadLanguageModel(recentlyUsedTargetLanguage)
    }

    override suspend fun downloadLanguageModel(language: Language) {
        remoteDataSource.downloadLanguageModel(language)
    }

}