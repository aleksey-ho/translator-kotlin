package com.example.translator_kotlin.presentation.bookmark

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.translator_kotlin.App
import com.example.translator_kotlin.R
import com.example.translator_kotlin.domain.model.Translate
import com.example.translator_kotlin.domain.usecase.GetTranslatesUseCase
import com.example.translator_kotlin.presentation.dialog.DialogUiConfig
import com.example.translator_kotlin.presentation.dialog.DialogViewModel
import com.example.translator_kotlin.presentation.dialog.STANDARD_DIALOG_CONFIG
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

    val favorites = getTranslatesUseCase.favoriteTranslates.asLiveData()

    val history: StateFlow<List<Translate>> = getTranslatesUseCase.translatesInHistory
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val isDialogVisible = MutableLiveData<Boolean>(false)
    var dialogConfig = MutableLiveData(STANDARD_DIALOG_CONFIG)
    var dialogViewModel = MutableLiveData(DialogViewModel())

    // clearHistory = 0 - clear history, 1 - clear favorites
    fun clear(clearHistory: Boolean) {
        val title = if (clearHistory)
            R.string.view_pager_history
        else
            R.string.view_pager_favorites

        val message = if (clearHistory)
            R.string.delete_history_question
        else
            R.string.delete_favorites_question

        dialogConfig.value = DialogUiConfig(
            title = App.app.resources.getString(title),
            message = App.app.resources.getString(message),
            positiveButtonText = App.app.resources.getString(R.string.yes),
            negativeButtonText = App.app.resources.getString(R.string.cancel)
        )
        dialogViewModel.value = DialogViewModel(
            positiveClick = {
                if (clearHistory) {
                    clearHistory()
                } else {
                    clearFavorites()
                }
                hideErrorDialog()
            },
            negativeClick = {
                hideErrorDialog()
            }
        )

        isDialogVisible.postValue(true)
    }

    private fun hideErrorDialog() {
        isDialogVisible.postValue(false)
    }

    suspend fun saveAsFavorite(translate: Translate) {
        return getTranslatesUseCase.saveAsFavorite(translate)
    }

    suspend fun removeFromFavorites(translate: Translate) {
        return getTranslatesUseCase.removeFromFavorites(translate)
    }

    private fun clearHistory() {
        viewModelScope.launch {
            try {
                getTranslatesUseCase.clearHistory()
            } catch (throwable: Throwable) {
                throwable.printStackTrace()
            }
        }
    }

    private fun clearFavorites() {
        viewModelScope.launch {
            try {
                getTranslatesUseCase.clearFavorites()
            } catch (throwable: Throwable) {
                throwable.printStackTrace()
            }
        }
    }

}
