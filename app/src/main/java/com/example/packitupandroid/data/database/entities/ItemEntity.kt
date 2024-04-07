package com.example.packitupandroid.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.packitupandroid.model.Item
import com.example.packitupandroid.utils.Converters
import java.util.Date

@Entity(tableName = "items")
data class ItemEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    @ColumnInfo(
        name = "image_uri",
        defaultValue = "null",
    )
    val imageUri: String?,
    @ColumnInfo(defaultValue = "null")
    val description: String?,
    @ColumnInfo(defaultValue = "0.0")
    val value: Double,
    @ColumnInfo(
        name = "is_fragile",
        defaultValue = "0",
    )
    val isFragile: Boolean,
    @ColumnInfo(
        name = "last_modified",
        defaultValue = "0",
    )
    val lastModified: Long,
)

fun ItemEntity.toItem(): Item = Item(
    id = this.id,
    name = this.name,
    description = this.description,
    imageUri = this.imageUri,
    value = this.value,
    isFragile = this.isFragile,
    lastModified = Converters().fromTimestamp(this.lastModified) ?: Date()
)

fun ItemEntity.updateWith(other: ItemEntity) : ItemEntity = copy (
    name = other.name,
    description = other.description,
    value = other.value,
    isFragile = other.isFragile,
    imageUri = other.imageUri,
    lastModified = System.currentTimeMillis(),
)
