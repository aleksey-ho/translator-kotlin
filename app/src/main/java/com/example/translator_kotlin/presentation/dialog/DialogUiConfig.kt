package com.example.translator_kotlin.presentation.dialog

import com.example.translator_kotlin.App
import com.example.translator_kotlin.R
import kotlinx.parcelize.Parcelize

val STANDARD_DIALOG_CONFIG = DialogUiConfig(
    title = App.app.resources.getString(R.string.error_title),
    message = App.app.resources.getString(R.string.error_default),
    positiveButtonText = App.app.resources.getString(R.string.ok),
    negativeButtonText = null
)

@Parcelize
data class DialogUiConfig(
    override val title: String,
    override var message: String,
    override val positiveButtonText: String? = null,
    override val negativeButtonText: String? = null
) : IDialogUiConfig