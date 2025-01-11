package com.example.packitupandroid.data.database.entities


/**
 * A sealed interface representing a domain entity that can be used in Composables.
 *
 * [BaseEntity] defines the common properties that all domain entities should have,
 * such as an ID, a name, an optional description, and a last modified timestamp.
 *
 * Implementing this interface allows for consistent handling of entities in the UI layer,
 * particularly when using Jetpack Compose.
 */
sealed interface BaseEntity {
    /**
     * The unique identifier of the entity.
     */
    val id: String
    /**
     * The name of the entity.
     */
    val name: String
    /**
     * An optional description of the entity.
     */
    val description: String?
    /**
     * The timestamp of when the entity was last modified.
     */
    val lastModified: Long
}