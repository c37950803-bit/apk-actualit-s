package com.example.data.repository

import com.example.core.util.Resource
import com.example.data.local.datasource.NewsLocalDataSource
import com.example.data.mapper.toDomain
import com.example.data.mapper.toEntity
import com.example.data.remote.datasource.NewsRemoteDataSource
import com.example.domain.model.NewsArticle
import com.example.domain.model.NewsCategory
import com.example.domain.model.NewsZone
import com.example.domain.repository.NewsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow

/**
 * ============================================================================
 * COUCHE DATA : IMPLÉMENTATION DU RÉSERVOIR D'ACTUALITÉS (REPOSITORY)
 * ============================================================================
 * Orchestre la récupération de données selon une stratégie "Hors-ligne d'abord" (Offline-First) :
 * 1. Émet immédiatement les données stockées en cache local (Room).
 * 2. Déclenche la requête réseau Retrofit si le cache est vide ou si rafraîchissement demandé.
 * 3. Délègue à la couche Mapper (M) la conversion DTO -> Domain et Domain -> Entity.
 * 4. Met à jour la base Room et émet les actualités fraîches.
 *
 * Principes SOLID :
 * - Dependency Inversion Principle (DIP) : Dépend de l'interface abstraite [NewsRepository].
 * - Single Responsibility Principle (SRP) : Coordonne les sources locale et distante.
 */
class NewsRepositoryImpl(
    private val remoteDataSource: NewsRemoteDataSource,
    private val localDataSource: NewsLocalDataSource
) : NewsRepository {

    override fun getCameroonNews(
        category: NewsCategory,
        forceRefresh: Boolean
    ): Flow<Resource<List<NewsArticle>>> = flow {
        emit(Resource.Loading())

        // 1. Lire le cache local
        val localEntities = if (category == NewsCategory.ALL) {
            localDataSource.getArticlesByZone(NewsZone.CAMEROUN.name).firstOrNull().orEmpty()
        } else {
            localDataSource.getArticlesByZoneAndCategory(NewsZone.CAMEROUN.name, category.id).firstOrNull().orEmpty()
        }

        val cachedArticles = localEntities.map { it.toDomain() }
        if (cachedArticles.isNotEmpty()) {
            emit(Resource.Success(cachedArticles))
        }

        // 2. Si rafraîchissement demandé ou cache vide, interroger l'API distante
        if (forceRefresh || cachedArticles.isEmpty()) {
            try {
                val dtos = remoteDataSource.fetchCameroonNews(category.apiCategory)
                // Utilisation de la couche Mapper (M) : DTO -> Domain
                val domainArticles = dtos.map { it.toDomain(NewsZone.CAMEROUN) }

                if (domainArticles.isNotEmpty()) {
                    // Sauvegarde dans Room via le Mapper : Domain -> Entity
                    localDataSource.saveArticles(domainArticles.map { it.toEntity() })
                    emit(Resource.Success(domainArticles))
                } else if (cachedArticles.isEmpty()) {
                    emit(Resource.Error("Aucune actualité disponible pour le Cameroun actuellement."))
                }
            } catch (e: Exception) {
                if (cachedArticles.isEmpty()) {
                    emit(Resource.Error(e.localizedMessage ?: "Échec de connexion au serveur d'actualités."))
                }
            }
        }
    }

    override fun getAfricaNews(
        category: NewsCategory,
        forceRefresh: Boolean
    ): Flow<Resource<List<NewsArticle>>> = flow {
        emit(Resource.Loading())

        val localEntities = if (category == NewsCategory.ALL) {
            localDataSource.getArticlesByZone(NewsZone.AFRIQUE.name).firstOrNull().orEmpty()
        } else {
            localDataSource.getArticlesByZoneAndCategory(NewsZone.AFRIQUE.name, category.id).firstOrNull().orEmpty()
        }

        val cached = localEntities.map { it.toDomain() }
        if (cached.isNotEmpty()) emit(Resource.Success(cached))

        if (forceRefresh || cached.isEmpty()) {
            try {
                val dtos = remoteDataSource.fetchAfricaNews(category.apiCategory)
                val domainArticles = dtos.map { it.toDomain(NewsZone.AFRIQUE) }
                if (domainArticles.isNotEmpty()) {
                    localDataSource.saveArticles(domainArticles.map { it.toEntity() })
                    emit(Resource.Success(domainArticles))
                }
            } catch (e: Exception) {
                if (cached.isEmpty()) {
                    emit(Resource.Error(e.localizedMessage ?: "Erreur de chargement pour l'Afrique."))
                }
            }
        }
    }

    override fun getWorldNews(
        category: NewsCategory,
        forceRefresh: Boolean
    ): Flow<Resource<List<NewsArticle>>> = flow {
        emit(Resource.Loading())

        val localEntities = if (category == NewsCategory.ALL) {
            localDataSource.getArticlesByZone(NewsZone.MONDE.name).firstOrNull().orEmpty()
        } else {
            localDataSource.getArticlesByZoneAndCategory(NewsZone.MONDE.name, category.id).firstOrNull().orEmpty()
        }

        val cached = localEntities.map { it.toDomain() }
        if (cached.isNotEmpty()) emit(Resource.Success(cached))

        if (forceRefresh || cached.isEmpty()) {
            try {
                val dtos = remoteDataSource.fetchWorldNews(category.apiCategory)
                val domainArticles = dtos.map { it.toDomain(NewsZone.MONDE) }
                if (domainArticles.isNotEmpty()) {
                    localDataSource.saveArticles(domainArticles.map { it.toEntity() })
                    emit(Resource.Success(domainArticles))
                }
            } catch (e: Exception) {
                if (cached.isEmpty()) {
                    emit(Resource.Error(e.localizedMessage ?: "Erreur de chargement pour l'International."))
                }
            }
        }
    }

    override fun searchNews(
        query: String,
        zone: NewsZone
    ): Flow<Resource<List<NewsArticle>>> = flow {
        emit(Resource.Loading())
        try {
            val dtos = remoteDataSource.searchNews(query)
            val mapped = dtos.map { it.toDomain(zone) }
            emit(Resource.Success(mapped))
        } catch (e: Exception) {
            // Recherche de repli dans le cache local
            val localResults = localDataSource.searchArticles(query, zone.name).firstOrNull().orEmpty()
            emit(Resource.Success(localResults.map { it.toDomain() }))
        }
    }

    override suspend fun getArticleById(articleId: String): NewsArticle? {
        return localDataSource.getArticleById(articleId)?.toDomain()
    }

    override suspend fun toggleFavorite(articleId: String, isFavorite: Boolean) {
        localDataSource.updateFavoriteStatus(articleId, isFavorite)
    }
}
