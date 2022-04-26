package com.example.translator_kotlin.data.source.remote

import android.util.LruCache
import androidx.lifecycle.MutableLiveData
import com.example.translator_kotlin.data.source.RemoteDataSource
import com.example.translator_kotlin.domain.model.Language
import com.example.translator_kotlin.domain.model.Translate
import com.example.translator_kotlin.utils.DownloadInProgressException
import com.google.android.gms.tasks.Tasks
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.common.model.RemoteModelManager
import com.google.mlkit.nl.translate.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.*

class RemoteDataSourceImpl : RemoteDataSource {

    private val modelManager: RemoteModelManager = RemoteModelManager.getInstance()
    private val availableModels = MutableLiveData<List<String>>()
    override var languageModelIsDownloading = false

    private val translators =
        object : LruCache<TranslatorOptions, Translator>(NUM_TRANSLATORS) {
            override fun create(options: TranslatorOptions): Translator {
                return Translation.getClient(options)
            }

            override fun entryRemoved(
                evicted: Boolean,
                key: TranslatorOptions,
                oldValue: Translator,
                newValue: Translator?
            ) {
                oldValue.close()
            }
        }

    override suspend fun getTranslate(
        langSource: Language,
        langTarget: Language,
        text: String
    ): Translate {
        if (languageModelIsDownloading) {
            throw DownloadInProgressException()
        }
        if (text.isEmpty()) {
            return Translate(
                id = text,
                languageSource = langSource,
                languageTarget = langTarget,
                date = Calendar.getInstance().time,
                textSource = text,
                textTarget = "",
                savedInHistory = false,
                savedInFavorites = false
            )
        }
        val options =
            TranslatorOptions.Builder()
                .setSourceLanguage(langSource.code)
                .setTargetLanguage(langTarget.code)
                .build()

        val translate = withContext(Dispatchers.IO) {
            translators[options].downloadModelIfNeeded()
                .continueWithTask { task ->
                    if (task.isSuccessful) {
                        return@continueWithTask translators[options].translate(text)
                    } else {
                        Tasks.forException(task.exception ?: Exception("Unknown error"))
                    }
                }.await()
        }

        return Translate(
            id = text,
            languageSource = langSource,
            languageTarget = langTarget,
            date = Calendar.getInstance().time,
            textSource = text,
            textTarget = translate,
            savedInHistory = false,
            savedInFavorites = false
        )
    }

    override fun loadRemoteLanguages(): List<String> {
        return TranslateLanguage.getAllLanguages()
    }

    // Updates the list of downloaded models available for local translation.
    private suspend fun fetchDownloadedModels() {
        val remoteModels =
            modelManager.getDownloadedModels(TranslateRemoteModel::class.java).await()
        availableModels.value = remoteModels.sortedBy { it.language }.map { it.language }
    }

    // Starts downloading a remote model for local translation.
    override suspend fun downloadLanguageModel(language: Language) {
        languageModelIsDownloading = true
        val model = getModel(TranslateLanguage.fromLanguageTag(language.code)!!)
        withContext(Dispatchers.IO) {
            modelManager.download(model, DownloadConditions.Builder().build()).await()
        }
        fetchDownloadedModels()
        languageModelIsDownloading = false
    }

    private fun getModel(languageCode: String): TranslateRemoteModel {
        return TranslateRemoteModel.Builder(languageCode).build()
    }

    companion object {
        private const val NUM_TRANSLATORS = 3
    }

}