package com.example.packitupandroid.data.model

import com.example.packitupandroid.data.database.entities.BoxEntity
import com.example.packitupandroid.utils.Converters
import com.example.packitupandroid.utils.EditFields
import java.util.Date
import java.util.UUID

data class QueryBox(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val description: String? = null,
    val value: Double = 0.0,
    val is_fragile: Boolean = false,
    val last_modified: Long,
    val count: Int = 0,
    val collectionId: String? = null,
)

fun QueryBox.toBox(): Box = Box(
    id = this.id,
    name = this.name,
    description = this.description,
    count = this.count,
    value = this.value,
    isFragile = this.is_fragile,
    lastModified = Converters().fromTimestamp(this.last_modified) ?: Date(),
    collectionId = this.collectionId,
)

data class Box(
    override val id: String = UUID.randomUUID().toString(),
    override val name: String,
    override val description: String? = null,
    override val value: Double = 0.0,
    override val isFragile: Boolean = false,
    override val lastModified: Date = Date(),
    val count: Int = 0,
    val collectionId: String? = null,
) : BaseCardData {
    companion object {
        val EDIT_FIELDS = setOf(
            EditFields.Name,
            EditFields.Description,
        )
    }
    override val editFields get() = EDIT_FIELDS
}

fun Box.toEntity(): BoxEntity = BoxEntity(
    id = this.id,
    name = this.name,
    description = this.description,
    lastModified = System.currentTimeMillis(),
    collectionId = this.collectionId,
)

fun Box.updateWith(other: Box) : Box = copy (
    name = other.name,
    description = other.description,
    value = other.value,
    isFragile = other.isFragile,
    count = other.count,
    lastModified = Date(),
    collectionId = other.collectionId,
)
