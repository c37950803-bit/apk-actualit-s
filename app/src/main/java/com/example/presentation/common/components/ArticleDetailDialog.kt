package com.example.presentation.common.components

import android.content.Intent
import android.net.Uri
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
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.OpenInBrowser
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.SubcomposeAsyncImage
import com.example.domain.model.NewsArticle

/**
 * ============================================================================
 * COUCHE PRESENTATION (P) : BOÎTE MODALE DU DÉTAIL D'ARTICLE
 * ============================================================================
 * Affiche la vue complète d'un article sélectionné : titre intégral, métadonnées,
 * image grand format, texte assaini et bouton d'ouverture dans le navigateur web.
 */
@Composable
fun ArticleDetailDialog(
    article: NewsArticle,
    onDismiss: () -> Unit,
    onShare: (NewsArticle) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = modifier
                .fillMaxWidth(0.94f)
                .clip(RoundedCornerShape(24.dp))
                .testTag("article_detail_dialog"),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 6.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Barre d'outils supérieure de la modale
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${article.zone.displayName} • ${article.sourceName}",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )

                    Row {
                        IconButton(
                            onClick = { onShare(article) },
                            modifier = Modifier.testTag("btn_modal_share")
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Share,
                                contentDescription = "Partager"
                            )
                        }
                        IconButton(
                            onClick = onDismiss,
                            modifier = Modifier.testTag("btn_modal_close")
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Close,
                                contentDescription = "Fermer"
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Titre complet
                Text(
                    text = article.title,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Date de parution
                Text(
                    text = "Publié le ${article.publishedDate}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.outline
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Image d'illustration si disponible
                if (!article.imageUrl.isNullOrBlank()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clip(RoundedCornerShape(16.dp))
                    ) {
                        SubcomposeAsyncImage(
                            model = article.imageUrl,
                            contentDescription = article.title,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize(),
                            loading = {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator(modifier = Modifier.size(28.dp))
                                }
                            }
                        )
                    }
                    Spacer(modifier = Modifier.height(14.dp))
                }

                // Contenu ou description
                Text(
                    text = article.content.ifEmpty { article.description },
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = MaterialTheme.typography.bodyLarge.lineHeight
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Boutons d'action : ouvrir la source originale
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = {
                            if (article.articleUrl.isNotBlank()) {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(article.articleUrl))
                                context.startActivity(intent)
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("btn_open_browser")
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.OpenInBrowser,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.size(6.dp))
                        Text("Lire l'article")
                    }

                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.testTag("btn_close_detail")
                    ) {
                        Text("Retour")
                    }
                }
            }
        }
    }
}
