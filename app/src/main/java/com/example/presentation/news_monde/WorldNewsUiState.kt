package com.example.presentation.news_monde

import com.example.domain.model.NewsArticle
import com.example.domain.model.NewsCategory

/**
 * ============================================================================
 * COUCHE PRESENTATION (P) : ÉTAT IMMUABLE DE L'UI MONDE (MBPM)
 * ============================================================================
 */
data class WorldNewsUiState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val articles: List<NewsArticle> = emptyList(),
    val selectedCategory: NewsCategory = NewsCategory.ALL,
    val searchQuery: String = "",
    val errorMessage: String? = null,
    val selectedArticle: NewsArticle? = null
)
