package com.example.packitupandroid.data.repository

import com.example.packitupandroid.data.database.dao.ItemDao
import com.example.packitupandroid.data.model.BoxIdAndName
import com.example.packitupandroid.data.model.Item
import com.example.packitupandroid.utils.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


/**
 * Offline implementation of the [ItemsRepository] interface.
 *
 * This class provides an offline data access implementation for [Item] entities,
 * using the [ItemDao] to perform data operations on the local database.
 *
 * @param itemDao The Data Access Object (DAO) for accessing item data.
 */
class OfflineItemsRepository(private val itemDao: ItemDao) : ItemsRepository {
    /**
     * Retrieves an item by its unique ID.
     *
     * @param id The ID of the item to retrieve.
     * @return A [Result] containing the item, or `null` if not found, or an error.
     */
    override suspend fun get(id: String): Result<Item?> {
        return try {
            val item = itemDao.get(id)
            Result.Success(item)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    /**
     * Retrieves all items as a stream.
     *
     * @return A [Flow] emitting a [Result] containing a list of all items, or an
     *         empty list if none are found, or an error.
     */
    override fun observeAll(): Flow<Result<List<Item?>>> {
        return itemDao.observeAll().map { Result.Success(it) }
    }

    /**
     * Retrieves an item by its unique ID as a stream.
     *
     * @param id The ID of the item to retrieve.
     * @return A [Flow] emitting a [Result] containing the item, or `null` if not
     *         found, or an error.
     */
    override fun observe(id: String): Flow<Result<Item?>> {
        return itemDao.observe(id).map { Result.Success(it) }
    }

    /**
     * Inserts a list of new items into the data source.
     *
     * @param data The list of items to insert.
     * @return A [Result] indicating the success or failure of the operation.
     */
    override suspend fun insert(data: List<Item>): Result<Unit> {
        return try {
            val entities = data.map { it.toEntity() }
            itemDao.insert(entities)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    /**
     * Updates an existing item in the data source.
     *
     * @param data The item to update.
     * @return A [Result] indicating the success or failure of the operation.
     */
    override suspend fun update(data: Item): Result<Unit> {
        return try {
            itemDao.update(data.toEntity())
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    /**
     * Deletes a list of items from the data source.
     *
     * @param data The list of IDs of the items to delete.
     * @return A [Result] indicating the success or failure of the operation.
     */
    override suspend fun delete(data: List<String>): Result<Unit> {
        return try {
            itemDao.delete(data)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    /**
     * Clears all items from the data source.
     *
     * @return A [Result] indicating the success or failure of the operation.
     */
    override suspend fun clear(): Result<Unit> {
        return try {
            itemDao.clear()
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    /**
     * Retrieves a list of box IDs and their corresponding names.
     *
     * This function queries the underlying data source (e.g., a database) via the `itemDao`
     * to fetch a list of `BoxIdAndName` objects. Each `BoxIdAndName` represents a single box
     * and contains its unique identifier (ID) and associated name.
     *
     * The function returns a `Flow` of `Result` that either:
     *  - Emits a `Result.Success` containing a list of `BoxIdAndName?` objects.
     *  - Emits an appropriate `Result.Failure` if any error occurs during data retrieval
     *
     * The `BoxIdAndName?` type allows for null values within the list, meaning that a box might exist without a name, if supported by the data model.
     * The result list can be empty if no boxes are available.
     *
     * @return A `Flow` emitting a `Result` containing a list of `BoxIdAndName?` objects.
     *         The `Flow` may emit multiple times as the underlying data changes, if the
     *         data source supports it.
     *         It can also emit a failure if an exception is thrown.
     *
     * @throws Any exceptions that might be thrown by the underlying data access layer (e.g., database).
     *
     * @sample
     * // Example usage within a coroutine scope:
     * launch {
     *     listOfBoxIdsAndNames().collect { result ->
     *         when (result) {
     *             is Result.Success -> {
     *                 val boxes = result.data
     *                 if (boxes.isEmpty()) {
     *                     println("No boxes found.")
     *                 } else {
     *                     boxes.forEach { box ->
     *                        println("Box ID: ${box?.boxId}, Name: ${box?.name}")
     *                     }
     *                  }
     *             }
     *             is Result.Failure -> {
     *                 println("Error fetching boxes: ${result.exception}")
     *             }
     *         }
     *     }
     * }
     *
     */
    override fun listOfBoxIdsAndNames(): Flow<Result<List<BoxIdAndName?>>> {
        return itemDao.listOfBoxIdsAndNames().map { Result.Success(it) }
    }
}
