package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.local.entity.NewsEntity
import kotlinx.coroutines.flow.Flow

/**
 * ============================================================================
 * COUCHE DATA / LOCAL : DATA ACCESS OBJECT (DAO) ROOM
 * ============================================================================
 * Définit les requêtes SQL locales de persistance, de filtrage par zone
 * (Cameroun, Afrique, Monde) et de gestion des favoris.
 */
@Dao
interface NewsDao {

    @Query("SELECT * FROM news_articles WHERE zoneName = :zone ORDER BY publishedDate DESC")
    fun getArticlesByZone(zone: String): Flow<List<NewsEntity>>

    @Query("SELECT * FROM news_articles WHERE zoneName = :zone AND categoryId = :categoryId ORDER BY publishedDate DESC")
    fun getArticlesByZoneAndCategory(zone: String, categoryId: String): Flow<List<NewsEntity>>

    @Query("SELECT * FROM news_articles WHERE (title LIKE '%' || :query || '%' OR description LIKE '%' || :query || '%') AND zoneName = :zone ORDER BY publishedDate DESC")
    fun searchArticles(query: String, zone: String): Flow<List<NewsEntity>>

    @Query("SELECT * FROM news_articles WHERE id = :articleId LIMIT 1")
    suspend fun getArticleById(articleId: String): NewsEntity?

    @Query("SELECT * FROM news_articles WHERE isFavorite = 1 ORDER BY cachedAtTimestamp DESC")
    fun getFavoriteArticles(): Flow<List<NewsEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticles(articles: List<NewsEntity>)

    @Query("UPDATE news_articles SET isFavorite = :isFavorite WHERE id = :articleId")
    suspend fun updateFavoriteStatus(articleId: String, isFavorite: Boolean)

    @Query("DELETE FROM news_articles WHERE zoneName = :zone AND isFavorite = 0")
    suspend fun clearNonFavoriteArticlesByZone(zone: String)
}
