package com.example.packitupandroid.model

import com.example.packitupandroid.ui.components.card.EditFields
import java.util.UUID

data class Collection(
    override val id: String = UUID.randomUUID().toString(),
    override val name: String,
    override val description: String = "",
    val boxes: List<Box> = emptyList(),
) : BaseCardData {
    override val isFragile: Boolean
        get() = boxes.any { it.isFragile }
override val value: Double
    get() = boxes.sumOf { it.value }
    companion object {
        val EDIT_FIELDS = setOf(
            EditFields.Name,
            EditFields.Description,
        )
    }
    override val editFields get() = EDIT_FIELDS
}
