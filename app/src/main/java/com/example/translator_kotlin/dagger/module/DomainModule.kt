package com.example.translator_kotlin.dagger.module

import com.example.translator_kotlin.domain.usecase.DownloadLanguageModelUseCase
import com.example.translator_kotlin.domain.usecase.GetLanguagesUseCase
import com.example.translator_kotlin.domain.usecase.GetTranslatesUseCase
import org.koin.dsl.module

val domainModule = module {

    factory {
        DownloadLanguageModelUseCase(repository = get())
    }

    factory {
        GetLanguagesUseCase(repository = get())
    }

    factory {
        GetTranslatesUseCase(repository = get())
    }

}

