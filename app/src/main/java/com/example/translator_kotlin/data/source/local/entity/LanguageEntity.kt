package com.example.translator_kotlin.data.source.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.translator_kotlin.data.LangDirection
import java.util.*

@Entity(tableName = "Language")
data class LanguageEntity @JvmOverloads constructor(
    @PrimaryKey @ColumnInfo(name = "code") var code: String = "",
    @ColumnInfo(name = "sourceLastUseDate") var sourceLastUseDate: Date? = null,
    @ColumnInfo(name = "targetLastUseDate") var targetLastUseDate: Date? = null,
    @ColumnInfo(name = "usageCounter") var usageCounter: Int = 0
) {

    fun updateUsage(direction: LangDirection) {
        usageCounter++
        if (direction == LangDirection.SOURCE)
            sourceLastUseDate = Calendar.getInstance().time
        else
            targetLastUseDate = Calendar.getInstance().time
    }

}
