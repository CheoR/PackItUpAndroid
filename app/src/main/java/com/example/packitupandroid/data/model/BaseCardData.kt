package com.example.packitupandroid.data.model

import com.example.packitupandroid.utils.EditFields
import java.util.Date


/**
 * Sealed interface representing a common entity in the inventory system.
 */
sealed interface BaseCardData {
    /**
     * Unique identifier for the entity.
     */
    val id: String

    /**
     * Name of the entity.
     */
    val name: String

    /**
     * Description of the entity. Can be null.
     */
    val description: String?

    /**
     * Indicates if the entity is fragile.
     */
    val isFragile: Boolean

    /**
     * The monetary value of the entity.
     */
    val value: Double

    /**
     * Timestamp of the last modification of the entity.
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
