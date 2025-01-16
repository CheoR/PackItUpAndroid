package com.example.packitupandroid.data.repository

import com.example.packitupandroid.data.database.dao.CollectionDao
import com.example.packitupandroid.data.model.Collection
import com.example.packitupandroid.data.model.QueryCollection
import com.example.packitupandroid.data.model.QueryDropdownOptions
import com.example.packitupandroid.utils.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map


/**
 * Offline implementation of the [CollectionsRepository] interface.
 *
 * This class provides an offline data access implementation for [Collection] entities,
 * using the [CollectionDao] to perform data operations on the local database.
 *
 * @param collectionDao The Data Access Object (DAO) for accessing collection data.
 */
class OfflineCollectionsRepository(private val collectionDao: CollectionDao) : CollectionsRepository {
    /**
     * Retrieves a collection by its unique ID.
     *
     * @param id The ID of the collection to retrieve.
     * @return A [Result] containing the collection, or `null` if not found, or an error.
     */
    override suspend fun get(id: String): Result<Collection?> {
        return try {
            val collection = collectionDao.get(id)
            Result.Success(collection)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    /**
     * Retrieves all collections as a stream.
     *
     * @return A [Flow] emitting a [Result] containing a list of all collections, or
     *         an empty list if none are found, or an error.
     */
    override fun observeAll(): Flow<Result<List<Collection?>>> {
        return collectionDao.observeAll().map { Result.Success(it) }
    }

    /**
     * Retrieves a collection by its unique ID as a stream.
     *
     * @param id The ID of the collection to retrieve.
     * @return A [Flow] emitting a [Result] containing the collection, or `null` if
     *         not found, or an error.
     */
    override fun observe(id: String): Flow<Result<Collection?>> {
        return collectionDao.observe(id).map { Result.Success(it) }
    }

    /**
     * Inserts a list of new collections into the data source.
     *
     * @param data The list of collections to insert.
     * @return A [Result] indicating the success or failure of the operation.
     */
    override suspend fun insert(data: List<Collection>): Result<Unit> {
        return try {
            val entities = data.map { it.toEntity() }
            collectionDao.insert(entities)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    /**
     * Updates an existing collection in the data source.
     *
     * @param data The collection to update.
     * @return A [Result] indicating the success or failure of the operation.
     */
    override suspend fun update(data: Collection): Result<Unit> {
        return try {
            collectionDao.update(data.toEntity())
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    /**
     * Deletes a list of collections from the data source.
     *
     * @param data The list of IDs of the collections to delete.
     * @return A [Result] indicating the success or failure of the operation.
     */
    override suspend fun delete(data: List<String>): Result<Unit> {
        return try {
            collectionDao.delete(data)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    /**
     * Clears all collections from the data source.
     *
     * @return A [Result] indicating the success or failure of the operation.
     */
    override suspend fun clear(): Result<Unit> {
        return try {
            collectionDao.clear()
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    /**
     * Retrieves a QueryCollection from the data source by its ID.
     *
     * This function accesses the underlying data source (likely a database or local storage)
     * through the `collectionDao` to fetch a QueryCollection based on the provided ID.
     * It utilizes Kotlin coroutines and Flow to handle asynchronous operations efficiently.
     *
     * @param id The unique identifier of the QueryCollection to retrieve.
     * @return The QueryCollection associated with the given ID, or `null` if no such collection is found.
     *
     * @throws Exception if any error occurs during the data retrieval process.
     */
    override suspend fun getQueryCollection(id: String): QueryCollection? = collectionDao.getQueryCollection(id).firstOrNull()

    /**
     * Retrieves a list of dropdown selection options from the data source.
     *
     * This function is a suspend function that fetches the available options for dropdowns,
     * likely used to populate UI elements like selection lists or filters. It retrieves
     * these options from the `collectionDao`, which represents the data access layer.
     *
     * The function returns a `Flow` of `List<QueryDropdownOptions>`, enabling reactive
     * updates to the UI whenever the dropdown options change in the data source.
     *
     * @return A [Flow] emitting a [List] of [QueryDropdownOptions]. Each [QueryDropdownOptions]
     *         represents a single option available for selection in a dropdown. The flow
     *         will emit a new list whenever the underlying data source changes.
     *
     * Example Usage:
     * ```
     * lifecycleScope.launch {
     *   viewModel.getDropdownSelections().collect { dropdownOptions ->
     *     // Update the UI with the new dropdown options
     *     myDropdown.setOptions(dropdownOptions)
     *   }
     * }
     * ```
     */
    override suspend fun getDropdownSelections(): Flow<List<QueryDropdownOptions>> = collectionDao.getDropdownSelections()
}
