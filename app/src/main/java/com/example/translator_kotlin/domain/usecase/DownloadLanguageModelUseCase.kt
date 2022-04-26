package com.example.translator_kotlin.domain.usecase

import com.example.translator_kotlin.domain.model.Language
import com.example.translator_kotlin.domain.repository.Repository
import javax.inject.Inject

class DownloadLanguageModelUseCase @Inject constructor(
    private val repository: Repository
) {

    suspend fun downloadLanguageModels() {
        repository.downloadLanguageModels()
    }

    suspend fun downloadLanguageModel(language: Language) {
        repository.downloadLanguageModel(language)
    }

}