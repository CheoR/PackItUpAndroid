package com.example.packitupandroid.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.packitupandroid.data.model.Box
import com.example.packitupandroid.utils.Converters
import java.util.Date
import java.util.UUID


/**
 * Represents a box entity in the database.
 *
 * This data class is used to store information about a box in the database.
 * It includes properties such as the box's ID, name, description, last modification
 * timestamp, and the ID of the collection it belongs to.
 *
 * @property id The unique identifier for the box, generated using UUID.randomUUID().
 * @property name The name of the box.
 * @property description An optional description of the box. Defaults to null.
 * @property lastModified The timestamp of the last modification of the box. Defaults to 0.
 * @property collectionId The identifier of the collection containing the box. Defaults to null.
 */
@Entity(
    tableName = "boxes",
    foreignKeys = [
        ForeignKey(
            entity = CollectionEntity::class,
            parentColumns = ["id"],
            childColumns = ["collection_id"],
            onDelete = ForeignKey.CASCADE
        ),
    ],
    indices = [Index(value = ["collection_id"])]
)
data class BoxEntity(
    @PrimaryKey
    override val id: String = UUID.randomUUID().toString(),
    override val name: String,
    @ColumnInfo(defaultValue = "null")
    override val description: String? = null,
    @ColumnInfo(
        name = "last_modified",
        defaultValue = "0"
    )
    override val lastModified: Long = 0,
    @ColumnInfo(
        name = "collection_id",
        defaultValue = "null",
    )
    val collectionId: String? = null,
) : BaseEntity

/**
 * Converts a [BoxEntity] to a [Box].
 *
 * This function takes a [BoxEntity] and maps its properties to a new [Box] object.
 * It also converts the `lastModified` timestamp to a `Date` object using the [Converters] class.
 *
 * @return The converted [Box].
 */
fun BoxEntity.toBox(): Box = Box(
    id = this.id,
    name = this.name,
    description = this.description,
    lastModified = Converters().fromTimestamp(this.lastModified) ?: Date(),
    collectionId = this.collectionId
)

/**
 * Updates a [BoxEntity] with the values from another [BoxEntity].
 *
 * This function takes another [BoxEntity] and copies its properties to the current [BoxEntity],
 * updating the `lastModified` timestamp to the current time.
 *
 * @param other The [BoxEntity] to update with.
 * @return The updated [BoxEntity].
 */
fun BoxEntity.updateWith(other: BoxEntity) : BoxEntity = copy (
    name = other.name,
    description = other.description,
    lastModified = System.currentTimeMillis(),
    collectionId = other.collectionId,
)
