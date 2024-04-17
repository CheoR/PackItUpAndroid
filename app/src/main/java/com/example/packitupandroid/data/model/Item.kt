package com.example.packitupandroid.data.model

import com.example.packitupandroid.data.database.entities.ItemEntity
import com.example.packitupandroid.utils.Converters
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
    val boxId: String?,
    val imageUri: String? = null,
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

fun Item.toEntity(): ItemEntity = ItemEntity(
    id = this.id,
    name = this.name,
    description = this.description,
    value = this.value,
    isFragile = this.isFragile,
    lastModified = Converters().dateToTimestamp(this.lastModified) ?: 0L,
    boxId = this.boxId,
    imageUri = this.imageUri,
)

fun Item.updateWith(other: Item) : Item = copy (
    name = other.name,
    description = other.description,
    value = other.value,
    isFragile = other.isFragile,
    lastModified = Date(),
    boxId = other.boxId,
    imageUri = other.imageUri,
)
