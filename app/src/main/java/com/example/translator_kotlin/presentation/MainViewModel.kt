package com.example.translator_kotlin.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.translator_kotlin.domain.usecase.DownloadLanguageModelUseCase
import com.example.translator_kotlin.utils.SharedPreferenceHelper
import com.example.translator_kotlin.utils.SharedPreferenceHelper.Companion.DATA_USAGE_WARNING
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private var downloadLanguageModelUseCase: DownloadLanguageModelUseCase,
    private var sharedPreferenceHelper: SharedPreferenceHelper
) : ViewModel() {

    fun getShowInitDialog(): Boolean {
        return sharedPreferenceHelper.getBooleanValue(DATA_USAGE_WARNING, true)
    }

    fun downloadLanguageModels() {
        sharedPreferenceHelper.saveBoolean(DATA_USAGE_WARNING, false)
        viewModelScope.launch {
            try {
                downloadLanguageModelUseCase.downloadLanguageModels()
            } catch (throwable: Throwable) {
                throwable.printStackTrace()
            }
        }
    }

}

