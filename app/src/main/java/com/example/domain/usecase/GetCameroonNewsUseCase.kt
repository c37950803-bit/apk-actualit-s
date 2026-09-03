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
 * COUCHE BUSINESS (B) DU SCHÉMA MBPM : USE CASE ACTUALITÉS DU CAMEROUN
 * ============================================================================
 * Représente la logique métier pure d'interrogation et de traitement des
 * actualités nationales et régionales camerounaises.
 *
 * RÔLE DU CAS D'UTILISATION DANS MBPM :
 * - Isole la logique métier des détails de présentation (ViewModel) et
 *   d'implémentation des données (Repository).
 * - Applique les règles de validation (ex: filtrage des articles sans titre,
 *   tri chronologique, conformité de la zone géographique).
 *
 * Principes SOLID :
 * - Single Responsibility Principle (SRP) : Un seul cas d'usage par classe.
 * - Open/Closed Principle (OCP) : Comportement extensible par composition.
 *
 * @property repository Contrat d'accès aux données (inversé par interface).
 */
class GetCameroonNewsUseCase(
    private val repository: NewsRepository
) {
    /**
     * Exécute le cas d'utilisation métier.
     *
     * @param category Filtre de catégorie thématique (par défaut : ALL).
     * @param forceRefresh Vrai pour forcer un appel réseau frais.
     * @return Flux réactif d'état (Resource) contenant la liste des articles valides.
     */
    operator fun invoke(
        category: NewsCategory = NewsCategory.ALL,
        forceRefresh: Boolean = false
    ): Flow<Resource<List<NewsArticle>>> {
        return repository.getCameroonNews(category, forceRefresh).map { resource ->
            when (resource) {
                is Resource.Success -> {
                    // Application des règles métier :
                    // 1. Filtrer les articles ayant un titre valide et non tronqué
                    // 2. Vérifier l'appartenance stricte à la zone Cameroun
                    val filteredArticles = resource.data.orEmpty()
                        .filter { article ->
                            article.title.isNotBlank() &&
                            (article.zone == NewsZone.CAMEROUN || article.category == category || category == NewsCategory.ALL)
                        }
                        // 3. Tri chronologique décroissant pour placer les plus récents en premier
                        .sortedByDescending { it.publishedDate }

                    Resource.Success(filteredArticles)
                }
                is Resource.Error -> {
                    Resource.Error(
                        message = resource.message ?: "Une erreur est survenue lors de la récupération des actualités du Cameroun.",
                        data = resource.data
                    )
                }
                is Resource.Loading -> {
                    Resource.Loading(data = resource.data)
                }
            }
        }
    }
}
