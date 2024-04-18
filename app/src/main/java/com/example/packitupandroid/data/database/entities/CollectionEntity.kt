package com.example.packitupandroid.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.packitupandroid.data.model.Collection
import com.example.packitupandroid.utils.Converters
import java.util.Date

@Entity(tableName = "collections")
data class CollectionEntity(
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
)

fun CollectionEntity.toCollection(): Collection = Collection(
    id = this.id,
    name = this.name,
    description = this.description,
    lastModified = Converters().fromTimestamp(this.lastModified) ?: Date(),
)

fun CollectionEntity.updateWith(other: CollectionEntity) : CollectionEntity = copy (
    name = other.name,
    description = other.description,
    lastModified = System.currentTimeMillis(),
)
