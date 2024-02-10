package com.example.packitupandroid.model

import com.example.packitupandroid.ui.components.card.EditFields
import java.util.UUID

data class Box(
    override val id: String = UUID.randomUUID().toString(),
    override var name: String,
    override var description: String = "",
    val items: List<Item> = emptyList(),
) : BaseCardData {
    // These properties are read-only and will be computed when the `Box` object is created.
    // If the `items` list changes, create a new `Box` object to update these properties.
    // TODO: FIX THIS
    override var isFragile: Boolean = false
        get() = items.any { it.isFragile }
    override var value: Double = 0.0
        get() = items.sumOf { it.value }
    companion object {
        val EDIT_FIELDS = setOf(
            EditFields.Name,
            EditFields.Description,
        )
    }
    override val editFields get() = EDIT_FIELDS
}
