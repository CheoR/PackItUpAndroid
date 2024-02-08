package com.example.packitupandroid.model

import com.example.packitupandroid.ui.components.card.EditFields
import java.util.UUID

data class Box(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val description: String = "",
    val items: List<Item> = emptyList(),
) {
    // These properties are read-only and will be computed when the `Box` object is created.
    // If the `items` list changes, create a new `Box` object to update these properties.
    // TODO: FIX THIS
    val isFragile: Boolean
        get() = items.any { it.isFragile }
    val value: Double
        get() = items.sumOf { it.value }
    companion object {
        val editFields = setOf(
            EditFields.Name,
            EditFields.Description,
        )
    }
}
