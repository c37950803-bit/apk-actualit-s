package com.example.di

/**
 * ============================================================================
 * COUCHE DI : MODULES DAGGER HILT POUR PROJETS MULTI-MODULES GRADLE
 * ============================================================================
 * Dans une configuration de build utilisant le plugin Gradle Dagger Hilt
 * (`com.google.dagger.hilt.android`), voici la configuration complète
 * des modules d'injection :
 *
 * ```kotlin
 * // 1. Module Réseau (dans le module :core ou :data)
 * @Module
 * @InstallIn(SingletonComponent::class)
 * object NetworkModule {
 *     @Provides
 *     @Singleton
 *     fun provideMoshi(): Moshi = Moshi.Builder()
 *         .add(KotlinJsonAdapterFactory())
 *         .build()
 *
 *     @Provides
 *     @Singleton
 *     fun provideOkHttpClient(): OkHttpClient = OkHttpClient.Builder()
 *         .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY })
 *         .connectTimeout(30, TimeUnit.SECONDS)
 *         .build()
 *
 *     @Provides
 *     @Singleton
 *     fun provideRetrofit(okHttpClient: OkHttpClient, moshi: Moshi): Retrofit =
 *         Retrofit.Builder()
 *             .baseUrl(Constants.BASE_URL)
 *             .client(okHttpClient)
 *             .addConverterFactory(MoshiConverterFactory.create(moshi))
 *             .build()
 *
 *     @Provides
 *     @Singleton
 *     fun provideNewsApiService(retrofit: Retrofit): NewsApiService =
 *         retrofit.create(NewsApiService::class.java)
 * }
 *
 * // 2. Module Base de données Room (dans le module :data)
 * @Module
 * @InstallIn(SingletonComponent::class)
 * object DatabaseModule {
 *     @Provides
 *     @Singleton
 *     fun provideDatabase(@ApplicationContext context: Context): NewsDatabase =
 *         NewsDatabase.getInstance(context)
 *
 *     @Provides
 *     @Singleton
 *     fun provideNewsDao(database: NewsDatabase): NewsDao = database.newsDao()
 * }
 *
 * // 3. Module Repository (DIP : Liaison Interface -> Implémentation)
 * @Module
 * @InstallIn(SingletonComponent::class)
 * abstract class RepositoryModule {
 *     @Binds
 *     @Singleton
 *     abstract fun bindNewsRepository(
 *         impl: NewsRepositoryImpl
 *     ): NewsRepository
 * }
 *
 * // 4. Module UseCases Métier (dans le module :domain)
 * @Module
 * @InstallIn(ViewModelComponent::class)
 * object UseCaseModule {
 *     @Provides
 *     @ViewModelScoped
 *     fun provideGetCameroonNewsUseCase(repository: NewsRepository): GetCameroonNewsUseCase =
 *         GetCameroonNewsUseCase(repository)
 *
 *     @Provides
 *     @ViewModelScoped
 *     fun provideGetAfricaNewsUseCase(repository: NewsRepository): GetAfricaNewsUseCase =
 *         GetAfricaNewsUseCase(repository)
 *
 *     @Provides
 *     @ViewModelScoped
 *     fun provideGetWorldNewsUseCase(repository: NewsRepository): GetWorldNewsUseCase =
 *         GetWorldNewsUseCase(repository)
 * }
 * ```
 */
object HiltModulesDocumentation
