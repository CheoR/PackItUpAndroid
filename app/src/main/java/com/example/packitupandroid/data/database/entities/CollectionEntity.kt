package com.example.packitupandroid.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.packitupandroid.data.model.Collection
import com.example.packitupandroid.utils.Converters
import java.util.Date


/**
 * Represents a collection entity in the database.
 *
 * @property id The unique identifier for the collection.
 * @property name The name of the collection.
 * @property description An optional description of the collection. Defaults to null.
 * @property lastModified The timestamp of the last modification. Defaults to 0.
 */
@Entity(tableName = "collections")
data class CollectionEntity(
    @PrimaryKey
    override val id: String,
    override val name: String,
    @ColumnInfo(defaultValue = "null")
    override val description: String? = null,
    @ColumnInfo(
        name = "last_modified",
        defaultValue = "0"
    )
    override val lastModified: Long = 0,
) : BaseEntity

/**
 * Converts a [CollectionEntity] to a [Collection].
 *
 * This function takes a [CollectionEntity] and maps its properties to a new [Collection] object.
 * It also converts the `lastModified` timestamp to a `Date` object using the [Converters] class.
 *
 * @return The converted [Collection].
 */
fun CollectionEntity.toCollection(): Collection = Collection(
    id = this.id,
    name = this.name,
    description = this.description,
    lastModified = Converters().fromTimestamp(this.lastModified) ?: Date(),
)

/**
 * Updates a [CollectionEntity] with the values from another [CollectionEntity].
 *
 * This function takes another [CollectionEntity] and copies its properties to the current [CollectionEntity],
 * updating the `lastModified` timestamp to the current time.
 *
 * @param other The [CollectionEntity] to update with.
 * @return The updated [CollectionEntity].
 */
fun CollectionEntity.updateWith(other: CollectionEntity) : CollectionEntity = copy (
    name = other.name,
    description = other.description,
    lastModified = System.currentTimeMillis(),
)


//fun CollectionEntity.toQueryCollection(collectionRepository: CollectionsRepository): QueryCollection {
//    val queryCollection: QueryCollection
//
//        collectionRepository.getQueryCollection(this.id)
//    return QueryCollection(
//        id = this.id,
//        name = this.name,
//        description = this.description,
//        last_modified = Converters().fromTimestamp(this.lastModified) ?: Date(),
//        value = queryCollection.value,
//        is_fragile = queryCollection.is_fragile,
//        item_count = queryCollection.item_count,
//        box_count = queryCollection.box_count,
//
//    )
//}