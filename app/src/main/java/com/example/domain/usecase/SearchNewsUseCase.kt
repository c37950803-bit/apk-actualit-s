package com.example.domain.usecase

import com.example.core.util.Resource
import com.example.domain.model.NewsArticle
import com.example.domain.model.NewsZone
import com.example.domain.repository.NewsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

/**
 * ============================================================================
 * COUCHE BUSINESS (B) DU SCHÉMA MBPM : USE CASE DE RECHERCHE D'ACTUALITÉS
 * ============================================================================
 * Cas d'utilisation permettant la recherche par mots-clés au sein d'une zone.
 */
class SearchNewsUseCase(
    private val repository: NewsRepository
) {
    operator fun invoke(
        query: String,
        zone: NewsZone
    ): Flow<Resource<List<NewsArticle>>> {
        val trimmedQuery = query.trim()
        if (trimmedQuery.length < 2) {
            return flowOf(Resource.Success(emptyList()))
        }
        return repository.searchNews(trimmedQuery, zone)
    }
}
