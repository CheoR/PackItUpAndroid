package com.example.packitupandroid.data.repository

import com.example.packitupandroid.data.database.dao.CollectionDao
import com.example.packitupandroid.data.model.Collection
import com.example.packitupandroid.utils.Result
import kotlinx.coroutines.flow.Flow
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
}
