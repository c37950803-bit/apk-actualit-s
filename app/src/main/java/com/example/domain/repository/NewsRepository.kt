package com.example.domain.repository

import com.example.core.util.Resource
import com.example.domain.model.NewsArticle
import com.example.domain.model.NewsCategory
import com.example.domain.model.NewsZone
import kotlinx.coroutines.flow.Flow

/**
 * ============================================================================
 * COUCHE BUSINESS / DOMAIN (DÉPÔT MÉTIER ABSTRAIT)
 * ============================================================================
 * Interface définissant les opérations métier d'accès et de manipulation
 * des actualités pour les 3 zones cibles (Cameroun, Afrique, Monde).
 *
 * Principes SOLID :
 * - Dependency Inversion Principle (DIP) : La couche Domain dicte le contrat ;
 *   la couche Data est contrainte de l'implémenter.
 * - Interface Segregation Principle (ISP) : Méthodes ciblées et cohérentes.
 */
interface NewsRepository {

    /**
     * Récupère le flux des actualités spécifiques au Cameroun.
     * Met en œuvre une stratégie hors-ligne d'abord (Offline-First cache local Room).
     *
     * @param category Filtre thématique optionnel (Politique, Sport, etc.).
     * @param forceRefresh Force le rafraîchissement réseau via Retrofit.
     * @return Flow émettant les états de chargement, de succès et d'erreur.
     */
    fun getCameroonNews(
        category: NewsCategory = NewsCategory.ALL,
        forceRefresh: Boolean = false
    ): Flow<Resource<List<NewsArticle>>>

    /**
     * Récupère le flux des actualités de la zone Afrique.
     *
     * @param category Filtre thématique optionnel.
     * @param forceRefresh Force l'appel à l'API distante.
     */
    fun getAfricaNews(
        category: NewsCategory = NewsCategory.ALL,
        forceRefresh: Boolean = false
    ): Flow<Resource<List<NewsArticle>>>

    /**
     * Récupère le flux des actualités internationales / Monde.
     *
     * @param category Filtre thématique optionnel.
     * @param forceRefresh Force l'appel à l'API distante.
     */
    fun getWorldNews(
        category: NewsCategory = NewsCategory.ALL,
        forceRefresh: Boolean = false
    ): Flow<Resource<List<NewsArticle>>>

    /**
     * Effectue une recherche plein-texte d'actualités par mot-clé au sein d'une zone.
     *
     * @param query Terme de recherche saisi par l'utilisateur.
     * @param zone Périmètre géographique sélectionné.
     */
    fun searchNews(
        query: String,
        zone: NewsZone
    ): Flow<Resource<List<NewsArticle>>>

    /**
     * Récupère un article spécifique par son identifiant unique.
     */
    suspend fun getArticleById(articleId: String): NewsArticle?

    /**
     * Bascule l'état favori d'un article pour consultation ultérieure hors-ligne.
     */
    suspend fun toggleFavorite(articleId: String, isFavorite: Boolean)
}
