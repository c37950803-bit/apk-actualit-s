package com.example.di

import android.content.Context
import com.example.core.util.Constants
import com.example.data.local.database.NewsDatabase
import com.example.data.local.datasource.NewsLocalDataSource
import com.example.data.local.datasource.NewsLocalDataSourceImpl
import com.example.data.remote.api.NewsApiService
import com.example.data.remote.datasource.NewsRemoteDataSource
import com.example.data.remote.datasource.NewsRemoteDataSourceImpl
import com.example.data.repository.NewsRepositoryImpl
import com.example.domain.repository.NewsRepository
import com.example.domain.usecase.GetAfricaNewsUseCase
import com.example.domain.usecase.GetCameroonNewsUseCase
import com.example.domain.usecase.GetWorldNewsUseCase
import com.example.domain.usecase.SearchNewsUseCase
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

/**
 * ============================================================================
 * COUCHE DI (DEPENDENCY INJECTION) : CONTENEUR D'INVERSION DE CONTRÔLE
 * ============================================================================
 * Centralise l'instanciation et la résolution des dépendances de l'application :
 * - Configuration réseau (OkHttp, Moshi, Retrofit)
 * - Base de données locale (Room Database & DAO)
 * - Sources de données (Remote & Local DataSources)
 * - Couche Mapper & Repository
 * - Cas d'utilisation métier (Use Cases)
 *
 * Principes SOLID :
 * - Inversion of Control (IoC) : Les clients ne créent pas leurs dépendances.
 * - Single Responsibility : Seule responsabilité d'assemblage du graphe d'objets.
 */
class AppContainer(private val context: Context) {

    // 1. Configuration de la sérialisation JSON avec Moshi
    val moshi: Moshi by lazy {
        Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    // 2. Configuration du client HTTP OkHttpClient avec intercepteur de journalisation
    val okHttpClient: OkHttpClient by lazy {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        OkHttpClient.Builder()
            .addInterceptor(logging)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    // 3. Configuration du client Retrofit
    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    // 4. Service d'API Retrofit
    val newsApiService: NewsApiService by lazy {
        retrofit.create(NewsApiService::class.java)
    }

    // 5. Base de données locale SQLite Room
    val database: NewsDatabase by lazy {
        NewsDatabase.getInstance(context)
    }

    // 6. Source de données distante (Remote DataSource)
    val remoteDataSource: NewsRemoteDataSource by lazy {
        NewsRemoteDataSourceImpl(
            apiService = newsApiService,
            apiKey = Constants.DEFAULT_API_KEY
        )
    }

    // 7. Source de données locale (Local DataSource)
    val localDataSource: NewsLocalDataSource by lazy {
        NewsLocalDataSourceImpl(
            newsDao = database.newsDao()
        )
    }

    // 8. Dépôt d'actualités (Repository - Couche Data implémentant le Domain)
    val newsRepository: NewsRepository by lazy {
        NewsRepositoryImpl(
            remoteDataSource = remoteDataSource,
            localDataSource = localDataSource
        )
    }

    // 9. Cas d'utilisation métier (Business Layer Use Cases)
    val getCameroonNewsUseCase: GetCameroonNewsUseCase by lazy {
        GetCameroonNewsUseCase(newsRepository)
    }

    val getAfricaNewsUseCase: GetAfricaNewsUseCase by lazy {
        GetAfricaNewsUseCase(newsRepository)
    }

    val getWorldNewsUseCase: GetWorldNewsUseCase by lazy {
        GetWorldNewsUseCase(newsRepository)
    }

    val searchNewsUseCase: SearchNewsUseCase by lazy {
        SearchNewsUseCase(newsRepository)
    }
}
