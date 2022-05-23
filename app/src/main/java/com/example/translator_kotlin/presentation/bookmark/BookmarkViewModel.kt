package com.example.translator_kotlin.presentation.bookmark

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.translator_kotlin.domain.model.Translate
import com.example.translator_kotlin.domain.usecase.GetTranslatesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookmarkViewModel @Inject constructor(
    private var getTranslatesUseCase: GetTranslatesUseCase,
) : ViewModel() {

    val history: StateFlow<List<Translate>> = getTranslatesUseCase.translatesInHistory
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    val favorites: StateFlow<List<Translate>> = getTranslatesUseCase.favoriteTranslates
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun saveAsFavorite(translate: Translate) {
        viewModelScope.launch {
            try {
                getTranslatesUseCase.saveAsFavorite(translate)
            } catch (throwable: Throwable) {
                throwable.printStackTrace()
            }
        }
    }

    fun removeFromFavorites(translate: Translate) {
        viewModelScope.launch {
            try {
                getTranslatesUseCase.removeFromFavorites(translate)
            } catch (throwable: Throwable) {
                throwable.printStackTrace()
            }
        }
    }

    fun clearHistory() {
        viewModelScope.launch {
            try {
                getTranslatesUseCase.clearHistory()
            } catch (throwable: Throwable) {
                throwable.printStackTrace()
            }
        }
    }

    fun clearFavorites() {
        viewModelScope.launch {
            try {
                getTranslatesUseCase.clearFavorites()
            } catch (throwable: Throwable) {
                throwable.printStackTrace()
            }
        }
    }

}
