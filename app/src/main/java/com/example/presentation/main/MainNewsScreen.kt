package com.example.presentation.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountTree
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Badge
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.domain.model.NewsZone
import com.example.presentation.common.components.ZoneNavigationTabs
import com.example.presentation.news_afrique.AfricaNewsScreen
import com.example.presentation.news_afrique.AfricaNewsViewModel
import com.example.presentation.news_cameroun.CameroonNewsScreen
import com.example.presentation.news_cameroun.CameroonNewsViewModel
import com.example.presentation.news_monde.WorldNewsScreen
import com.example.presentation.news_monde.WorldNewsViewModel

/**
 * ============================================================================
 * COUCHE PRESENTATION (P) : ÉCRAN PRINCIPAL COORDONNATEUR (MBPM)
 * ============================================================================
 * Coordonne les trois zones géographiques de l'agrégateur :
 * 1. CAMEROUN
 * 2. AFRIQUE
 * 3. MONDE
 *
 * Intègre également une modale pédagogique détaillant l'architecture MBPM
 * (Model, Business, Presentation, Mapper) et ses principes SOLID.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainNewsScreen(
    cameroonViewModel: CameroonNewsViewModel,
    africaViewModel: AfricaNewsViewModel,
    worldViewModel: WorldNewsViewModel,
    modifier: Modifier = Modifier
) {
    var selectedZone by remember { mutableStateOf(NewsZone.CAMEROUN) }
    var showArchitectureDialog by remember { mutableStateOf(false) }

    if (showArchitectureDialog) {
        MbpmArchitectureDialog(onDismiss = { showArchitectureDialog = false })
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .testTag("main_news_screen"),
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Newspaper,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(modifier = Modifier.size(10.dp))
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "ActuMBPM",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.size(6.dp))
                                Badge(
                                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                                ) {
                                    Text(text = "MBPM", fontWeight = FontWeight.Bold)
                                }
                            }
                            Text(
                                text = "Cameroun • Afrique • Monde",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.outline
                            )
                        }
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            when (selectedZone) {
                                NewsZone.CAMEROUN -> cameroonViewModel.refresh()
                                NewsZone.AFRIQUE -> africaViewModel.refresh()
                                NewsZone.MONDE -> worldViewModel.refresh()
                            }
                        },
                        modifier = Modifier.testTag("btn_top_refresh")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Rafraîchir"
                        )
                    }

                    IconButton(
                        onClick = { showArchitectureDialog = true },
                        modifier = Modifier.testTag("btn_top_architecture")
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccountTree,
                            contentDescription = "Architecture MBPM",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Barre d'onglets pour basculer entre Cameroun, Afrique et Monde
            ZoneNavigationTabs(
                selectedZone = selectedZone,
                onZoneSelected = { selectedZone = it }
            )

            // Affichage de l'écran Compose correspondant à la zone sélectionnée
            Box(modifier = Modifier.fillMaxSize()) {
                when (selectedZone) {
                    NewsZone.CAMEROUN -> CameroonNewsScreen(viewModel = cameroonViewModel)
                    NewsZone.AFRIQUE -> AfricaNewsScreen(viewModel = africaViewModel)
                    NewsZone.MONDE -> WorldNewsScreen(viewModel = worldViewModel)
                }
            }
        }
    }
}

/**
 * Modale pédagogique affichant les détails de l'architecture MBPM.
 */
@Composable
fun MbpmArchitectureDialog(
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = modifier
                .fillMaxWidth(0.92f)
                .clip(RoundedCornerShape(24.dp))
                .testTag("mbpm_architecture_dialog"),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 8.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Architecture MBPM & SOLID",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Fermer")
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                ArchitectureLayerItem(
                    layer = "M - Model",
                    description = "Structures de données scindées : Data Models (DTOs JSON Retrofit) et Domain Models (NewsArticle pur en Kotlin).",
                    color = MaterialTheme.colorScheme.primary
                )

                ArchitectureLayerItem(
                    layer = "B - Business",
                    description = "Cœur métier : UseCases (GetCameroonNewsUseCase, etc.), validation et contrats d'interfaces abstraits (NewsRepository).",
                    color = MaterialTheme.colorScheme.secondary
                )

                ArchitectureLayerItem(
                    layer = "P - Presentation",
                    description = "Interface déclarative Jetpack Compose, ViewModels avec StateFlow (UDF, MVI/MVVM), réactivité et gestion des états d'erreur.",
                    color = MaterialTheme.colorScheme.tertiary
                )

                ArchitectureLayerItem(
                    layer = "M - Mapper",
                    description = "Couche d'isolation cruciale : NewsMapper convertit DTO -> Domain et Entity -> Domain. Protège l'UI contre les ruptures de contrat API.",
                    color = MaterialTheme.colorScheme.error
                )

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "Modules Gradle : :core • :data • :domain • :features:news",
                    style = MaterialTheme.typography.labelSmall,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.outline
                )
            }
        }
    }
}

@Composable
private fun ArchitectureLayerItem(
    layer: String,
    description: String,
    color: androidx.compose.ui.graphics.Color
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .background(color.copy(alpha = 0.08f), RoundedCornerShape(12.dp))
            .padding(12.dp)
    ) {
        Text(
            text = layer,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            color = color
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = description,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
