package com.example.packitupandroid.data.model

import com.example.packitupandroid.utils.DropdownOptions
import com.example.packitupandroid.utils.EditFields
import java.util.Date
import java.util.UUID


/**
 * Represents a collection of queries, encapsulating metadata and state information.
 *
 * This data class holds information about a collection of queries, including its unique identifier,
 * name, optional description, associated value, fragility status, last modification date,
 * and counts of items and boxes associated with it.
 *
 * @property id A unique identifier for the query collection. Defaults to a randomly generated UUID.
 * @property name The name of the query collection. This field is mandatory and should provide a
 *                 human-readable identifier for the collection.
 * @property description An optional description of the query collection, providing further context
 *                      or details about its purpose. Can be null.
 * @property value A numerical value associated with the query collection. Defaults to 0.0.
 * @property is_fragile Indicates whether the query collection is considered fragile. A fragile
 *                     collection might require special handling or be more susceptible to errors.
 *                     Defaults to false.
 * @property last_modified The date and time when the query collection was last modified. This is
 *                         used for tracking changes and updates to the collection.
 * @property item_count The number of items associated with this query collection. Defaults to 0.
 * @property box_count The number of boxes associated with this query collection. Defaults to 0.
 */
data class QueryCollection(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val description: String? = null,
    val value: Double = 0.0,
    val is_fragile: Boolean = false,
    val last_modified: Date,
    val item_count: Int = 0,
    val box_count: Int = 0,
)

/**
 * Represents a collection of items, often used for organizing and managing data.
 *
 * This data class holds information about a collection, including its unique identifier, name,
 * description, associated value, fragility status, last modification date, item count, box count,
 * current selection, and optional custom icons.
 *
 * @property id The unique identifier of the collection. Defaults to a randomly generated UUID.
 * @property name The name of the collection.
 * @property description A description of the collection's purpose or content. Can be null.
 * @property value The monetary or assigned value of the collection. Defaults to 0.0.
 * @property isFragile Indicates whether the collection contains fragile items. Defaults to false.
 * @property lastModified The date and time when the collection was last modified. Defaults to the current date and time.
 * @property itemCount The number of items contained within the collection. Defaults to 0.
 * @property boxCount The number of boxes or containers used to store the items in this collection. Defaults to 0.
 *
 * @property editFields A set of fields that can be edited for this collection.
 *
 * @see BaseCardData
 * @see EditFields
 */
data class Collection(
    override val id: String = UUID.randomUUID().toString(),
    override val name: String,
    override val description: String? = null,
    override val value: Double = 0.0,
    override val isFragile: Boolean = false,
    override val lastModified: Date = Date(),
    val boxCount: Int = 0,
    val itemCount: Int = 0,
) : BaseCardData {
    companion object {
        /**
         * Defines the set of fields that are editable for a particular entity.
         *
         * This set specifies which properties can be modified through an edit operation.
         * Currently, it includes the 'Name' and 'Description' fields.
         */
        val EDIT_FIELDS = setOf(
            EditFields.Name,
            EditFields.Description,
        )
    }
    /**
     * Specifies the fields that are editable in the corresponding UI or data structure.
     *
     * This property returns a constant set of field names (defined in [EDIT_FIELDS]) that
     * represent the fields within this object which can be modified by the user or
     * through an editing process.  It's typically used to determine which parts of
     * a data structure can be altered.
     *
     * The specific field names returned will depend on the implementation and the
     * context in which this object is used.  For example, in a user profile, this
     * might include fields like "name", "email", and "phone".
     *
     * @see EDIT_FIELDS for the list of specific fields that are editable.
     */
    override val editFields get() = EDIT_FIELDS
}

/**
 * Represents a collection's ID and name, suitable for use in dropdown menus or lists.
 *
 * This data class implements the [DropdownOptions] interface, allowing it to be used
 * where a list of selectable options with an ID and a display name are required.
 *
 * @property id The unique identifier of the collection.
 * @property name The human-readable name of the collection.
 */
data class CollectionIdAndName(
    override val id: String,
    override val name: String
) : DropdownOptions
