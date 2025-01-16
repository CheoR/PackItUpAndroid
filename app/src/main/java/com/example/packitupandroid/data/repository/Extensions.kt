package com.example.packitupandroid.data.repository

import com.example.packitupandroid.data.database.entities.BoxEntity
import com.example.packitupandroid.data.database.entities.CollectionEntity
import com.example.packitupandroid.data.database.entities.ItemEntity
import com.example.packitupandroid.data.model.Box
import com.example.packitupandroid.data.model.Collection
import com.example.packitupandroid.data.model.Item
import com.example.packitupandroid.data.model.QueryBox
import com.example.packitupandroid.data.model.QueryCollection
import com.example.packitupandroid.data.model.QuerySummary
import com.example.packitupandroid.data.model.Summary
import com.example.packitupandroid.utils.Converters
import java.util.Date


/**
 * Converts an [Item] data class to an [ItemEntity] database entity.
 *
 * This function maps the properties of an [Item] object to the corresponding
 * properties of an [ItemEntity] object, which is used for database storage.
 * It also handles the conversion of the [lastModified] date to a timestamp using
 * the [Converters] utility.
 *
 * @receiver The [Item] object to convert.
 * @return An [ItemEntity] object with the data from the [Item].
 */
fun Item.toEntity(): ItemEntity = ItemEntity(
    id = this.id,
    name = this.name,
    description = this.description,
    value = this.value,
    isFragile = this.isFragile,
    lastModified = Converters().dateToTimestamp(this.lastModified) ?: 0L,
    boxId = this.boxId,
    imageUri = this.imageUri,
)

/**
 * Creates a new Item instance by updating the current item with the properties of another item.
 *
 * This function copies the `name`, `description`, `value`, `isFragile`, `boxId`, and `imageUri` properties from the `other` item.
 * It also updates the `lastModified` property to the current date and time.
 * The `currentSelection` of the current item is retained.
 *
 * @param other The other Item instance whose properties will be used to update this item.
 * @return A new Item instance with the updated properties.
 *
 */
fun Item.updateWith(other: Item) : Item = copy (
    name = other.name,
    description = other.description,
    value = other.value,
    isFragile = other.isFragile,
    lastModified = Date(),
    boxId = other.boxId,
    currentSelection = this.currentSelection,
    imageUri = other.imageUri,
)


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

/**
 * Represents a Box data object.
 *
 * @property id The unique identifier of the box.
 * @property name The name of the box.
 * @property description A description of the box.
 * @property lastModified The last modified date of the box.
 * @property collectionId The ID of the collection the box belongs to.
 */
fun Box.toEntity(): BoxEntity = BoxEntity(
    id = this.id,
    name = this.name,
    description = this.description,
    lastModified = Converters().dateToTimestamp(this.lastModified) ?: 0L, // System.currentTimeMillis(),
    collectionId = this.collectionId,
)

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

/**
 * Converts a [Box] object to a [QueryBox] object.
 *
 * This function takes a [Box] instance and extracts its properties to create a corresponding [QueryBox] instance.
 * The properties that are copied include:
 * - `id`: The unique identifier of the box.
 * - `name`: The name of the box.
 * - `description`: A description of the box.
 * - `value`: The value associated with the box.
 * - `is_fragile`: A boolean indicating if the box's contents are fragile.
 * - `last_modified`: The timestamp of the last modification to the box.
 * - `item_count`: The number of items contained within the box.
 * - `collection_id`: The identifier of the collection to which the box belongs.
 *
 * @receiver The [Box] object to be converted.
 * @return A new [QueryBox] object containing the properties extracted from the [Box].
 */
fun Box.toQueryBox(): QueryBox = QueryBox(
    id = this.id,
    name = this.name,
    description = this.description,
    value = this.value,
    is_fragile = this.isFragile,
    last_modified = this.lastModified, // Date(), // this.lastModified.time,
    item_count = this.item_count,
    collection_id = this.collectionId,
)

/**
 * Represents a box with various attributes.
 *
 * @property name The name of the box.
 * @property description A description of the box.
 * @property value The value of the box.
 * @property isFragile Whether the box is fragile.
 * @property item_count The number of items in the box.
 * @property lastModified The last modification date of the box.
 * @property collectionId The ID of the collection this box belongs to.
 */
