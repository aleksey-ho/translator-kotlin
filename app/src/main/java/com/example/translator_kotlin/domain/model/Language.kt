package com.example.translator_kotlin.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class Language(
    var code: String,
    var name: String,
    var sourceLastUseDate: Date?,
    var targetLastUseDate: Date?,
    var usageCounter: Int
): Parcelable