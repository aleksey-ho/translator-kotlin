package com.example.translator_kotlin.presentation.langSelector

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.translator_kotlin.data.LangDirection
import com.example.translator_kotlin.domain.model.Language
import com.example.translator_kotlin.domain.usecase.GetLanguagesUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LangSelectionViewModel constructor(
    private var getLanguagesUseCase: GetLanguagesUseCase
) : ViewModel() {

    private var direction: LangDirection? = null

    fun setDirection(direction: LangDirection) {
        this.direction = direction
    }

    suspend fun getRecentlyUsedSourceLangs(): List<Language> {
        return if (direction == LangDirection.SOURCE)
            getLanguagesUseCase.getRecentlyUsedSourceLanguages()
        else
            getLanguagesUseCase.getRecentlyUsedTargetLanguages()
    }

    suspend fun getLanguages(): List<Language> {
        return getLanguagesUseCase.getLanguages()
    }

    fun updateLanguageUsage(language: Language, direction: LangDirection) {
        viewModelScope.launch(Dispatchers.IO) {
            getLanguagesUseCase.updateLanguageUsage(language, direction)
        }
    }

}