package com.example.packitupandroid.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.packitupandroid.model.Item
import com.example.packitupandroid.ui.utils.Converters
import java.util.Date

@Entity(tableName = "items")
data class ItemEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    @ColumnInfo(name = "image_uri")
    val imageUri: String?,
    @ColumnInfo(defaultValue = "")
    val description: String,
    @ColumnInfo(defaultValue = "0.0")
    val value: Double,
    @ColumnInfo(
        name = "is_fragile",
        defaultValue = "false",
    )
    val isFragile: Boolean,
    @ColumnInfo(
        name = "last_modified",
        defaultValue = ""
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
