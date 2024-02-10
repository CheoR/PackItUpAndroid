package com.example.packitupandroid.model

import com.example.packitupandroid.ui.components.card.EditFields
import java.util.UUID

data class Collection(
    override val id: String = UUID.randomUUID().toString(),
    override var name: String,
    override var description: String = "",
    val boxes: List<Box> = emptyList(),
) : BaseCardData {
    // TODO: FIX THIS
    override var isFragile: Boolean = false
        get() = boxes.any { it.isFragile }
    override var value: Double = 0.0
        get() = boxes.sumOf { it.value }
    companion object {
        val EDIT_FIELDS = setOf(
            EditFields.Name,
            EditFields.Description,
        )
    }
    override val editFields get() = EDIT_FIELDS
}
