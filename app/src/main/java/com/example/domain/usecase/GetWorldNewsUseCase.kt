package com.example.domain.usecase

import com.example.core.util.Resource
import com.example.domain.model.NewsArticle
import com.example.domain.model.NewsCategory
import com.example.domain.model.NewsZone
import com.example.domain.repository.NewsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * ============================================================================
 * COUCHE BUSINESS (B) DU SCHÉMA MBPM : USE CASE ACTUALITÉS DU MONDE
 * ============================================================================
 * Cas d'utilisation métier gérant l'agrégation internationale et géopolitique.
 */
class GetWorldNewsUseCase(
    private val repository: NewsRepository
) {
    operator fun invoke(
        category: NewsCategory = NewsCategory.ALL,
        forceRefresh: Boolean = false
    ): Flow<Resource<List<NewsArticle>>> {
        return repository.getWorldNews(category, forceRefresh).map { resource ->
            when (resource) {
                is Resource.Success -> {
                    val validArticles = resource.data.orEmpty()
                        .filter { it.title.isNotBlank() && it.zone == NewsZone.MONDE }
                        .sortedByDescending { it.publishedDate }
                    Resource.Success(validArticles)
                }
                is Resource.Error -> Resource.Error(
                    message = resource.message ?: "Impossible de charger les actualités internationales.",
                    data = resource.data
                )
                is Resource.Loading -> Resource.Loading(data = resource.data)
            }
        }
    }
}
