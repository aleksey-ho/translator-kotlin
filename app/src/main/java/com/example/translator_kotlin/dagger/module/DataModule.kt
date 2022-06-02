package com.example.translator_kotlin.dagger.module

import com.example.translator_kotlin.data.repository.RepositoryImpl
import com.example.translator_kotlin.data.source.LocalDataSource
import com.example.translator_kotlin.data.source.RemoteDataSource
import com.example.translator_kotlin.data.source.local.AppDatabase
import com.example.translator_kotlin.data.source.local.LocalDataSourceImpl
import com.example.translator_kotlin.data.source.remote.RemoteDataSourceImpl
import com.example.translator_kotlin.domain.repository.Repository
import com.example.translator_kotlin.utils.ResourcesProvider
import com.example.translator_kotlin.utils.SharedPreferenceHelper
import org.koin.dsl.module


val dataModule = module {

    single {
        SharedPreferenceHelper(context = get())
    }

    single {
        AppDatabase.getAppDatabaseInstance(application = get())
    }

    single<LocalDataSource> {
        LocalDataSourceImpl(appDatabase = get())
    }

    single<RemoteDataSource> {
        RemoteDataSourceImpl()
    }

    single<Repository> {
        RepositoryImpl(
            remoteDataSource = get(),
            localDataSource = get()
        )
    }

    single {
        ResourcesProvider(context = get())
    }

}

