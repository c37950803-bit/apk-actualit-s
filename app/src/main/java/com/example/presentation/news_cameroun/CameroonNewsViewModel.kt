package com.example.presentation.news_cameroun

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.util.Resource
import com.example.domain.model.NewsArticle
import com.example.domain.model.NewsCategory
import com.example.domain.model.NewsZone
import com.example.domain.repository.NewsRepository
import com.example.domain.usecase.GetCameroonNewsUseCase
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
 * COUCHE PRESENTATION (P) DU SCHÉMA MBPM : VIEWMODEL ACTUALITÉS CAMEROUN
 * ============================================================================
 * Chef d'orchestre de la vue d'actualités du Cameroun.
 *
 * RÔLE DU VIEWMODEL DANS MBPM :
 * - Reçoit les intentions de l'utilisateur (filtrage catégorie, recherche, rafraîchissement).
 * - Invoque les UseCases de la couche Business [GetCameroonNewsUseCase] et [SearchNewsUseCase].
 * - Expose un état immuable [CameroonNewsUiState] via [StateFlow] observé par Jetpack Compose.
 * - Ne possède aucune dépendance vers le framework Android UI (séparation stricte des préoccupations).
 *
 * Principes SOLID :
 * - Single Responsibility Principle (SRP) : Gestion exclusive du cycle de vie et de l'état UI Cameroun.
 * - Dependency Inversion Principle (DIP) : Reçoit les UseCases par injection de constructeur.
 */
class CameroonNewsViewModel(
    private val getCameroonNewsUseCase: GetCameroonNewsUseCase,
    private val searchNewsUseCase: SearchNewsUseCase,
    private val repository: NewsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CameroonNewsUiState())
    val uiState: StateFlow<CameroonNewsUiState> = _uiState.asStateFlow()

    private var currentJob: Job? = null

    init {
        // Chargement initial des actualités camerounaises
        loadNews(category = NewsCategory.ALL, forceRefresh = false)
    }

    /**
     * Charge les actualités camerounaises pour la catégorie demandée.
     *
     * @param category Thématique (Politique, Économie, Sports, etc.).
     * @param forceRefresh Force l'appel au serveur distant.
     */
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

            getCameroonNewsUseCase(category = category, forceRefresh = forceRefresh).collectLatest { resource ->
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
                                errorMessage = resource.message ?: "Impossible de récupérer les actualités du Cameroun."
                            )
                        }
                    }
                    is Resource.Loading -> {
                        _uiState.update { state ->
                            state.copy(
                                isLoading = state.articles.isEmpty() && !state.isRefreshing
                            )
                        }
                    }
                }
            }
        }
    }

    /**
     * Rafraîchit les actualités du Cameroun (action Pull-to-refresh).
     */
    fun refresh() {
        loadNews(category = _uiState.value.selectedCategory, forceRefresh = true)
    }

    /**
     * Change la catégorie thématique active.
     */
    fun onCategorySelected(category: NewsCategory) {
        if (_uiState.value.selectedCategory != category) {
            loadNews(category = category, forceRefresh = false)
        }
    }

    /**
     * Recherche textuelle d'articles au Cameroun.
     */
    fun onSearchQueryChanged(query: String) {
        _uiState.update { it.copy(searchQuery = query) }

        currentJob?.cancel()
        if (query.trim().length < 2) {
            // Revenir à la catégorie courante si requête effacée
            loadNews(category = _uiState.value.selectedCategory, forceRefresh = false)
            return
        }

        currentJob = viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            searchNewsUseCase(query = query, zone = NewsZone.CAMEROUN).collectLatest { resource ->
                when (resource) {
                    is Resource.Success -> {
                        _uiState.update { state ->
                            state.copy(
                                isLoading = false,
                                articles = resource.data.orEmpty(),
                                errorMessage = if (resource.data.isNullOrEmpty()) "Aucun résultat pour '$query' au Cameroun." else null
                            )
                        }
                    }
                    is Resource.Error -> {
                        _uiState.update { state ->
                            state.copy(
                                isLoading = false,
                                errorMessage = resource.message
                            )
                        }
                    }
                    is Resource.Loading -> {
                        _uiState.update { it.copy(isLoading = true) }
                    }
                }
            }
        }
    }

    /**
     * Efface la recherche active.
     */
    fun clearSearch() {
        onSearchQueryChanged("")
    }

    /**
     * Sélectionne un article pour afficher la fiche détaillée.
     */
    fun onArticleSelected(article: NewsArticle?) {
        _uiState.update { it.copy(selectedArticle = article) }
    }

    /**
     * Ajoute ou retire un article des favoris locaux.
     */
    fun toggleFavorite(article: NewsArticle) {
        viewModelScope.launch {
            val updatedStatus = !article.isFavorite
            repository.toggleFavorite(article.id, updatedStatus)
            _uiState.update { state ->
                val updatedArticles = state.articles.map {
                    if (it.id == article.id) it.copy(isFavorite = updatedStatus) else it
                }
                val updatedSelected = if (state.selectedArticle?.id == article.id) {
                    state.selectedArticle.copy(isFavorite = updatedStatus)
                } else {
                    state.selectedArticle
                }
                state.copy(articles = updatedArticles, selectedArticle = updatedSelected)
            }
        }
    }
}
