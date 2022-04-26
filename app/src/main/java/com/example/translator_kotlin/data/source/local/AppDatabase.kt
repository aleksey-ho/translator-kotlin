package com.example.translator_kotlin.data.source.local

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.translator_kotlin.data.source.local.converter.Converters
import com.example.translator_kotlin.data.source.local.entity.LanguageEntity
import com.example.translator_kotlin.data.source.local.entity.TranslateEntity
import com.example.translator_kotlin.utils.Const

@Database(entities = [LanguageEntity::class, TranslateEntity::class], version = Const.ROOM_VERSION, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun languageDao(): LanguageDao

    abstract fun translateDao(): TranslateDao

    companion object {

        private var INSTANCE: AppDatabase? = null

        fun getAppDatabaseInstance(application: Application): AppDatabase {
            if (INSTANCE == null) {
                synchronized(this) {
                    INSTANCE = Room.databaseBuilder(
                        application,
                        AppDatabase::class.java,
                        "Translator.db"
                    )
                        .allowMainThreadQueries()
                        .build()
                }
            }
            return INSTANCE!!
        }
    }

}
