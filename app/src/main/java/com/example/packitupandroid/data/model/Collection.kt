package com.example.packitupandroid.data.model

import com.example.packitupandroid.ui.components.card.EditFields
import java.util.Date
import java.util.UUID

data class Collection(
    override val id: String = UUID.randomUUID().toString(),
    override val name: String,
    override val description: String? = null,
    val boxes: List<Box> = emptyList(),
    override val isFragile: Boolean = boxes.any { it.isFragile },
    override val value: Double = boxes.sumOf { it.value },
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
