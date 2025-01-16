package com.example.packitupandroid.data.model

import androidx.compose.runtime.Composable
import com.example.packitupandroid.utils.EditFields
import java.util.Date
import java.util.UUID


/**
 * Represents a query box, which is a container for storing and managing data related to a specific query.
 *
 * @property id The unique identifier of the query box. Defaults to a randomly generated UUID.
 * @property name The name of the query box. This is a required field.
 * @property description An optional description of the query box's purpose or content.
 * @property value A numerical value associated with the query box, often used for aggregated or calculated data. Defaults to 0.0.
 * @property is_fragile Indicates whether the query box's data is sensitive or prone to invalidation. Defaults to false.
 * @property last_modified The timestamp indicating the last time the query box was modified. Defaults to the current date and time.
 * @property item_count The number of items or data points associated with the query box. Defaults to 0.
 * @property collection_id An optional identifier of the collection that this query box belongs to. If null, it doesn't belong to any specific collection.
 */
data class QueryBox(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val description: String? = null,
    val value: Double = 0.0,
    val is_fragile: Boolean = false,
    val last_modified: Date = Date(), // Long
    val item_count: Int = 0,
    val collection_id: String? = null,
)

/**
 * Represents a single option in a dropdown list for a query.
 *
 * This data class is used to define an option that can be selected within a dropdown
 * that is used for filtering or searching. Each option has a unique identifier and a
 * displayable name.
 *
 * @property id A unique identifier for the option. This is typically used internally
 *             to represent the selected option in a structured way. It should be
 *             unique within the context of the dropdown.
 * @property name The name of the option that is displayed to the user in the dropdown.
 *              This is the human-readable label for the option.
 */
data class QueryDropdownOptions(
    val id: String,
    val name: String,
)

/**
 * Represents a Box, a container for holding items with specific properties.
 *
 * This data class extends [BaseCardData] and provides information about a box, such as its name,
 * description, value, fragility, and modification date. It also includes details about the number of
 * items it contains, its collection ID, the currently selected item within it, and a composable function
 * to display custom icons.
 *
 * @property id The unique identifier of the box. Defaults to a randomly generated UUID.
 * @property name The name of the box.
 * @property description An optional description of the box.
 * @property value The monetary value of the box or its contents. Defaults to 0.0.
 * @property isFragile Indicates whether the box's contents are fragile. Defaults to false.
 * @property lastModified The date and time when the box was last modified. Defaults to the current date and time.
 * @property item_count The number of items currently contained within the box. Defaults to 0.
 * @property collectionId An optional identifier of the collection this box belongs to.
 * @property currentSelection An optional identifier of the item currently selected within the box.
 * @property iconsContent An optional composable function used to render custom icons related to the box.
 * @property editFields A set of fields that can be edited for this Box.
 *
 * @see BaseCardData
 */
data class Box(
    override val id: String = UUID.randomUUID().toString(),
    override val name: String,
    override val description: String? = null,
    override val value: Double = 0.0,
    override val isFragile: Boolean = false,
    override val lastModified: Date = Date(),
    val item_count: Int = 0,
    val collectionId: String? = null,
    val currentSelection: String? = null,
    val iconsContent: @Composable (() -> Unit)? = null,
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
