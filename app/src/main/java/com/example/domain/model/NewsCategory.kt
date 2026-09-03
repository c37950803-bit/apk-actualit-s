package com.example.domain.model

/**
 * ============================================================================
 * COUCHE BUSINESS / MODEL (DOMAIN) : CATÉGORIES D'ACTUALITÉS
 * ============================================================================
 * Définit les thématiques d'actualités supportées par l'application pour
 * le Cameroun, l'Afrique et l'International.
 */
enum class NewsCategory(
    val id: String,
    val displayName: String,
    val apiCategory: String?
) {
    ALL(
        id = "all",
        displayName = "Tout",
        apiCategory = null
    ),
    POLITICS(
        id = "politics",
        displayName = "Politique",
        apiCategory = "politics"
    ),
    BUSINESS(
        id = "business",
        displayName = "Économie",
        apiCategory = "business"
    ),
    SPORTS(
        id = "sports",
        displayName = "Sports",
        apiCategory = "sports"
    ),
    TECHNOLOGY(
        id = "technology",
        displayName = "Technologie",
        apiCategory = "technology"
    ),
    ENTERTAINMENT(
        id = "entertainment",
        displayName = "Culture & Société",
        apiCategory = "entertainment"
    ),
    WORLD(
        id = "world",
        displayName = "International",
        apiCategory = "world"
    );

    companion object {
        /**
         * Retrouve une catégorie à partir de son identifiant textuel.
         */
        fun fromId(id: String?): NewsCategory {
            return entries.find { it.id.equals(id, ignoreCase = true) } ?: ALL
        }
    }
}
