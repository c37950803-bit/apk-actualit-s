package com.example.presentation.news_cameroun

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.domain.model.NewsArticle
import com.example.presentation.common.components.ArticleDetailDialog
import com.example.presentation.common.components.CategoryChipsRow
import com.example.presentation.common.components.NewsCard
import com.example.presentation.common.components.NewsSearchTopBar

/**
 * ============================================================================
 * COUCHE PRESENTATION (P) DU SCHÉMA MBPM : ÉCRAN DES ACTUALITÉS DU CAMEROUN
 * ============================================================================
 * Composant Jetpack Compose autonome affichant l'agrégateur d'actualités
 * camerounaises avec architecture MVI/MVVM :
 * - Observation réactive via [collectAsStateWithLifecycle].
 * - Recherche instantanée.
 * - Filtres par catégories (Politique, Économie, Sports, etc.).
 * - États de chargement, d'erreur et de succès avec réactivité fluide.
 */
@Composable
fun CameroonNewsScreen(
    viewModel: CameroonNewsViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    // Boîte modale de détail d'article
    uiState.selectedArticle?.let { article ->
        ArticleDetailDialog(
            article = article,
            onDismiss = { viewModel.onArticleSelected(null) },
            onShare = { shareArticle(context, it) }
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag("cameroon_news_screen")
    ) {
        // Barre de recherche
        NewsSearchTopBar(
            query = uiState.searchQuery,
            onQueryChange = viewModel::onSearchQueryChanged,
            onClearQuery = viewModel::clearSearch,
            placeholder = "Rechercher au Cameroun (ex: Yaoundé, Lions Indomptables...)"
        )

        // Sélecteur de catégories
        CategoryChipsRow(
            selectedCategory = uiState.selectedCategory,
            onCategorySelected = viewModel::onCategorySelected
        )

        // Corps principal : Gestion des états réactifs
        Box(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
        ) {
            when {
                uiState.isLoading -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .testTag("loading_cameroon_news"),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Chargement des actualités du Cameroun...",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.outline
                        )
                    }
                }

                uiState.errorMessage != null && uiState.articles.isEmpty() -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp)
                            .testTag("error_cameroon_news"),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.ErrorOutline,
                            contentDescription = null,
                            modifier = Modifier.size(48.dp),
                            tint = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = uiState.errorMessage ?: "Erreur inconnue",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = viewModel::refresh,
                            modifier = Modifier.testTag("btn_retry_cameroon")
                        ) {
                            Icon(imageVector = Icons.Outlined.Refresh, contentDescription = null)
                            Spacer(modifier = Modifier.size(8.dp))
                            Text("Réessayer")
                        }
                    }
                }

                uiState.articles.isEmpty() -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp)
                            .testTag("empty_cameroon_news"),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Info,
                            contentDescription = null,
                            modifier = Modifier.size(48.dp),
                            tint = MaterialTheme.colorScheme.outline
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Aucune actualité trouvée pour ce filtre.",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.outline
                        )
                    }
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .testTag("list_cameroon_news")
                    ) {
                        items(
                            items = uiState.articles,
                            key = { it.id }
                        ) { article ->
                            NewsCard(
                                article = article,
                                onArticleClick = { viewModel.onArticleSelected(it) },
                                onToggleFavorite = { viewModel.toggleFavorite(it) },
                                onShareClick = { shareArticle(context, it) }
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * Fonction utilitaire de partage Android natif.
 */
private fun shareArticle(context: android.content.Context, article: NewsArticle) {
    val sendIntent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, "${article.title}\n\n${article.articleUrl}")
        type = "text/plain"
    }
    val shareIntent = Intent.createChooser(sendIntent, "Partager l'article")
    context.startActivity(shareIntent)
}
