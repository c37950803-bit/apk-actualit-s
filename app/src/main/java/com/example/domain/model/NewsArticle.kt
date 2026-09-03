package com.example.domain.model

/**
 * ============================================================================
 * COUCHE MODEL (M) DU SCHÉMA MBPM : MODÈLE MÉTIER PUR (DOMAIN ENTITY)
 * ============================================================================
 * Représente un article d'actualité au sens du domaine métier de l'application.
 *
 * RÈGLE D'OR ARCHITECTURALE MBPM :
 * - Ce modèle est totalement indépendant des technologies de persistance (Room),
 *   des annotations de sérialisation JSON (Moshi, Gson) ou des frameworks UI.
 * - Seule la couche Mapper (M) est autorisée à produire cette instance depuis
 *   les DTOs de l'API.
 *
 * Principes SOLID :
 * - Single Responsibility : Contient uniquement l'état et les invariants d'un article.
 *
 * @property id Identifiant unique pérenne de l'article.
 * @property title Titre ou manchette journalistique.
 * @property description Résumé ou amorce textuelle de l'article.
 * @property content Contenu complet ou extrait textuel détaillé.
 * @property articleUrl URL originale de publication chez l'éditeur.
 * @property imageUrl URL de la vignette ou image principale.
 * @property sourceName Nom du média ou de la source d'information (ex: Cameroon Tribune).
 * @property sourceIconUrl Favicon ou logo du média source.
 * @property publishedDate Date et heure de parution sous format textuel lisible.
 * @property category Catégorie thématique métier associée.
 * @property zone Zone géographique ciblée (Cameroun, Afrique, Monde).
 * @property isFavorite Indicateur de mise en favori pour lecture hors-ligne.
 */
data class NewsArticle(
    val id: String,
    val title: String,
    val description: String,
    val content: String,
    val articleUrl: String,
    val imageUrl: String?,
    val sourceName: String,
    val sourceIconUrl: String?,
    val publishedDate: String,
    val category: NewsCategory,
    val zone: NewsZone,
    val isFavorite: Boolean = false
)