fun Box.updateWith(other: Box) : Box = copy (
    name = other.name,
    description = other.description,
    value = other.value,
    isFragile = other.isFragile,
    item_count = other.item_count,
    lastModified = Date(),
    collectionId = other.collectionId,
)


/**
 * Converts a [Collection] object to a [CollectionEntity] object.
 *
 * This function takes a [Collection] object and creates a corresponding [CollectionEntity]
 * object, mapping its properties (id, name, description) to the entity. It also sets
 * the `lastModified` field to the current system time.
 *
 * @receiver The [Collection] object to convert.
 * @return A new [CollectionEntity] object with the properties from the [Collection]
 *         and the current timestamp as the last modified time.
 */
fun Collection.toEntity(): CollectionEntity = CollectionEntity(
    id = this.id,
    name = this.name,
    description = this.description,
    lastModified = System.currentTimeMillis(),
)

/**
 * Represents a collection of data, potentially with sensitive properties.
 *
 * @property id The unique identifier of the collection.
 * @property name The name of the collection.
 * @property description A description of the collection.
 * @property value The underlying data or content of the collection.
 * @property isFragile Indicates if the collection is fragile or sensitive.
 * @property lastModified The last modification timestamp of the collection.
 */
fun Collection.toQueryCollection(): QueryCollection = QueryCollection(
    id = this.id,
    name = this.name,
    description = this.description,
    value = this.value,
    is_fragile = this.isFragile,
    last_modified = this.lastModified,
)

/**
 * Updates the current `Collection` with the properties of another `Collection`.
 *
 * This function creates a new `Collection` object with the same underlying data
 * as the current instance, but with the following properties replaced by the
 * corresponding properties from the `other` Collection:
 *  - `name`
 *  - `description`
 *  - `value`
 *  - `isFragile`
 *  - `item_count`
 *  - `box_count`
 *
 * The `lastModified` property of the new `Collection` is set to the current date and time.
 *
 * This method does not modify the original `Collection`.
 *
 * @param other The `Collection` to take updated properties from.
 * @return A new `Collection` object with the updated properties.
 *
 * @throws NullPointerException if other is null
 */
fun Collection.updateWith(other: Collection) : Collection = copy (
    name = other.name,
    description = other.description,
    value = other.value,
    isFragile = other.isFragile,
    item_count = other.item_count,
    box_count = other.box_count,
    lastModified = Date(),
)

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

/**
 * Converts a [QueryCollection] object to a [Collection] object.
 *
 * This function takes a [QueryCollection] instance and extracts its properties
 * to create a new [Collection] instance. It essentially maps the data from
 * the database representation ([QueryCollection]) to the application's model
 * representation ([Collection]).
 *
 * @receiver The [QueryCollection] instance to convert.
 * @return A new [Collection] instance populated with data from the [QueryCollection].
 *
 * @property id The unique identifier of the collection.
 * @property name The name of the collection.
 * @property description A description of the collection's contents or purpose.
 * @property item_count The number of items in the collection.
 * @property box_count The number of boxes associated with the collection.
 * @property value The total value of the items within the collection.
 * @property isFragile A boolean indicating whether the items in the collection are fragile.
 * @property lastModified The timestamp indicating the last modification of the collection.
 */
fun QueryCollection.toCollection(): Collection = Collection(
    id = this.id,
    name = this.name,
    description = this.description,
    item_count = this.item_count,
    box_count = this.box_count,
    value = this.value,
    isFragile = this.is_fragile,
    lastModified = this.last_modified,  //  Converters().fromTimestamp(this.last_modified) ?: Date(),
)

/**
 * Converts a [QuerySummary] object to a [Summary] object.
 *
 * This function takes a [QuerySummary] instance and a name as input. It then creates a new [Summary] object,
 * populating its fields based on the data in the [QuerySummary] and the provided name.
 *
 * @param name The name to be assigned to the [Summary] object's id and name fields.
 * @return A new [Summary] object containing the data from the [QuerySummary] and the specified name.
 */
fun QuerySummary.toSummary(
    name: String
): Summary = Summary(
    id = name,
    name = name,
    isFragile = this.is_fragile,
    value = this.value,
    itemCount = this.item_count,
    boxCount = this.box_count,
    collectionCount = this.collection_count,
)