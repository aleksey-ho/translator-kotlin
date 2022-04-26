package com.example.translator_kotlin.domain.model

import java.util.*

data class Translate(
    var id: String?,
    var languageSource: Language,
    var languageTarget: Language,
    var date: Date,
    var textSource: String,
    var textTarget: String,
    var savedInHistory: Boolean = false,
    var savedInFavorites: Boolean = false
)