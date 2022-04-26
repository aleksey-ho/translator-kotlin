package com.example.translator_kotlin.data.source.local

import com.example.translator_kotlin.data.LangDirection
import com.example.translator_kotlin.data.source.LocalDataSource
import com.example.translator_kotlin.data.source.local.entity.LanguageEntity
import com.example.translator_kotlin.data.source.local.entity.TranslateEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.*

class LocalDataSourceImpl internal constructor(
    appDatabase: AppDatabase,
) : LocalDataSource {

    private val languageDao = appDatabase.languageDao()
    private val translateDao = appDatabase.translateDao()

    override suspend fun getLanguages(): List<LanguageEntity> {
        return languageDao.getLanguages()
    }

    override suspend fun getRecentlyUsedSourceLanguage(): LanguageEntity {
        return languageDao.getRecentlyUsedSourceLanguage()
    }

    override suspend fun getRecentlyUsedTargetLanguage(): LanguageEntity {
        return languageDao.getRecentlyUsedTargetLanguage()
    }

    override suspend fun getRecentlyUsedSourceLanguages(): List<LanguageEntity> {
        return languageDao.getRecentlyUsedSourceLanguages()
    }

    override suspend fun getRecentlyUsedTargetLanguages(): List<LanguageEntity> {
        return languageDao.getRecentlyUsedTargetLanguages()
    }

    override val translatesInHistory: Flow<List<TranslateEntity>>
        get() = translateDao.getHistory()
            .map {
                return@map it.mapNotNull { translateEntity ->
                    val languageSource = languageDao
                        .getLanguage(translateEntity.languageSourceCode)
                    if (languageSource != null) {
                        translateEntity.languageSource = languageSource
                    } else {
                        return@mapNotNull null
                    }
                    val languageTarget = languageDao
                        .getLanguage(translateEntity.languageTargetCode)
                    if (languageTarget != null) {
                        translateEntity.languageTarget = languageTarget
                    } else {
                        return@mapNotNull null
                    }
                    translateEntity
                }
            }

    override val favoriteTranslates: Flow<List<TranslateEntity>>
        get() = translateDao.getFavorites().map {
            return@map it.mapNotNull { translateEntity ->
                val languageSource =
                    languageDao.getLanguage(translateEntity.languageSourceCode)
                if (languageSource != null) {
                    translateEntity.languageSource = languageSource
                } else {
                    return@mapNotNull null
                }
                val languageTarget =
                    languageDao.getLanguage(translateEntity.languageTargetCode)
                if (languageTarget != null) {
                    translateEntity.languageTarget = languageTarget
                } else {
                    return@mapNotNull null
                }
                translateEntity
            }
        }

    override suspend fun updateLanguages(list: List<String>) {
        for (code in list) {
            var language = languageDao.getLanguage(code)
            if (language != null) {
                languageDao.update(language)
            } else {
                language = LanguageEntity(code)
                languageDao.insertLanguage(language)
            }
        }
    }

    override suspend fun updateLanguageUsage(
        language: LanguageEntity,
        direction: LangDirection
    ) {
        language.updateUsage(direction)
        languageDao.update(language)
    }

    override suspend fun addTranslate(
        textSource: String,
        textTranslate: String,
        languageSource: LanguageEntity,
        languageTarget: LanguageEntity
    ) {
        if (textSource.isEmpty()) {
            return
        }
        var translate =
            translateDao.getTranslate(languageSource.code, languageTarget.code, textSource)
        if (translate == null) {
            translate = TranslateEntity(
                languageSourceCode = languageSource.code,
                languageTargetCode = languageTarget.code,
                date = Calendar.getInstance().time,
                textSource = textSource,
                textTarget = textTranslate,
                savedInHistory = true,
                savedInFavorites = false,
            ).apply {
                this.languageSource = languageSource
                this.languageTarget = languageTarget
            }
        } else {
            translate.savedInHistory = true
            translate.date = Calendar.getInstance().time
            translate.textTarget = textTranslate
        }
        translateDao.insert(translate)
    }

    override suspend fun saveAsFavorite(translate: TranslateEntity) {
        translate.savedInFavorites = true
        translate.date = Calendar.getInstance().time
        translateDao.update(translate)
    }

    override suspend fun removeFromFavorites(translate: TranslateEntity) {
        translate.savedInFavorites = false
        translate.date = Calendar.getInstance().time
        translateDao.update(translate)
    }

    override suspend fun clearHistory() {
        translateDao.clearHistory()
        removeRedundantEntries()
    }

    override suspend fun clearFavorites() {
        translateDao.clearFavorites()
        removeRedundantEntries()
    }

    private suspend fun removeRedundantEntries() {
        translateDao.removeRedundantEntries()
    }

    override suspend fun initFirstSelectedLanguages() {
        languageDao.getLanguage("en")?.let { languageSource ->
            languageSource.sourceLastUseDate = Calendar.getInstance().time
            languageDao.update(languageSource)
        }
        languageDao.getLanguage("ru")?.let { languageTarget ->
            languageTarget.targetLastUseDate = Calendar.getInstance().time
            languageDao.update(languageTarget)
        }
    }

}
