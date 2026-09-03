package com.example.presentation.news_monde

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.util.Resource
import com.example.domain.model.NewsArticle
import com.example.domain.model.NewsCategory
import com.example.domain.model.NewsZone
import com.example.domain.repository.NewsRepository
import com.example.domain.usecase.GetWorldNewsUseCase
import com.example.domain.usecase.SearchNewsUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ============================================================================
 * COUCHE PRESENTATION (P) : VIEWMODEL ACTUALITÉS MONDE (MBPM)
 * ============================================================================
 */
class WorldNewsViewModel(
    private val getWorldNewsUseCase: GetWorldNewsUseCase,
    private val searchNewsUseCase: SearchNewsUseCase,
    private val repository: NewsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(WorldNewsUiState())
    val uiState: StateFlow<WorldNewsUiState> = _uiState.asStateFlow()

    private var currentJob: Job? = null

    init {
        loadNews(category = NewsCategory.ALL, forceRefresh = false)
    }

    fun loadNews(category: NewsCategory = _uiState.value.selectedCategory, forceRefresh: Boolean = false) {
        currentJob?.cancel()
        currentJob = viewModelScope.launch {
            _uiState.update {
                it.copy(
                    selectedCategory = category,
                    searchQuery = "",
                    isRefreshing = forceRefresh,
                    isLoading = !forceRefresh && it.articles.isEmpty(),
                    errorMessage = null
                )
            }

            getWorldNewsUseCase(category = category, forceRefresh = forceRefresh).collectLatest { resource ->
                when (resource) {
                    is Resource.Success -> {
                        _uiState.update { state ->
                            state.copy(
                                isLoading = false,
                                isRefreshing = false,
                                articles = resource.data.orEmpty(),
                                errorMessage = null
                            )
                        }
                    }
                    is Resource.Error -> {
                        _uiState.update { state ->
                            state.copy(
                                isLoading = false,
                                isRefreshing = false,
                                errorMessage = resource.message ?: "Impossible de récupérer les actualités internationales."
                            )
                        }
                    }
                    is Resource.Loading -> {
                        _uiState.update { it.copy(isLoading = it.articles.isEmpty() && !it.isRefreshing) }
                    }
                }
            }
        }
    }

    fun refresh() = loadNews(category = _uiState.value.selectedCategory, forceRefresh = true)

    fun onCategorySelected(category: NewsCategory) {
        if (_uiState.value.selectedCategory != category) {
            loadNews(category = category, forceRefresh = false)
        }
    }

    fun onSearchQueryChanged(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
        currentJob?.cancel()
        if (query.trim().length < 2) {
            loadNews(category = _uiState.value.selectedCategory, forceRefresh = false)
            return
        }

        currentJob = viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            searchNewsUseCase(query = query, zone = NewsZone.MONDE).collectLatest { resource ->
                when (resource) {
                    is Resource.Success -> {
                        _uiState.update { state ->
                            state.copy(
                                isLoading = false,
                                articles = resource.data.orEmpty(),
                                errorMessage = if (resource.data.isNullOrEmpty()) "Aucun résultat pour '$query' dans le Monde." else null
                            )
                        }
                    }
                    is Resource.Error -> _uiState.update { it.copy(isLoading = false, errorMessage = resource.message) }
                    is Resource.Loading -> _uiState.update { it.copy(isLoading = true) }
                }
            }
        }
    }

    fun clearSearch() = onSearchQueryChanged("")

    fun onArticleSelected(article: NewsArticle?) {
        _uiState.update { it.copy(selectedArticle = article) }
    }

    fun toggleFavorite(article: NewsArticle) {
        viewModelScope.launch {
            val updatedStatus = !article.isFavorite
            repository.toggleFavorite(article.id, updatedStatus)
            _uiState.update { state ->
                val updatedArticles = state.articles.map {
                    if (it.id == article.id) it.copy(isFavorite = updatedStatus) else it
                }
                state.copy(articles = updatedArticles)
            }
        }
    }
}
