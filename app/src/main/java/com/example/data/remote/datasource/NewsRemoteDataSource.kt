package com.example.data.remote.datasource

import com.example.data.remote.dto.NewsArticleDto

/**
 * ============================================================================
 * COUCHE DATA / REMOTE : CONTRAT DE SOURCE DISTANTE
 * ============================================================================
 * Définit les interactions directes avec l'infrastructure réseau.
 */
interface NewsRemoteDataSource {
    suspend fun fetchCameroonNews(category: String? = null): List<NewsArticleDto>
    suspend fun fetchAfricaNews(category: String? = null): List<NewsArticleDto>
    suspend fun fetchWorldNews(category: String? = null): List<NewsArticleDto>
    suspend fun searchNews(query: String, country: String? = null): List<NewsArticleDto>
}
