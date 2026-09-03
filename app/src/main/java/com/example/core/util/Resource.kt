package com.example.core.util

/**
 * ============================================================================
 * COUCHE CORE : GESTION GÉNÉRIQUE DES ÉTATS DE DONNÉES (RESOURCE)
 * ============================================================================
 * Cette classe scellée encapsule l'état d'un flux de données asynchrone.
 * Elle permet d'unifier la communication entre les couches Data, Business
 * (Domain) et Presentation selon les principes SOLID :
 * - Single Responsibility Principle (SRP) : Représente uniquement l'état du résultat.
 * - Open/Closed Principle (OCP) : Extensible via ses sous-classes scellées.
 *
 * @param T Le type de données transporté par la ressource.
 * @property data Les données retournées en cas de succès ou de cache partiel.
 * @property message Le message d'erreur éventuel pour les couches supérieures.
 */
sealed class Resource<T>(
    val data: T? = null,
    val message: String? = null
) {
    /**
     * État représentant le succès de l'opération métier avec données garanties.
     *
     * @param data Les données fraîches ou issues du cache.
     */
    class Success<T>(data: T) : Resource<T>(data)

    /**
     * État représentant un échec de récupération (réseau, base de données, etc.).
     *
     * @param message Message d'erreur explicatif pour l'affichage ou le log.
     * @param data Données optionnelles en cache précédemment enregistrées.
     */
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)

    /**
     * État transitoire indiquant qu'une opération asynchrone est en cours d'exécution.
     *
     * @param data Données optionnelles préliminaires affichables pendant le chargement.
     */
    class Loading<T>(data: T? = null) : Resource<T>(data)
}
