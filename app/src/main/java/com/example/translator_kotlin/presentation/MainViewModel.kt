package com.example.translator_kotlin.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.translator_kotlin.App
import com.example.translator_kotlin.R
import com.example.translator_kotlin.domain.usecase.DownloadLanguageModelUseCase
import com.example.translator_kotlin.presentation.dialog.DialogUiConfig
import com.example.translator_kotlin.presentation.dialog.DialogViewModel
import com.example.translator_kotlin.presentation.dialog.STANDARD_DIALOG_CONFIG
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

    val isDialogVisible = MutableLiveData<Boolean>(false)
    var dialogConfig = MutableLiveData(STANDARD_DIALOG_CONFIG)
    var dialogViewModel = MutableLiveData(DialogViewModel())

    fun showInitDialog() {
        if (sharedPreferenceHelper.getBooleanValue(DATA_USAGE_WARNING, false)) {
            return
        }
        val title = R.string.init_langs_title
        val message = R.string.init_langs_description

        dialogConfig.value = DialogUiConfig(
            title = App.app.resources.getString(title),
            message = App.app.resources.getString(message),
            positiveButtonText = App.app.resources.getString(R.string.ok)
        )
        dialogViewModel.value = DialogViewModel(
            positiveClick = {
                sharedPreferenceHelper.saveBoolean(DATA_USAGE_WARNING, true)
                startDownload()
                hideDialog()
            }
        )

        isDialogVisible.postValue(true)
    }

    private fun hideDialog() {
        isDialogVisible.postValue(false)
    }

    private fun startDownload() {
        viewModelScope.launch {
            try {
                downloadLanguageModelUseCase.downloadLanguageModels()
            } catch (throwable: Throwable) {
                throwable.printStackTrace()
            }
        }
    }

}

