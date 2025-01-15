package com.example.packitupandroid.data.model

import androidx.compose.runtime.Composable
import com.example.packitupandroid.utils.EditFields
import java.util.Date
import java.util.UUID


data class Item(
    override val id: String = UUID.randomUUID().toString(),
    override val name: String,
    override val description: String? = null,
    override val value: Double  = 0.0,
    override val isFragile: Boolean = false,
    override val lastModified: Date = Date(),
    val boxId: String? = null,
    val imageUri: String? = null,
    val iconsContent: @Composable (() -> Unit)? = null,
) : BaseCardData {
    companion object {
        val EDIT_FIELDS = setOf(
            EditFields.Name,
            EditFields.Description,
            EditFields.Dropdown,
            EditFields.IsFragile,
            EditFields.Value,
            EditFields.ImageUri,
        )
    }
    override val editFields get() = EDIT_FIELDS
}
