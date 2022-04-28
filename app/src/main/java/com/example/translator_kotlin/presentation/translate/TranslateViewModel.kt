package com.example.translator_kotlin.presentation.translate

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.translator_kotlin.App
import com.example.translator_kotlin.R
import com.example.translator_kotlin.data.LangDirection
import com.example.translator_kotlin.domain.model.Language
import com.example.translator_kotlin.domain.usecase.DownloadLanguageModelUseCase
import com.example.translator_kotlin.domain.usecase.GetLanguagesUseCase
import com.example.translator_kotlin.domain.usecase.GetTranslatesUseCase
import com.example.translator_kotlin.presentation.dialog.DialogViewModel
import com.example.translator_kotlin.presentation.dialog.STANDARD_DIALOG_CONFIG
import com.example.translator_kotlin.utils.DownloadInProgressException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.net.UnknownHostException
import javax.inject.Inject

@HiltViewModel
class TranslateViewModel @Inject constructor(
    private var getTranslatesUseCase: GetTranslatesUseCase,
    private var getLanguagesUseCase: GetLanguagesUseCase,
    private var downloadLanguageModelUseCase: DownloadLanguageModelUseCase,
) : ViewModel() {

    private val _languageSource = MutableLiveData<Language>()
    val languageSource: LiveData<Language>
        get() = _languageSource

    private val _languageTarget = MutableLiveData<Language>()
    val languageTarget: LiveData<Language>
        get() = _languageTarget

    // Two-way databinding, exposing MutableLiveData
    val textSource = MutableLiveData<String>()
    val translate = MutableLiveData<String>()

    val internetConnectionError = MutableLiveData<Boolean>(false)

    val isDialogVisible = MutableLiveData<Boolean>(false)
    var errorDialogConfig = STANDARD_DIALOG_CONFIG
    val errorDialogViewModel = DialogViewModel(
        positiveClick = {
            hideErrorDialog()
        },
        negativeClick = {}
    )

    private fun showErrorDialog(ex: Throwable) {
        errorDialogConfig.message = ex.localizedMessage
        isDialogVisible.postValue(true)
    }

    private fun hideErrorDialog() {
        isDialogVisible.postValue(false)
    }

    fun loadLanguages() {
        viewModelScope.launch {
            try {
                val recentlyUsedSourceLanguage = getLanguagesUseCase.getRecentlyUsedSourceLanguage()
                _languageSource.postValue(recentlyUsedSourceLanguage)

                val recentlyUsedTargetLanguage = getLanguagesUseCase.getRecentlyUsedTargetLanguage()
                _languageTarget.postValue(recentlyUsedTargetLanguage)

            } catch (throwable: Throwable) {
                throwable.printStackTrace()
            }
        }
    }

    fun setLanguageSource(language: Language) {
        _languageSource.value = language
    }

    fun setLanguageTarget(language: Language) {
        _languageTarget.value = language
    }

    /**
     * Translates text and saves result in database
     */
    fun translateText(text: String?) {
        translateText(text, true)
    }

    fun swapLanguages() {
        val tempLanguageSource = _languageSource.value
        _languageSource.value = _languageTarget.value
        tempLanguageSource?.let { _languageTarget.value = it }
        viewModelScope.launch {
            try {
                getLanguagesUseCase.updateLanguageUsage(
                    _languageSource.value ?: return@launch,
                    LangDirection.SOURCE
                )
                getLanguagesUseCase.updateLanguageUsage(
                    _languageTarget.value ?: return@launch,
                    LangDirection.TARGET
                )
                translateText(textSource.value)
            } catch (throwable: Throwable) {
                throwable.printStackTrace()
            }
        }
    }

    fun translateText(text: String?, saveOnCompleted: Boolean) {
        if (text.isNullOrEmpty()) {
            clearTranslate()
            return
        }
        textSource.value = text ?: return
        viewModelScope.launch {
            try {
                val translate = getTranslatesUseCase.getTranslate(
                    _languageSource.value ?: return@launch,
                    _languageTarget.value ?: return@launch,
                    textSource.value ?: return@launch,
                )
                internetConnectionError.postValue(false)
                setTranslate(translate.textTarget)
                if (saveOnCompleted) {
                    saveTranslate()
                }
            } catch (error: Throwable) {
                if (error is UnknownHostException) {
                    internetConnectionError.postValue(true)
                    clearTranslate()
                } else if (error is DownloadInProgressException) {
                    internetConnectionError.postValue(false)
                    translate.value = App.app.resources.getString(R.string.download_in_progress_error)
                }
                else {
                    internetConnectionError.postValue(false)
                    showErrorDialog(error)
                }
            }
        }
    }

    fun setSourceText(value: String) {
        textSource.value = value
    }

    fun setTranslate(value: String) {
        translate.value = value
    }

    fun clearSourceText() {
        setSourceText("")
    }

    fun clearTranslate() {
        setTranslate("")
    }

    fun saveTranslate() {
        viewModelScope.launch {
            val textSource = textSource.value
            val translate = translate.value
            if (textSource == null || translate == null) {
                return@launch
            }
            try {
                getTranslatesUseCase.addTranslate(
                    textSource,
                    translate,
                    _languageSource.value ?: return@launch,
                    _languageTarget.value ?: return@launch,
                )
            } catch (throwable: Throwable) {
                throwable.printStackTrace()
            }
        }
    }

    fun downloadLanguageModel(language: Language) {
        viewModelScope.launch {
            try {
                downloadLanguageModelUseCase.downloadLanguageModel(language)
                translateText(textSource.value)
            } catch (throwable: Throwable) {
                throwable.printStackTrace()
            }
        }
    }

}