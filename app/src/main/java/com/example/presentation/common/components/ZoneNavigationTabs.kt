package com.example.presentation.common.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.TravelExplore
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.domain.model.NewsZone

/**
 * ============================================================================
 * COUCHE PRESENTATION (P) : ONGLETS DES ZONES GÉOGRAPHIQUES (MBPM)
 * ============================================================================
 * Permet de basculer instantanément entre les trois périmètres d'agrégation :
 * - Cameroun (Actualités nationales et régionales)
 * - Afrique (Actualités continentales et régionales)
 * - Monde (Actualités internationales et géopolitiques)
 */
@Composable
fun ZoneNavigationTabs(
    selectedZone: NewsZone,
    onZoneSelected: (NewsZone) -> Unit,
    modifier: Modifier = Modifier
) {
    val zones = NewsZone.entries

    TabRow(
        selectedTabIndex = zones.indexOf(selectedZone),
        modifier = modifier
            .fillMaxWidth()
            .testTag("zone_navigation_tabs"),
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.primary,
        indicator = { tabPositions ->
            val index = zones.indexOf(selectedZone)
            if (index in tabPositions.indices) {
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[index]),
                    height = 3.dp,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    ) {
        zones.forEachIndexed { index, zone ->
            val isSelected = zone == selectedZone
            val icon = when (zone) {
                NewsZone.CAMEROUN -> Icons.Filled.Flag
                NewsZone.AFRIQUE -> Icons.Filled.TravelExplore
                NewsZone.MONDE -> Icons.Filled.Public
            }

            Tab(
                selected = isSelected,
                onClick = { onZoneSelected(zone) },
                text = {
                    Text(
                        text = zone.displayName,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                },
                icon = {
                    Icon(
                        imageVector = icon,
                        contentDescription = zone.displayName
                    )
                },
                modifier = Modifier.testTag("tab_zone_${zone.name.lowercase()}")
            )
        }
    }
}
