package com.example.presentation.news_cameroun

import com.example.domain.model.NewsArticle
import com.example.domain.model.NewsCategory

/**
 * ============================================================================
 * COUCHE PRESENTATION (P) DU SCHÉMA MBPM : ÉTAT IMMUABLE DE L'UI CAMEROUN
 * ============================================================================
 * Modélise de façon déterministe et prédictive l'état réactif de l'écran
 * des actualités du Cameroun selon les principes MVI / MVVM :
 * - Unidirectional Data Flow (UDF)
 * - Single Source of Truth
 *
 * @property isLoading Vrai lors d'un appel réseau en arrière-plan.
 * @property isRefreshing Vrai lors d'un rafraîchissement manuel (Pull to refresh).
 * @property articles Liste filtrée des articles du Cameroun à afficher.
 * @property selectedCategory Catégorie thématique actuellement sélectionnée.
 * @property searchQuery Requête de recherche textuelle active.
 * @property errorMessage Message d'erreur localisé en cas d'incident.
 * @property selectedArticle Article ouvert dans la boîte modale de lecture.
 */
data class CameroonNewsUiState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val articles: List<NewsArticle> = emptyList(),
    val selectedCategory: NewsCategory = NewsCategory.ALL,
    val searchQuery: String = "",
    val errorMessage: String? = null,
    val selectedArticle: NewsArticle? = null
)
