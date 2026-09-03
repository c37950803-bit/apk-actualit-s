package com.example.core.util

/**
 * ============================================================================
 * COUCHE CORE : CONSTANTES GLOBALES DE L'APPLICATION
 * ============================================================================
 * Centralise les paramètres d'infrastructure réseau, d'API et de configuration
 * des zones géographiques (Cameroun, Afrique, Monde).
 */
object Constants {
    /**
     * URL de base du service REST NewsData.io.
     */
    const val BASE_URL = "https://newsdata.io/api/1/"

    /**
     * Clé d'API par défaut pour l'agrégateur.
     * Note de sécurité : Peut être surchargée à l'exécution ou injectée via BuildConfig.
     */
    const val DEFAULT_API_KEY = "pub_4ab18b48846e4bcdbe892fc2b355fb0d"

    /**
     * Nom du fichier de base de données SQLite Room.
     */
    const val DATABASE_NAME = "actu_mbpm_news.db"

    /**
     * Identifiant du pays pour le Cameroun (ISO 3166-1 alpha-2).
     */
    const val COUNTRY_CAMEROON = "cm"

    /**
     * Langues par défaut prises en charge (français et anglais).
     */
    const val LANGUAGE_FR = "fr"

    /**
     * Identifiant de la zone par défaut.
     */
    const val DEFAULT_ZONE = "CAMEROUN"
}
