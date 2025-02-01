package com.example.packitupandroid.data.model

import com.example.packitupandroid.utils.DropdownOptions
import com.example.packitupandroid.utils.EditFields
import java.util.Date
import java.util.UUID


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
 * @property itemCount The number of items currently contained within the box. Defaults to 0.
 * @property collectionId An optional identifier of the collection this box belongs to.
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
    val itemCount: Int = 0,
    val collectionId: String? = null,
) : BaseCardData {
    companion object {
        /**
         * Set of editable fields for a Box.
         *
         * This set defines which [EditFields] of a [Box] can be edited. It includes
         * fields such as name and description.
         */
        val EDIT_FIELDS = setOf(
            EditFields.Name,
            EditFields.Description,
            EditFields.Dropdown,
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
 * Represents a box with an ID and a name, suitable for use in dropdown options.
 *
 * This data class implements the [DropdownOptions] interface, making it compatible with
 * components that require a list of selectable options with an ID and a display name.
 *
 * @property id The unique identifier for the box. This is typically a string value.
 * @property name The display name of the box, intended for presentation to the user.
 *
 * @constructor Creates a new BoxIdAndName instance with the specified ID and name.
 *
 * @see DropdownOptions
 */
data class BoxIdAndName(
    override val id: String,
    override val name: String
) : DropdownOptions
