package com.example.packitupandroid.model

import com.example.packitupandroid.ui.components.card.EditFields
import java.util.Date
import java.util.UUID

data class Box(
    override val id: String = UUID.randomUUID().toString(),
    override val name: String,
    override val description: String? = null,
    val items: List<Item> = emptyList(),
    override val value: Double = items.sumOf { it.value },
    override val isFragile: Boolean = items.any { it.isFragile },
    override val lastModified: Date = Date(),
) : BaseCardData {
    companion object {
        val EDIT_FIELDS = setOf(
            EditFields.Name,
            EditFields.Description,
        )
    }
    override val editFields get() = EDIT_FIELDS
}
