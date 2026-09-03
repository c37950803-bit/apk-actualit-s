package com.example.domain.model

/**
 * ============================================================================
 * COUCHE BUSINESS / MODEL (DOMAIN) : ZONES GÉOGRAPHIQUES
 * ============================================================================
 * Énumération métier définissant les trois périmètres d'agrégation d'actualités
 * requis par le cahier des charges :
 * 1. CAMEROUN : Actualités nationales et régionales camerounaises.
 * 2. AFRIQUE   : Actualités panafricaines et continentales.
 * 3. MONDE     : Actualités internationales et géopolitiques mondiales.
 *
 * Principes SOLID :
 * - Single Responsibility : Représente uniquement la segmentation territoriale métier.
 */
enum class NewsZone(
    val displayName: String,
    val description: String
) {
    CAMEROUN(
        displayName = "Cameroun",
        description = "Actualités nationales, politiques, économiques et sportives du Cameroun"
    ),
    AFRIQUE(
        displayName = "Afrique",
        description = "Actualités panafricaines, intégration régionale et événements continentaux"
    ),
    MONDE(
        displayName = "Monde",
        description = "Actualités internationales, géopolitique, technologies et économie mondiale"
    )
}
