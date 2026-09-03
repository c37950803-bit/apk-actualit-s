package com.example.data.local.datasource

import com.example.data.local.dao.NewsDao
import com.example.data.local.entity.NewsEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

/**
 * ============================================================================
 * COUCHE DATA / LOCAL : IMPLÉMENTATION DE LA SOURCE LOCALE ROOM
 * ============================================================================
 */
class NewsLocalDataSourceImpl(
    private val newsDao: NewsDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : NewsLocalDataSource {

    override fun getArticlesByZone(zone: String): Flow<List<NewsEntity>> =
        newsDao.getArticlesByZone(zone).flowOn(ioDispatcher)

    override fun getArticlesByZoneAndCategory(zone: String, categoryId: String): Flow<List<NewsEntity>> =
        newsDao.getArticlesByZoneAndCategory(zone, categoryId).flowOn(ioDispatcher)

    override fun searchArticles(query: String, zone: String): Flow<List<NewsEntity>> =
        newsDao.searchArticles(query, zone).flowOn(ioDispatcher)

    override suspend fun getArticleById(articleId: String): NewsEntity? =
        withContext(ioDispatcher) { newsDao.getArticleById(articleId) }

    override fun getFavoriteArticles(): Flow<List<NewsEntity>> =
        newsDao.getFavoriteArticles().flowOn(ioDispatcher)

    override suspend fun saveArticles(articles: List<NewsEntity>) =
        withContext(ioDispatcher) { newsDao.insertArticles(articles) }

    override suspend fun updateFavoriteStatus(articleId: String, isFavorite: Boolean) =
        withContext(ioDispatcher) { newsDao.updateFavoriteStatus(articleId, isFavorite) }

    override suspend fun clearZoneCache(zone: String) =
        withContext(ioDispatcher) { newsDao.clearNonFavoriteArticlesByZone(zone) }
}
