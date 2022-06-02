package com.example.translator_kotlin.dagger.module

import com.example.translator_kotlin.presentation.MainActivity
import com.example.translator_kotlin.presentation.MainRouter
import com.example.translator_kotlin.presentation.MainViewModel
import com.example.translator_kotlin.presentation.bookmark.BookmarkViewModel
import com.example.translator_kotlin.presentation.langSelector.LangSelectionViewModel
import com.example.translator_kotlin.presentation.translate.TranslateViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val appModule = module {

    viewModel {
        MainViewModel(
            downloadLanguageModelUseCase = get(),
            sharedPreferenceHelper = get()
        )
    }

    viewModel {
        TranslateViewModel(
            downloadLanguageModelUseCase = get(),
            getLanguagesUseCase = get(),
            getTranslatesUseCase = get(),
            resourcesProvider = get()
        )
    }

    viewModel {
        BookmarkViewModel(
            getTranslatesUseCase = get(),
        )
    }

    viewModel {
        LangSelectionViewModel(
            getLanguagesUseCase = get(),
        )
    }

    scope<MainActivity> {
        scoped {
            MainRouter()
        }
    }

}

