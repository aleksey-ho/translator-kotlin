package com.example.translator_kotlin.presentation.dialog

import android.os.Parcelable

interface IDialogUiConfig : Parcelable {

    val title: String

    val message: String

    val positiveButtonText: String?

    val negativeButtonText: String?
}