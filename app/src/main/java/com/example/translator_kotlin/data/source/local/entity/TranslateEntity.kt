package com.example.translator_kotlin.data.source.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "Translate")
data class TranslateEntity @JvmOverloads constructor(
    @PrimaryKey @ColumnInfo(name = "id") var id: String = UUID.randomUUID().toString(),
    @ColumnInfo(name = "languageSourceCode") var languageSourceCode: String,
    @ColumnInfo(name = "languageTargetCode") var languageTargetCode: String,
    @ColumnInfo(name = "date") var date: Date,
    @ColumnInfo(name = "textSource") var textSource: String,
    @ColumnInfo(name = "textTarget") var textTarget: String,
    @ColumnInfo(name = "savedInHistory") var savedInHistory: Boolean,
    @ColumnInfo(name = "savedInFavorites") var savedInFavorites: Boolean,
) {
    @Ignore
    lateinit var languageSource: LanguageEntity
    @Ignore
    lateinit var languageTarget: LanguageEntity
}
