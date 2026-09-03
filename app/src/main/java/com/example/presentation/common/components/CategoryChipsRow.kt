package com.example.presentation.common.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.domain.model.NewsCategory

/**
 * ============================================================================
 * COUCHE PRESENTATION (P) : SÉLECTEUR DE CATÉGORIES THÉMATIQUES
 * ============================================================================
 * Ligne défilante horizontale de puces filtrantes Material 3 pour basculer
 * rapidement entre Politique, Économie, Sports, Technologie, etc.
 */
@Composable
fun CategoryChipsRow(
    selectedCategory: NewsCategory,
    onCategorySelected: (NewsCategory) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .testTag("category_chips_row"),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        NewsCategory.entries.forEach { category ->
            val isSelected = category == selectedCategory
            FilterChip(
                selected = isSelected,
                onClick = { onCategorySelected(category) },
                label = { Text(category.displayName) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = MaterialTheme.colorScheme.primary,
                    selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                ),
                modifier = Modifier.testTag("chip_category_${category.id}")
            )
        }
    }
}
