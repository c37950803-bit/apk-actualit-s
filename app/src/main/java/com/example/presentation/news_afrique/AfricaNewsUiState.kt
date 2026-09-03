package com.example.presentation.news_afrique

import com.example.domain.model.NewsArticle
import com.example.domain.model.NewsCategory

/**
 * ============================================================================
 * COUCHE PRESENTATION (P) : ÉTAT IMMUABLE DE L'UI AFRIQUE (MBPM)
 * ============================================================================
 */
data class AfricaNewsUiState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val articles: List<NewsArticle> = emptyList(),
    val selectedCategory: NewsCategory = NewsCategory.ALL,
    val searchQuery: String = "",
    val errorMessage: String? = null,
    val selectedArticle: NewsArticle? = null
)
