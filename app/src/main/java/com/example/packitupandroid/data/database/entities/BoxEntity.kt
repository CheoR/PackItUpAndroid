package com.example.packitupandroid.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.packitupandroid.data.model.Box
import com.example.packitupandroid.utils.Converters
import java.util.Date

@Entity(tableName = "boxes")
data class BoxEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    @ColumnInfo(defaultValue = "null")
    val description: String?,
    @ColumnInfo(
        name = "last_modified",
        defaultValue = "0"
    )
    val lastModified: Long,
    @ColumnInfo(
        name = "collection_id",
        defaultValue = "null",
    )
    val collectionId: String?,
)

fun BoxEntity.toBox(): Box = Box(
    id = this.id,
    name = this.name,
    description = this.description,
    lastModified = Converters().fromTimestamp(this.lastModified) ?: Date(),
    collectionId = this.collectionId
)

fun BoxEntity.updateWith(other: BoxEntity) : BoxEntity = copy (
    name = other.name,
    description = other.description,
    lastModified = System.currentTimeMillis(),
    collectionId = other.collectionId,
)
