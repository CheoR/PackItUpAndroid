package com.example.packitupandroid.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID


/**
 * Represents a collection entity in the database.
 *
 * This data class is used to store information about a collection in the database.
 * It includes properties such as the collection's ID, name, description, and last
 * modification timestamp.
 *
 * @property id The unique identifier for the collection, generated using UUID.randomUUID().
 * @property name The name of the collection.
 * @property description An optional description of the collection. Defaults to null.
 * @property lastModified The timestamp of the last modification of the collection. Defaults to 0.
 */
@Entity(tableName = "collections")
data class CollectionEntity(
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
) : BaseEntity


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