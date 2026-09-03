package com.example.data.mapper

import com.example.data.local.entity.NewsEntity
import com.example.data.remote.dto.NewsArticleDto
import com.example.domain.model.NewsArticle
import com.example.domain.model.NewsCategory
import com.example.domain.model.NewsZone

/**
 * ============================================================================
 * COUCHE MAPPER (M) DU SCHÉMA MBPM : ISOLATION ET CONVERSION DE MODÈLES
 * ============================================================================
 * Dans l'architecture MBPM (Model, Business, Presentation, Mapper), la couche
 * Mapper joue un rôle capital pour garantir la pérennité et la modularité :
 *
 * 1. DÉCOUPLAGE TECHNOLOGIQUE :
 *    Elle empêche qu'une modification du contrat JSON externe (changement de nom
 *    de clé, valeur nulle inattendue, dépréciation de champ API) ne se propage
 *    aux règles métier (Domain) ou ne fasse planter l'interface utilisateur (Compose).
 *
 * 2. TRANSFORMATION BI-DIRECTIONNELLE :
 *    - Data DTO (Réseau)      ===> Domain Model (Entités métier pures)
 *    - Data Entity (Room SQL) ===> Domain Model (Entités métier pures)
 *    - Domain Model           ===> Data Entity (Room SQL) pour la persistance locale.
 *
 * 3. VALEURS PAR DÉFAUT & ASSAINISSEMENT :
 *    Assainit les champs nullables, valide les identifiants et associe la
 *    catégorie thématique et la zone géographique (Cameroun, Afrique, Monde).
 *
 * Principes SOLID :
 * - Single Responsibility Principle (SRP) : Dédiée à 100 % à la transformation de données.
 * - Open/Closed Principle (OCP) : Facilement extensible pour de nouvelles zones ou DTOs.
 */
object NewsMapper {

    /**
     * Transforme un objet DTO de l'API (Data Layer) en entité métier pure [NewsArticle] (Domain Layer).
     *
     * @param dto Objet JSON désérialisé de l'API.
     * @param targetZone Zone géographique cible attribuée (Cameroun, Afrique ou Monde).
     * @return Modèle métier immuable prêt pour la couche Business et Presentation.
     */
    fun dtoToDomain(dto: NewsArticleDto, targetZone: NewsZone): NewsArticle {
        val categoryId = dto.category?.firstOrNull().orEmpty()
        val resolvedCategory = NewsCategory.fromId(categoryId)

        return NewsArticle(
            id = dto.articleId ?: dto.link?.hashCode()?.toString() ?: System.nanoTime().toString(),
            title = dto.title?.trim().orEmpty().ifEmpty { "Actualité sans titre" },
            description = dto.description?.trim().orEmpty().ifEmpty { "Consultez l'article complet pour plus de détails." },
            content = dto.content?.trim().orEmpty().ifEmpty { dto.description.orEmpty() },
            articleUrl = dto.link.orEmpty().ifEmpty { "https://newsdata.io" },
            imageUrl = dto.imageUrl?.takeIf { it.isNotBlank() },
            sourceName = dto.sourceName?.trim().orEmpty().ifEmpty { "Source locale" },
            sourceIconUrl = dto.sourceIcon?.takeIf { it.isNotBlank() },
            publishedDate = dto.pubDate?.trim().orEmpty().ifEmpty { "Récemment" },
            category = resolvedCategory,
            zone = targetZone,
            isFavorite = false
        )
    }

    /**
     * Transforme une liste de DTOs distants en une liste d'articles de domaine.
     */
    fun dtoListToDomainList(dtos: List<NewsArticleDto>, targetZone: NewsZone): List<NewsArticle> {
        return dtos.map { dtoToDomain(it, targetZone) }
    }

    /**
     * Transforme une entité locale Room [NewsEntity] en modèle de domaine [NewsArticle].
     */
    fun entityToDomain(entity: NewsEntity): NewsArticle {
        val zone = try {
            NewsZone.valueOf(entity.zoneName)
        } catch (e: IllegalArgumentException) {
            NewsZone.CAMEROUN
        }

        return NewsArticle(
            id = entity.id,
            title = entity.title,
            description = entity.description,
            content = entity.content,
            articleUrl = entity.articleUrl,
            imageUrl = entity.imageUrl,
            sourceName = entity.sourceName,
            sourceIconUrl = entity.sourceIconUrl,
            publishedDate = entity.publishedDate,
            category = NewsCategory.fromId(entity.categoryId),
            zone = zone,
            isFavorite = entity.isFavorite
        )
    }

    /**
     * Transforme une liste d'entités Room en liste de modèles de domaine.
     */
    fun entityListToDomainList(entities: List<NewsEntity>): List<NewsArticle> {
        return entities.map { entityToDomain(it) }
    }

    /**
     * Convertit un modèle de domaine [NewsArticle] en entité Room [NewsEntity] pour la persistance locale.
     */
    fun domainToEntity(article: NewsArticle): NewsEntity {
        return NewsEntity(
            id = article.id,
            title = article.title,
            description = article.description,
            content = article.content,
            articleUrl = article.articleUrl,
            imageUrl = article.imageUrl,
            sourceName = article.sourceName,
            sourceIconUrl = article.sourceIconUrl,
            publishedDate = article.publishedDate,
            categoryId = article.category.id,
            zoneName = article.zone.name,
            isFavorite = article.isFavorite,
            cachedAtTimestamp = System.currentTimeMillis()
        )
    }

    /**
     * Convertit une liste de modèles de domaine en liste d'entités Room.
     */
    fun domainListToEntityList(articles: List<NewsArticle>): List<NewsEntity> {
        return articles.map { domainToEntity(it) }
    }
}

/**
 * ============================================================================
 * FONCTIONS D'EXTENSION KOTLIN (SYNTAXIC SUGAR MBPM)
 * ============================================================================
 * Offrent une syntaxe élégante et idiomatique pour les transformations.
 */

/**
 * Extension pour convertir un [NewsArticleDto] en [NewsArticle] du domaine.
 */
fun NewsArticleDto.toDomain(zone: NewsZone): NewsArticle =
    NewsMapper.dtoToDomain(this, zone)

/**
 * Extension pour convertir une liste de [NewsArticleDto] en liste de [NewsArticle].
 */
fun List<NewsArticleDto>.toDomain(zone: NewsZone): List<NewsArticle> =
    NewsMapper.dtoListToDomainList(this, zone)

/**
 * Extension pour convertir une entité [NewsEntity] Room en [NewsArticle].
 */
fun NewsEntity.toDomain(): NewsArticle =
    NewsMapper.entityToDomain(this)

/**
 * Extension pour convertir un [NewsArticle] de domaine en entité Room [NewsEntity].
 */
fun NewsArticle.toEntity(): NewsEntity =
    NewsMapper.domainToEntity(this)
