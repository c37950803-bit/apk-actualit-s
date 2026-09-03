package com.example.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.presentation.news_afrique.AfricaNewsViewModel
import com.example.presentation.news_cameroun.CameroonNewsViewModel
import com.example.presentation.news_monde.WorldNewsViewModel

/**
 * ============================================================================
 * COUCHE DI : FABRIQUE DE VIEWMODELS (VIEWMODEL PROVIDER FACTORY)
 * ============================================================================
 * Permet l'injection par constructeur des UseCases métiers dans les ViewModels
 * de présentation, respectant le principe d'inversion de dépendance.
 */
class AppViewModelFactory(
    private val appContainer: AppContainer
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(CameroonNewsViewModel::class.java) -> {
                CameroonNewsViewModel(
                    getCameroonNewsUseCase = appContainer.getCameroonNewsUseCase,
                    searchNewsUseCase = appContainer.searchNewsUseCase,
                    repository = appContainer.newsRepository
                ) as T
            }
            modelClass.isAssignableFrom(AfricaNewsViewModel::class.java) -> {
                AfricaNewsViewModel(
                    getAfricaNewsUseCase = appContainer.getAfricaNewsUseCase,
                    searchNewsUseCase = appContainer.searchNewsUseCase,
                    repository = appContainer.newsRepository
                ) as T
            }
            modelClass.isAssignableFrom(WorldNewsViewModel::class.java) -> {
                WorldNewsViewModel(
                    getWorldNewsUseCase = appContainer.getWorldNewsUseCase,
                    searchNewsUseCase = appContainer.searchNewsUseCase,
                    repository = appContainer.newsRepository
                ) as T
            }
            else -> throw IllegalArgumentException("Classe ViewModel inconnue : ${modelClass.name}")
        }
    }
}
