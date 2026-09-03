package com.example.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * ============================================================================
 * COUCHE MODEL (M) DU SCHÉMA MBPM : DATA TRANSFER OBJECT (DTO)
 * ============================================================================
 * Ces classes modélisent la réponse JSON brute retournée par l'API REST
 * (ex: NewsData.io).
 *
 * RÈGLE D'OR ARCHITECTURALE MBPM :
 * - Ces DTOs ne doivent JAMAIS être exposés directement à l'interface utilisateur.
 * - Ils sont cantonnés à la couche Data et doivent obligatoirement passer par
 *   la couche Mapper (M) pour être transformés en modèles Domain purs.
 */

@JsonClass(generateAdapter = true)
data class NewsResponseDto(
    @Json(name = "status")
    val status: String? = null,

    @Json(name = "totalResults")
    val totalResults: Int? = null,

    @Json(name = "results")
    val results: List<NewsArticleDto>? = null,

    @Json(name = "nextPage")
    val nextPage: String? = null
)

@JsonClass(generateAdapter = true)
data class NewsArticleDto(
    @Json(name = "article_id")
    val articleId: String? = null,

    @Json(name = "title")
    val title: String? = null,

    @Json(name = "link")
    val link: String? = null,

    @Json(name = "description")
    val description: String? = null,

    @Json(name = "content")
    val content: String? = null,

    @Json(name = "pubDate")
    val pubDate: String? = null,

    @Json(name = "image_url")
    val imageUrl: String? = null,

    @Json(name = "source_id")
    val sourceId: String? = null,

    @Json(name = "source_name")
    val sourceName: String? = null,

    @Json(name = "source_icon")
    val sourceIcon: String? = null,

    @Json(name = "country")
    val country: List<String>? = null,

    @Json(name = "category")
    val category: List<String>? = null
)
