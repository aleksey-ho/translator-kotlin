package com.example.translator_kotlin

import android.app.Application
import com.example.translator_kotlin.domain.repository.Repository
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltAndroidApp
open class App : Application() {

    private val scope = MainScope()

    @Inject
    lateinit var repository: Repository

    override fun onCreate() {
        super.onCreate()
        loadLanguages()
    }

    private fun loadLanguages() {
        runBlocking {
            try {
                repository.loadLanguages()
            } catch (e: Throwable) {
                e.printStackTrace()
            }
        }
    }

    override fun onLowMemory() {
        super.onLowMemory()
        scope.cancel()
    }

}
