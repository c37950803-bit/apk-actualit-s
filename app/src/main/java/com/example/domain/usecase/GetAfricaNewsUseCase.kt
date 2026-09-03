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
 * COUCHE BUSINESS (B) DU SCHÉMA MBPM : USE CASE ACTUALITÉS D'AFRIQUE
 * ============================================================================
 * Cas d'utilisation responsable de la récupération, du filtrage et de la
 * validation des actualités panafricaines.
 */
class GetAfricaNewsUseCase(
    private val repository: NewsRepository
) {
    operator fun invoke(
        category: NewsCategory = NewsCategory.ALL,
        forceRefresh: Boolean = false
    ): Flow<Resource<List<NewsArticle>>> {
        return repository.getAfricaNews(category, forceRefresh).map { resource ->
            when (resource) {
                is Resource.Success -> {
                    val validArticles = resource.data.orEmpty()
                        .filter { it.title.isNotBlank() && it.zone == NewsZone.AFRIQUE }
                        .sortedByDescending { it.publishedDate }
                    Resource.Success(validArticles)
                }
                is Resource.Error -> Resource.Error(
                    message = resource.message ?: "Impossible de charger les actualités d'Afrique.",
                    data = resource.data
                )
                is Resource.Loading -> Resource.Loading(data = resource.data)
            }
        }
    }
}
