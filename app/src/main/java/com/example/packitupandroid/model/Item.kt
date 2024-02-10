package com.example.packitupandroid.model

import com.example.packitupandroid.ui.components.card.EditFields
import java.util.UUID

data class Item(
    override val id: String = UUID.randomUUID().toString(),
    override var name: String,
    override var description: String = "",
    val imageUri: Int? = null,
    override var value: Double = 0.00,
    override var isFragile: Boolean = false
) : BaseCardData {
    companion object {
        val EDIT_FIELDS = setOf(
            EditFields.Name,
            EditFields.Description,
            EditFields.Dropdown,
            EditFields.IsFragile,
            EditFields.Value,
        )
    }
    override val editFields get() = EDIT_FIELDS
}