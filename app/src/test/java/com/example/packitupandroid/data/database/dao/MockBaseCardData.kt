package com.example.packitupandroid.data.database.dao

import com.example.packitupandroid.utils.EditFields
import java.util.Date

/**
 * TODO: MOVE TO COMMON MODULE
 */

/**
 * Sealed interface representing a common entity in the inventory system.
 *
 * This interface defines the common properties for all card-like data entities
 * within the inventory system. It includes properties such as a unique identifier,
 * a name, an optional description, a monetary value, a fragility indicator, a last
 * modified timestamp, and a set of editable fields.
 *
 * Implementing this interface ensures that all card-like data entities have a
 * consistent set of core properties.
 */
sealed interface BaseCardData {
    /**
     * Unique identifier for the entity.
     *
     * This property represents the unique identifier for the entity, typically a UUID.
     */
    val id: String

    /**
     * Name of the entity.
     *
     * This property represents the name of the entity.
     */
    val name: String

    /**
     * Description of the entity. Can be null.
     *
     * This property represents an optional description of the entity. It can be null
     * if no description is available.
     */
    val description: String?

    /**
     * The monetary value of the entity.
     *
     * This property represents the monetary value of the entity.
     */
    val value: Double

    /**
     * Indicates if the entity is fragile.
     *
     * This property indicates whether the entity is considered fragile.
     */
    val isFragile: Boolean

    /**
     * Timestamp of the last modification of the entity.
     *
     * This property represents the timestamp of the last modification to the entity.
     */
    val lastModified: Date

    /**
     * A set of fields that can be edited in an item.
     *
     * This set contains [EditFields] objects, indicating which properties of an element
     * are editable. The specific fields included in this set will vary depending on
     * the type of element and the context in which it is being edited.
     *
     * @see EditFields
     */
    val editFields: Set<EditFields>
}
