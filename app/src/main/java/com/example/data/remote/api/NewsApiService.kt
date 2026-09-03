package com.example.data.remote.api

import com.example.data.remote.dto.NewsResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * ============================================================================
 * COUCHE DATA / REMOTE : SERVICE RETROFIT DE L'API D'ACTUALITÉS
 * ============================================================================
 * Déclare les appels HTTP REST vers l'agrégateur distant (NewsData.io).
 * Respecte le principe de ségrégation d'interface (ISP).
 */
interface NewsApiService {

    /**
     * Récupère les dernières actualités avec filtrage par pays, mots-clés ou catégories.
     *
     * @param apiKey Clé secrète d'accès à l'API.
     * @param query Requête de recherche plein-texte.
     * @param country Code pays ISO (ex: "cm" pour Cameroun).
     * @param category Thématique (politics, business, sports, technology, world).
     * @param language Code langue (ex: "fr,en").
     * @param page Jeton de pagination optionnel.
     */
    @GET("latest")
    suspend fun getLatestNews(
        @Query("apikey") apiKey: String,
        @Query("q") q: String? = null,
        @Query("country") country: String? = null,
        @Query("category") category: String? = null,
        @Query("language") language: String? = "fr",
        @Query("page") page: String? = null
    ): NewsResponseDto
}
