package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * ============================================================================
 * COUCHE DATA / LOCAL : ENTITÉ DE BASE DE DONNÉES ROOM
 * ============================================================================
 * Modélise la structure de la table locale SQLite pour le stockage persistant
 * hors-ligne (Offline-First cache).
 *
 * RÈGLE D'OR ARCHITECTURALE MBPM :
 * - Cette entité Room appartient exclusivement à la couche Data/Local.
 * - Elle n'est jamais transmise telle quelle au Domain ni à l'UI Compose.
 * - Le Mapper (M) se charge de la convertir en [com.example.domain.model.NewsArticle].
 */
@Entity(tableName = "news_articles")
data class NewsEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val description: String,
    val content: String,
    val articleUrl: String,
    val imageUrl: String?,
    val sourceName: String,
    val sourceIconUrl: String?,
    val publishedDate: String,
    val categoryId: String,
    val zoneName: String,
    val isFavorite: Boolean = false,
    val cachedAtTimestamp: Long = System.currentTimeMillis()
)
