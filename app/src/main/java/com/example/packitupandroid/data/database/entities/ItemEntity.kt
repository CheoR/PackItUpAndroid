package com.example.packitupandroid.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.UUID


/**
 * Represents an item entity in the database.
 *
 * This data class is used to store information about an item in the database.
 * It includes properties such as the item's ID, name, description, value, fragility,
 * last modification timestamp, associated box ID, and image URI.
 *
 * This entity is mapped to the "items" table in the database and includes a foreign
 * key relationship with the [BoxEntity].
 *
 * @property id The unique identifier for the item, generated using UUID.randomUUID().
 * @property name The name of the item.
 * @property description An optional description of the item. Defaults to null.
 * @property value The monetary value of the item. Defaults to 0.0.
 * @property isFragile Indicates whether the item is fragile. Defaults to false.
 * @property lastModified The timestamp of the last modification of the item. Defaults to 0.
 * @property boxId The identifier of the box containing the item. Defaults to null.
 * @property imageUri The URI of the item's image. Defaults to null.
 */
@Entity(
    tableName = "items",
    foreignKeys = [
        ForeignKey(
            entity = BoxEntity::class,
            parentColumns = ["id"],
            childColumns = ["box_id"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [Index(value = ["box_id"])]
)
data class ItemEntity(
    @PrimaryKey
    override val id: String = UUID.randomUUID().toString(),
    override val name: String,
    @ColumnInfo(defaultValue = "null")
    override val description: String? = null,
    @ColumnInfo(defaultValue = "0.0")
    val value: Double = 0.0,
    @ColumnInfo(
        name = "is_fragile",
        defaultValue = "0",
    )
    val isFragile: Boolean = false,
    @ColumnInfo(
        name = "last_modified",
        defaultValue = "0",
    )
    override val lastModified: Long = 0,
    @ColumnInfo(
        name = "box_id",
        defaultValue = "null",
    )
    val boxId: String? = null,
    @ColumnInfo(
        name = "image_uri",
        defaultValue = "null",
    )
    val imageUri: String? = null,
): BaseEntity
