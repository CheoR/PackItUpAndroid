package com.example.packitupandroid.data.model

import com.example.packitupandroid.utils.EditFields
import java.util.Date
import java.util.UUID


/**
 * Represents an item with various properties.
 *
 * This data class holds information about a single item, including its unique identifier,
 * name, description, value, fragility status, last modification date, and optional
 * box association, current selection, image URI, and custom icons.
 *
 * @property id The unique identifier for the item. Defaults to a random UUID string.
 * @property name The name of the item.
 * @property description An optional description of the item.
 * @property value The numerical value associated with the item. Defaults to 0.0.
 * @property isFragile Indicates whether the item is fragile. Defaults to false.
 * @property lastModified The date and time when the item was last modified. Defaults to the current date and time.
 * @property boxId An optional identifier of the box this item belongs to.
 * @property currentSelection An optional string representing the current selection associated with the item.
 * @property imageUri An optional URI pointing to an image associated with the item.
 * @property iconsContent An optional Composable function that renders custom icons for the item.
 *
 * @see BaseCardData
 * @see EditFields
 */
data class Item(
    override val id: String = UUID.randomUUID().toString(),
    override val name: String,
    override val description: String? = null,
    override val value: Double  = 0.0,
    override val isFragile: Boolean = false,
    override val lastModified: Date = Date(),
    val boxId: String? = null,
    val imageUri: String? = null,
) : BaseCardData {
    companion object {
        /**
         *  A set of [EditFields] representing the fields that can be edited in a specific context.
         *  This set defines which fields are considered modifiable.
         *  For example, in an object editing screen, these would be the fields the user can change.
         */
        val EDIT_FIELDS = setOf(
            EditFields.Name,
            EditFields.Description,
            EditFields.Dropdown,
            EditFields.IsFragile,
            EditFields.Value,
            EditFields.ImageUri,
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
