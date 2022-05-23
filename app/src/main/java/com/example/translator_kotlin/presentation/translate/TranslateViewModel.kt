package com.example.translator_kotlin.presentation.translate

import android.content.Context
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
import com.example.translator_kotlin.utils.DownloadInProgressException
import com.example.translator_kotlin.utils.ResourcesProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.net.UnknownHostException
import javax.inject.Inject

@HiltViewModel
class TranslateViewModel @Inject constructor(
    private var getTranslatesUseCase: GetTranslatesUseCase,
    private var getLanguagesUseCase: GetLanguagesUseCase,
    private var downloadLanguageModelUseCase: DownloadLanguageModelUseCase,
    private val resourcesProvider: ResourcesProvider,
) : ViewModel() {

    private val _showErrorDialog: MutableStateFlow<Throwable?> = MutableStateFlow(null)
    val showErrorDialog: StateFlow<Throwable?>
        get() = _showErrorDialog

    val languageSource = MutableLiveData<Language>()
    val languageTarget = MutableLiveData<Language>()
    val textSource = MutableLiveData<String>()
    val translate = MutableLiveData<String>()
    val internetConnectionError = MutableLiveData<Boolean>(false)

    fun loadLanguages() {
        viewModelScope.launch {
            try {
                val recentlyUsedSourceLanguage = getLanguagesUseCase.getRecentlyUsedSourceLanguage()
                languageSource.postValue(recentlyUsedSourceLanguage)

                val recentlyUsedTargetLanguage = getLanguagesUseCase.getRecentlyUsedTargetLanguage()
                languageTarget.postValue(recentlyUsedTargetLanguage)

            } catch (throwable: Throwable) {
                throwable.printStackTrace()
            }
        }
    }

    fun setLanguageSource(language: Language) {
        languageSource.value = language
    }

    fun setLanguageTarget(language: Language) {
        languageTarget.value = language
    }

    /**
     * Translates text and saves result in database
     */
    fun translateText(text: String?) {
        translateText(text, true)
    }

    fun swapLanguages() {
        val tempLanguageSource = languageSource.value
        languageSource.value = languageTarget.value
        tempLanguageSource?.let { languageTarget.value = it }
        viewModelScope.launch {
            try {
                getLanguagesUseCase.updateLanguageUsage(
                    languageSource.value ?: return@launch,
                    LangDirection.SOURCE
                )
                getLanguagesUseCase.updateLanguageUsage(
                    languageTarget.value ?: return@launch,
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
                    languageSource.value ?: return@launch,
                    languageTarget.value ?: return@launch,
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
                    translate.value = resourcesProvider.getString(R.string.download_in_progress_error)
                } else {
                    internetConnectionError.postValue(false)
                    _showErrorDialog.value = error
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
                    languageSource.value ?: return@launch,
                    languageTarget.value ?: return@launch,
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