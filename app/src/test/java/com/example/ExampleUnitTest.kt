package com.example

import com.example.data.mapper.NewsMapper
import com.example.data.remote.dto.NewsArticleDto
import com.example.domain.model.NewsCategory
import com.example.domain.model.NewsZone
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * ============================================================================
 * TESTS UNITAIRES DE LA COUCHE MAPPER ET DOMAINE (MBPM)
 * ============================================================================
 */
class ExampleUnitTest {

    @Test
    fun newsMapper_convertsDtoToDomainArticle_correctly() {
        val dto = NewsArticleDto(
            articleId = "cm_test_123",
            title = "Sommet à Yaoundé",
            link = "https://example.cm/actu",
            description = "Un résumé clair de l'événement.",
            content = "Texte intégral de l'article.",
            pubDate = "2026-09-02 10:00:00",
            imageUrl = "https://example.cm/image.jpg",
            sourceName = "Cameroon News",
            sourceIcon = null,
            country = listOf("cameroon"),
            category = listOf("politics")
        )

        val domainArticle = NewsMapper.dtoToDomain(dto, NewsZone.CAMEROUN)

        assertEquals("cm_test_123", domainArticle.id)
        assertEquals("Sommet à Yaoundé", domainArticle.title)
        assertEquals(NewsCategory.POLITICS, domainArticle.category)
        assertEquals(NewsZone.CAMEROUN, domainArticle.zone)
        assertEquals("Cameroon News", domainArticle.sourceName)
    }

    @Test
    fun newsMapper_handlesEmptyDtoFields_withGracefulDefaults() {
        val emptyDto = NewsArticleDto()
        val domainArticle = NewsMapper.dtoToDomain(emptyDto, NewsZone.CAMEROUN)

        assertNotNull(domainArticle.id)
        assertTrue(domainArticle.title.isNotBlank())
        assertEquals(NewsCategory.ALL, domainArticle.category)
        assertEquals(NewsZone.CAMEROUN, domainArticle.zone)
    }
}
