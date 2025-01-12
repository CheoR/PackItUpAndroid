package com.example.packitupandroid.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.packitupandroid.data.database.entities.ItemEntity
import kotlinx.coroutines.flow.Flow


/**
 * Data Access Object (DAO) for interacting with the 'items' table in the database.
 *
 * This interface provides methods for retrieving, and deleting items.
 * It extends both [BaseDao] and [CommonDao] to inherit common data access operations.
 */
@Dao
interface ItemDao : BaseDao<ItemEntity>, CommonDao<ItemEntity> {

    // recommended to use Flow in persistence layer
    /**
     * Retrieves an item from the database by its unique ID.
     *
     * @param id The ID of the item to retrieve.
     * @return A [Flow] emitting the [ItemEntity] with the given ID, or null if not found.
     */
    @Query("SELECT * FROM items WHERE id = :id")
    override fun get(id: String): Flow<ItemEntity?>

    /**
     * Retrieves all items from the database.
     *
     * @return A [Flow] emitting a list of all [ItemEntity] objects, or an empty list if none are found.
     */
    @Query("SELECT * FROM items")
    override fun getAll(): Flow<List<ItemEntity?>>

    /**
     * Deletes all items from the database.
     */
    @Query("DELETE FROM items")
    override suspend fun clear()
}
