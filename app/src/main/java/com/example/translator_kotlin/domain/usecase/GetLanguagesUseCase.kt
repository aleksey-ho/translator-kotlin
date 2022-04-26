package com.example.translator_kotlin.domain.usecase

import com.example.translator_kotlin.data.LangDirection
import com.example.translator_kotlin.domain.model.Language
import com.example.translator_kotlin.domain.repository.Repository
import javax.inject.Inject

class GetLanguagesUseCase @Inject constructor(
    private val repository: Repository
) {

    suspend fun getLanguages(): List<Language> {
        return repository.getLanguages()
    }

    suspend fun getRecentlyUsedSourceLanguage(): Language {
        return repository.getRecentlyUsedSourceLanguage()
    }

    suspend fun getRecentlyUsedTargetLanguage(): Language {
        return repository.getRecentlyUsedTargetLanguage()
    }

    suspend fun getRecentlyUsedSourceLanguages(): List<Language> {
        return repository.getRecentlyUsedSourceLanguages()
    }

    suspend fun getRecentlyUsedTargetLanguages(): List<Language> {
        return repository.getRecentlyUsedTargetLanguages()
    }

    suspend fun updateLanguageUsage(language: Language, direction: LangDirection) {
        return repository.updateLanguageUsage(language, direction)
    }

}