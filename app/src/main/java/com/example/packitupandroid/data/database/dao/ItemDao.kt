package com.example.packitupandroid.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.packitupandroid.data.database.entities.ItemEntity
import com.example.packitupandroid.data.model.Item
import kotlinx.coroutines.flow.Flow


/**
 * Data Access Object (DAO) for interacting with the 'items' table in the database.
 *
 * This interface provides methods for retrieving, observing, and deleting items.
 * It extends both [EntityDao] and [DataDao] to inherit common data access operations.
 */
@Dao
interface ItemDao : EntityDao<ItemEntity>, DataDao<Item> {

    /**
     * Retrieves an item from the database by its unique ID.
     *
     * This function fetches an [Item] from the 'items' table based on the provided ID.
     * It returns the item or null if no item with the given ID is found.
     *
     * @param id The ID of the item to retrieve.
     * @return The [Item] with the given ID, or null if not found.
     */
    @Query("""
        SELECT
            i.*,
            i.box_id as boxId,
            i.image_uri as imageUri,
            i.is_fragile AS isFragile,
            i.last_modified AS lastModified
        FROM items i
        WHERE id = :id
    """)    override fun get(id: String): Item?

    /**
     * Observes an item from the database by its unique ID.
     *
     * This function sets up a flow to observe changes to an [Item] in the 'items' table
     * based on the provided ID. It emits the item or null if no item with the given ID
     * is found.
     *
     * @param id The ID of the item to observe.
     * @return A [Flow] emitting the [Item] with the given ID, or null if not found.
     */
    @Query("""
        SELECT
            i.*,
            i.box_id as boxId,
            i.image_uri as imageUri,
            i.is_fragile AS isFragile,
            i.last_modified AS lastModified
        FROM items i
        WHERE id = :id
    """)
    override fun observe(id: String): Flow<Item?>

    /**
     * Observes all items from the database.
     *
     * This function sets up a flow to observe changes to all [Item] objects in the 'items'
     * table. It emits a list of all items, or an empty list if no items are found.
     *
     * @return A [Flow] emitting a list of all [Item] objects, or an empty list if none are found.
     */
    @Query("""
        SELECT
            i.*,
            i.box_id as boxId,
            i.image_uri as imageUri,
            i.is_fragile AS isFragile,
            i.last_modified AS lastModified
        FROM items i
    """)
    override fun observeAll(): Flow<List<Item?>>

    /**
     * Deletes all items from the database.
     *
     * This function removes all records from the 'items' table.
     */
    @Query("DELETE FROM items")
    override suspend fun clear()

    /**
     * Deletes items from the database based on their unique IDs.
     *
     * This function removes records from the 'items' table where the ID matches
     * one of the IDs in the provided list.
     *
     * @param ids A list of IDs of the items to delete.
     */
    @Query("DELETE FROM items WHERE id IN (:ids)")
    override suspend fun delete(ids: List<String>)
}
