package com.example.translator_kotlin

import android.app.Application
import com.example.translator_kotlin.dagger.module.appModule
import com.example.translator_kotlin.dagger.module.dataModule
import com.example.translator_kotlin.dagger.module.domainModule
import com.example.translator_kotlin.domain.repository.Repository
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.runBlocking
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

open class App : Application() {

    private val scope = MainScope()

    val repository by inject<Repository>()

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@App)
            modules(listOf(appModule, dataModule, domainModule))
        }

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
