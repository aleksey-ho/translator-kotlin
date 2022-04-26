package com.example.translator_kotlin.dagger.module

import android.app.Application
import com.example.translator_kotlin.data.repository.RepositoryImpl
import com.example.translator_kotlin.data.source.LocalDataSource
import com.example.translator_kotlin.data.source.RemoteDataSource
import com.example.translator_kotlin.data.source.local.AppDatabase
import com.example.translator_kotlin.data.source.local.LocalDataSourceImpl
import com.example.translator_kotlin.data.source.remote.RemoteDataSourceImpl
import com.example.translator_kotlin.domain.repository.Repository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Singleton
    @Provides
    internal fun provideAppDatabase(application: Application): AppDatabase {
        return AppDatabase.getAppDatabaseInstance(application)
    }

    @Singleton
    @Provides
    fun provideLocalDataSource(appDatabase: AppDatabase): LocalDataSource {
        return LocalDataSourceImpl(appDatabase)
    }

    @Singleton
    @Provides
    fun provideRemoteDataSource(): RemoteDataSource {
        return RemoteDataSourceImpl()
    }

    @Singleton
    @Provides
    fun provideRepository(remoteDataSource: RemoteDataSource, localDataSource: LocalDataSource): Repository {
        return RepositoryImpl(
            remoteDataSource,
            localDataSource
        )
    }

}
