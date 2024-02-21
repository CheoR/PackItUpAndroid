package com.example.packitupandroid.model

import com.example.packitupandroid.ui.components.card.EditFields
import java.util.UUID

data class Box(
    override val id: String = UUID.randomUUID().toString(),
    override val name: String,
    override val description: String = "",
    val items: List<Item> = emptyList(),
) : BaseCardData {
    override val isFragile: Boolean
        get() = items.any { it.isFragile }
    override val value: Double
        get() = items.sumOf { it.value }
    companion object {
        val EDIT_FIELDS = setOf(
            EditFields.Name,
            EditFields.Description,
        )
    }
    override val editFields get() = EDIT_FIELDS
}
