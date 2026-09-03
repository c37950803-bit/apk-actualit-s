package com.example.data.local.datasource

import com.example.data.local.entity.NewsEntity
import kotlinx.coroutines.flow.Flow

/**
 * ============================================================================
 * COUCHE DATA / LOCAL : CONTRAT DE SOURCE LOCALE ROOM
 * ============================================================================
 */
interface NewsLocalDataSource {
    fun getArticlesByZone(zone: String): Flow<List<NewsEntity>>
    fun getArticlesByZoneAndCategory(zone: String, categoryId: String): Flow<List<NewsEntity>>
    fun searchArticles(query: String, zone: String): Flow<List<NewsEntity>>
    suspend fun getArticleById(articleId: String): NewsEntity?
    fun getFavoriteArticles(): Flow<List<NewsEntity>>
    suspend fun saveArticles(articles: List<NewsEntity>)
    suspend fun updateFavoriteStatus(articleId: String, isFavorite: Boolean)
    suspend fun clearZoneCache(zone: String)
}
