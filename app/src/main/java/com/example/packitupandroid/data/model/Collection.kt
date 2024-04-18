package com.example.packitupandroid.data.model

import com.example.packitupandroid.data.database.entities.CollectionEntity
import com.example.packitupandroid.utils.Converters
import com.example.packitupandroid.utils.EditFields
import java.util.Date
import java.util.UUID

data class QueryCollection(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val description: String? = null,
    val value: Double = 0.0,
    val is_fragile: Boolean = false,
    val last_modified: Long,
    val item_count: Int = 0,
    val box_count: Int = 0,
)

fun QueryCollection.toCollection(): Collection = Collection(
    id = this.id,
    name = this.name,
    description = this.description,
    item_count = this.item_count,
    box_count = this.box_count,
    value = this.value,
    isFragile = this.is_fragile,
    lastModified = Converters().fromTimestamp(this.last_modified) ?: Date(),
)

data class Collection(
    override val id: String = UUID.randomUUID().toString(),
    override val name: String,
    override val description: String? = null,
    override val value: Double = 0.0,
    override val isFragile: Boolean = false,
    override val lastModified: Date = Date(),
    val item_count: Int = 0,
    val box_count: Int = 0,
) : BaseCardData {
    companion object {
        val EDIT_FIELDS = setOf(
            EditFields.Name,
            EditFields.Description,
        )
    }
    override val editFields get() = EDIT_FIELDS
}

fun Collection.toEntity(): CollectionEntity = CollectionEntity(
    id = this.id,
    name = this.name,
    description = this.description,
    lastModified = System.currentTimeMillis(),
)

fun Collection.updateWith(other: Collection) : Collection = copy (
    name = other.name,
    description = other.description,
    value = other.value,
    isFragile = other.isFragile,
    item_count = other.item_count,
    box_count = other.box_count,
    lastModified = Date(),
)
