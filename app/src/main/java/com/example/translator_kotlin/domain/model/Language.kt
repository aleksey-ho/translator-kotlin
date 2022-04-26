package com.example.translator_kotlin.domain.model

import java.util.*

data class Language(
    var code: String,
    var name: String,
    var sourceLastUseDate: Date?,
    var targetLastUseDate: Date?,
    var usageCounter: Int
)
