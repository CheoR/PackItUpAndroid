package com.example.packitupandroid.model

import com.example.packitupandroid.data.database.entities.ItemEntity
import com.example.packitupandroid.ui.components.card.EditFields
import com.example.packitupandroid.ui.utils.Converters
import java.util.Date
import java.util.UUID

data class Item(
    override val id: String = UUID.randomUUID().toString(),
    override val name: String,
    override val description: String = "",
    val imageUri: String? = null, // ImageUri? = null,
    override val value: Double  = 0.0,
    override val isFragile: Boolean = false,
    override val lastModified: Date = Date(),
) : BaseCardData  {
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
    lastModified = Converters().dateToTimestamp(this.lastModified) ?: 0L,
)

//sealed class ImageUri {
//    data class StringUri(val uri: String) : ImageUri()
//    data class ResourceUri(val resourceId: Int) : ImageUri()
//}
