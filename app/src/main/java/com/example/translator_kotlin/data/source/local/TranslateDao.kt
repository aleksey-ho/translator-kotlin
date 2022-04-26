package com.example.translator_kotlin.data.source.local

import androidx.room.*
import com.example.translator_kotlin.data.source.local.entity.TranslateEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TranslateDao {

    @Query("SELECT * FROM Translate WHERE savedInHistory = 1")
    fun getHistory(): Flow<List<TranslateEntity>>

    @Query("SELECT * FROM Translate WHERE savedInFavorites = 1")
    fun getFavorites(): Flow<List<TranslateEntity>>

    @Query("SELECT * FROM Translate WHERE languageSourceCode = :languageSourceCode AND languageTargetCode = :languageTargetCode AND textSource = :textSource")
    suspend fun getTranslate(languageSourceCode: String, languageTargetCode: String, textSource: String): TranslateEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(task: TranslateEntity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(task: TranslateEntity)

    @Query("UPDATE Translate SET savedInHistory = 0")
    suspend fun clearHistory()

    @Query("UPDATE Translate SET savedInFavorites = 0")
    suspend fun clearFavorites()

    @Query("DELETE FROM Translate WHERE savedInHistory = 0 AND savedInFavorites = 0")
    suspend fun removeRedundantEntries()

}
