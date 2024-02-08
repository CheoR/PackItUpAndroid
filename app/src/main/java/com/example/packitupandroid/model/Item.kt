package com.example.packitupandroid.model

import com.example.packitupandroid.ui.components.card.EditFields
import java.util.UUID

data class Item(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val description: String = "",
    val isFragile: Boolean = false,
    val imageUri: Int? = null,
    val value: Double = 0.00,
) {
    companion object {
        val editFields = setOf(
            EditFields.Name,
            EditFields.Description,
            EditFields.Dropdown,
            EditFields.IsFragile,
            EditFields.Value,
        )
    }
}
