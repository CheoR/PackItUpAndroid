package com.example.packitupandroid.data.model

import com.example.packitupandroid.utils.EditFields
import java.util.Date
import java.util.UUID

data class Box(
    override val id: String = UUID.randomUUID().toString(),
    override val name: String,
    override val description: String? = null,
//    val itemIds: List<String> = emptyList(),
//    val items: List<Item> = emptyList(),
//    override val value: Double = items.sumOf { it.value },
//    override val isFragile: Boolean = items.any { it.isFragile },
//    val items: List<Item> = emptyList(),
//    override val value: Double = 0.0, // Don't calculate here, use entity for updates
//    override val isFragile: Boolean = false, // Don't calculate here, use entity for updates
//    override val lastModified: Date = Date(),
    override val value: Double = 0.0,
    override val isFragile: Boolean = false,
    val collectionId: String? = null,
    override val lastModified: Date = Date(),
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
//    value = this.value,
//    isFragile = this.isFragile,
    collectionId = this.collectionId,
    lastModified = System.currentTimeMillis(),
)

fun Box.updateWith(other: Box) : Box = copy (
    name = other.name,
    description = other.description,
//    value = other.value,
//    isFragile = other.isFragile,
    collectionId = other.collectionId,
    lastModified = Date(),
)
