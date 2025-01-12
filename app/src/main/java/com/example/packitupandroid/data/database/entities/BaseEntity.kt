package com.example.packitupandroid.data.database.entities


/**
 * Sealed interface representing a base entity with common properties.
 *
 * This interface defines the basic structure for all entities in the application.
 * It includes properties for a unique identifier, a name, an optional description,
 * and a timestamp for the last modification.
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
     * A description of the entity.
     */
    val description: String?

    /**
     * The timestamp of the last modification to the entity.
     */
    val lastModified: Long
}
