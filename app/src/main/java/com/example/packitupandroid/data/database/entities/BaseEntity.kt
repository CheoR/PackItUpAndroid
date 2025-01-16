package com.example.packitupandroid.data.database.entities


/**
 * Sealed interface representing a base entity with common properties.
 *
 * This interface defines the basic structure for all entities in the application's
 * data layer. It includes properties for a unique identifier (`id`), a name, an
 * optional description, and a timestamp for the last modification (`lastModified`).
 *
 * Implementing this interface ensures that all entities have a consistent set of
 * core properties.
 */
sealed interface BaseEntity {
    /**
     * The unique identifier of the entity.
     *
     * This property represents the unique identifier for the entity, typically a UUID.
     */
    val id: String

    /**
     * The name of the entity.
     *
     * This property represents the name of the entity.
     */
    val name: String

    /**
     * A description of the entity.
     *
     * This property represents an optional description of the entity. It can be null
     * if no description is available.
     */
    val description: String?

    /**
     * The timestamp of the last modification to the entity.
     *
     * This property represents the timestamp of the last modification to the entity,
     * typically stored as milliseconds since the epoch.
     */
    val lastModified: Long
}
