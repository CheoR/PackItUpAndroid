package com.example.packitupandroid.data.repository

import com.example.packitupandroid.data.database.entities.BoxEntity
import com.example.packitupandroid.data.database.entities.CollectionEntity
import com.example.packitupandroid.data.database.entities.ItemEntity
import com.example.packitupandroid.data.model.Box
import com.example.packitupandroid.data.model.Collection
import com.example.packitupandroid.data.model.Item
import com.example.packitupandroid.utils.Converters
import java.util.Date


/**
 * Converts an [ItemEntity] to an [Item] data class.
 *
 * This extension function facilitates the conversion of an [ItemEntity], typically retrieved
 * from a database, into an [Item] data class, suitable for use within the application's
 * business logic or UI. It handles the conversion of the `lastModified` property from a
 * Long timestamp to a Date object using the [Converters] class.
 *
 * @return The converted [Item] data class.
 */
fun ItemEntity.toItem(): Item = Item(
    id = this.id,
    name = this.name,
    description = this.description,
    value = this.value,
    isFragile = this.isFragile,
    lastModified = Converters().fromTimestamp(this.lastModified) ?: Date(),
    boxId = this.boxId,
    imageUri = this.imageUri,
)

/**
 * Converts an [Item] data class to an [ItemEntity].
 *
 * This extension function facilitates the conversion of an [Item] data class, used within
 * the application, into an [ItemEntity], suitable for storage in a database. It handles the
 * conversion of the `lastModified` property from a Date object to a Long timestamp.
 *
 * @return The converted [ItemEntity].
 */
fun Item.toEntity(): ItemEntity = ItemEntity(
    id = this.id,
    name = this.name,
    description = this.description,
    value = this.value,
    isFragile = this.isFragile,
    lastModified = this.lastModified.time,
    boxId = this.boxId,
    imageUri = this.imageUri,
)

/**
 * Converts a [BoxEntity] to a [Box] data class.
 *
 * This extension function facilitates the conversion of a [BoxEntity], typically retrieved
 * from a database, into a [Box] data class, suitable for use within the application's
 * business logic or UI. It handles the conversion of the `lastModified` property from a
 * Long timestamp to a Date object using the [Converters] class. The `value` property is
 * initialized to 0.0.
 *
 * @return The converted [Box] data class.
 */
fun BoxEntity.toBox(): Box = Box(
    id = this.id,
    name = this.name,
    description = this.description,
    lastModified = Converters().fromTimestamp(this.lastModified) ?: Date(),
    value = 0.0,
    collectionId = this.collectionId,
)

/**
 * Converts a [Box] data class to a [BoxEntity].
 *
 * This extension function facilitates the conversion of a [Box] data class, used within
 * the application, into a [BoxEntity], suitable for storage in a database. It handles the
 * conversion of the `lastModified` property from a Date object to a Long timestamp.
 *
 * @return The converted [BoxEntity].
 */
fun Box.toEntity(): BoxEntity = BoxEntity(
    id = this.id,
    name = this.name,
    description = this.description,
    lastModified = this.lastModified.time,
    collectionId = this.collectionId,
)

/**
 * Converts a [CollectionEntity] to a [Collection] data class.
 *
 * This extension function facilitates the conversion of a [CollectionEntity], typically retrieved
 * from a database, into a [Collection] data class, suitable for use within the application's
 * business logic or UI. It handles the conversion of the `lastModified` property from a
 * Long timestamp to a Date object using the [Converters] class. The `value` property is
 * initialized to 0.0, `isFragile` is set to true, `itemCount` to 2, and `boxCount` to 4.
 *
 * @return The converted [Collection] data class.
 */
fun CollectionEntity.toCollection(): Collection = Collection(
    id = this.id,
    name = this.name,
    description = this.description,
    lastModified = Converters().fromTimestamp(this.lastModified) ?: Date(),
    value = 0.0,
    isFragile = true,
    itemCount = 2,
    boxCount = 4,
)

/**
 * Converts a [Collection] data class to a [CollectionEntity].
 *
 * This extension function facilitates the conversion of a [Collection] data class, used within
 * the application, into a [CollectionEntity], suitable for storage in a database. It handles the
 * conversion of the `lastModified` property from a Date object to a Long timestamp.
 *
 * @return The converted [CollectionEntity].
 */
fun Collection.toEntity(): CollectionEntity = CollectionEntity(
    id = this.id,
    name = this.name,
    description = this.description,
    lastModified = this.lastModified.time,
)
