package com.example.packitupandroid.model

import com.example.packitupandroid.ui.components.card.EditFields
import java.util.UUID

data class Collection(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val description: String = "",
    val boxes: List<Box> = emptyList(),
) {
    // TODO: FIX THIS
    val isFragile: Boolean
        get() = boxes.any { it.isFragile }
    val value: Double
        get() = boxes.sumOf { it.value }
    companion object {
        val editFields = setOf(
            EditFields.Name,
            EditFields.Description,
        )
    }
}
