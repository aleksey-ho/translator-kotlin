package com.example.translator_kotlin.dagger.module

import com.example.translator_kotlin.App
import com.example.translator_kotlin.utils.SharedPreferenceHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class ApplicationModule {

    @Provides
    fun provideApp(): App {
        return App.app
    }

    @Provides
    internal fun provideSharedPreference(): SharedPreferenceHelper {
        return SharedPreferenceHelper()
    }

}