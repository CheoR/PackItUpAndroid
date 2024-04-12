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
    val imageUri: String? = null,
    override val value: Double  = 0.0,
    override val isFragile: Boolean = false,
    val boxId: String?,
    override val lastModified: Date = Date(),
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
    imageUri = this.imageUri,
    value = this.value,
    isFragile = this.isFragile,
    boxId = this.boxId,
    lastModified = Converters().dateToTimestamp(this.lastModified) ?: 0L,
)

fun Item.updateWith(other: Item) : Item = copy (
    name = other.name,
    description = other.description,
    value = other.value,
    isFragile = other.isFragile,
    imageUri = other.imageUri,
    boxId = other.boxId,
    lastModified = Date(),
)

//sealed class ImageUri {
//    data class StringUri(val uri: String) : ImageUri()
//    data class ResourceUri(val resourceId: Int) : ImageUri()
//}
