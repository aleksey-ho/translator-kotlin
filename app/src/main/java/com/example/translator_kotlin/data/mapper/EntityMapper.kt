package com.example.translator_kotlin.data.mapper

import com.example.translator_kotlin.data.source.local.entity.LanguageEntity
import com.example.translator_kotlin.data.source.local.entity.TranslateEntity
import com.example.translator_kotlin.domain.model.Language
import com.example.translator_kotlin.domain.model.Translate
import java.util.*

fun Language.toEntity() = LanguageEntity(
    code = code,
    sourceLastUseDate = sourceLastUseDate,
    targetLastUseDate = targetLastUseDate,
    usageCounter = usageCounter,
)

fun LanguageEntity.plain() = Language(
    code = code,
    name = Locale(code).displayName.replaceFirstChar {
        if (it.isLowerCase()) it.titlecase(
            Locale.getDefault()
        ) else it.toString()
    },
    sourceLastUseDate = sourceLastUseDate,
    targetLastUseDate = targetLastUseDate,
    usageCounter = usageCounter,
)

fun List<LanguageEntity>.plainLanguageEntityList(): List<Language> {
    return this.map { it.plain() }
}

fun Translate.toEntity(): TranslateEntity {
    val translate = this
    return TranslateEntity(
        id = id ?: UUID.randomUUID().toString(),
        languageSourceCode = this.languageSource.code,
        languageTargetCode = languageTarget.code,
        date = date,
        textSource = textSource,
        textTarget = textTarget,
        savedInHistory = savedInHistory,
        savedInFavorites = savedInFavorites,
    ).apply {
        this.languageSource = translate.languageSource.toEntity()
        this.languageTarget = translate.languageTarget.toEntity()
    }
}

fun TranslateEntity.plain() = Translate(
    id = id,
    languageSource = languageSource.plain(),
    languageTarget = languageTarget.plain(),
    date = date,
    textSource = textSource,
    textTarget = textTarget,
    savedInHistory = savedInHistory,
    savedInFavorites = savedInFavorites,
)

fun List<TranslateEntity>.plainTranslateEntityList(): List<Translate> {
    return this.map { it.plain() }
}
