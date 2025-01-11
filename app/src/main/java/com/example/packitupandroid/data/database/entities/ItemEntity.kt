package com.example.packitupandroid.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.packitupandroid.data.model.Item
import com.example.packitupandroid.utils.Converters
import java.util.Date


/**
 * Represents an item entity in the database.
 *
 * @property id The unique identifier for the item.
 * @property name The name of the item.
 * @property description An optional description of the item. Defaults to null.
 * @property value The monetary value of the item. Defaults to 0.0.
 * @property isFragile Indicates whether the item is fragile. Defaults to false.
 * @property lastModified The timestamp of the last modification. Defaults to 0.
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
    override val id: String,
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

/**
 * Converts an [ItemEntity] to an [Item].
 *
 * This function takes an [ItemEntity] and maps its properties to a new [Item] object.
 * It also converts the `lastModified` timestamp to a `Date` object using the [Converters] class.
 *
 * @return The converted [Item].
 */
fun ItemEntity.toItem(): Item = Item(
    id = this.id,
    name = this.name,
    description = this.description,
    value = this.value,
    isFragile = this.isFragile,
    lastModified = Converters().fromTimestamp(this.lastModified) ?: Date(),
    boxId = this.boxId,
    imageUri = this.imageUri
)

/**
 * Updates an [ItemEntity] with the values from another [ItemEntity].
 *
 * This function takes another [ItemEntity] and copies its properties to the current [ItemEntity],
 * updating the `lastModified` timestamp to the current time.
 *
 * @param other The [ItemEntity] to update with.
 * @return The updated [ItemEntity].
 */
fun ItemEntity.updateWith(other: ItemEntity) : ItemEntity = copy (
    name = other.name,
    description = other.description,
    value = other.value,
    isFragile = other.isFragile,
    lastModified = System.currentTimeMillis(),
    imageUri = other.imageUri,
    boxId = other.boxId,
)
