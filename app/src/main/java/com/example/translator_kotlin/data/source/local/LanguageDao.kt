package com.example.translator_kotlin.data.source.local

import androidx.room.*
import com.example.translator_kotlin.data.source.local.entity.LanguageEntity

@Dao
interface LanguageDao {

    @Query("SELECT * FROM Language")
    suspend fun getLanguages(): List<LanguageEntity>

    @Query("SELECT * FROM Language WHERE code = :code")
    suspend fun getLanguage(code: String): LanguageEntity?

    @Query("SELECT * FROM Language ORDER BY sourceLastUseDate DESC")
    suspend fun getRecentlyUsedSourceLanguage(): LanguageEntity

    @Query("SELECT * FROM Language ORDER BY targetLastUseDate DESC")
    suspend fun getRecentlyUsedTargetLanguage(): LanguageEntity

    @Query("SELECT * FROM Language WHERE sourceLastUseDate is not NULL ORDER BY sourceLastUseDate DESC")
    suspend fun getRecentlyUsedSourceLanguages(): List<LanguageEntity>

    @Query("SELECT * FROM Language WHERE targetLastUseDate is not NULL ORDER BY targetLastUseDate DESC")
    suspend fun getRecentlyUsedTargetLanguages(): List<LanguageEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertLanguage(task: LanguageEntity)

    @Update(onConflict = OnConflictStrategy.IGNORE)
    suspend fun update(LanguageEntity: LanguageEntity)

}
