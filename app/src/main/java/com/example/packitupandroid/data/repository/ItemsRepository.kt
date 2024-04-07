package com.example.packitupandroid.data.repository

import com.example.packitupandroid.data.database.entities.ItemEntity
import kotlinx.coroutines.flow.Flow

/**
 * Repository that provides insert, update, delete, and retrieve of [ItemEntity] from a given data source.
 */
interface ItemsRepository {
    /**
     * Retrieve all the items from the given data source.
     */
    fun getAllItemsStream(): Flow<List<ItemEntity>>

    /**
     * Retrieve an item from the given data source that matches with the [id].
     */
    fun getItemStream(id: String): Flow<ItemEntity?>

    /**
     * Insert item in the data source
     */
    suspend fun insertItem(item: ItemEntity)

    /**
     * Insert more than one item in the data source
     */
    suspend fun insertAll(items: List<ItemEntity>)

    /**
     * Delete item from the data source
     */
    suspend fun deleteItem(item: ItemEntity)

    /**
     * Delete more than one item from the data source
     */
    suspend fun deleteAll(items: List<ItemEntity>)

    /**
     * Update item in the data source
     */
    suspend fun updateItem(item: ItemEntity)
}
