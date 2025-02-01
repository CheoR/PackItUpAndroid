package com.example.packitupandroid.data.repository

import com.example.packitupandroid.data.database.dao.BoxDao
import com.example.packitupandroid.data.model.Box
import com.example.packitupandroid.data.model.CollectionIdAndName
import com.example.packitupandroid.utils.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


/**
 * Offline implementation of the [BoxesRepository] interface.
 *
 * This class provides an offline data access implementation for [Box] entities,
 * using the [BoxDao] to perform data operations on the local database.
 *
 * @param boxDao The Data Access Object (DAO) for accessing box data.
 */
class OfflineBoxesRepository(private val boxDao: BoxDao) : BoxesRepository {
    /**
     * Retrieves a box by its unique ID.
     *
     * @param id The ID of the box to retrieve.
     * @return A [Result] containing the box, or `null` if not found, or an error.
     */
    override suspend fun get(id: String): Result<Box?> {
        return try {
            val box = boxDao.get(id)
            Result.Success(box)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    /**
     * Retrieves all boxes as a stream.
     *
     * @return A [Flow] emitting a [Result] containing a list of all boxes, or an
     *         empty list if none are found, or an error.
     */
    override fun observeAll(): Flow<Result<List<Box?>>> {
        return boxDao.observeAll().map { Result.Success(it) }
    }

    /**
     * Retrieves a box by its unique ID as a stream.
     *
     * @param id The ID of the box to retrieve.
     * @return A [Flow] emitting a [Result] containing the box, or `null` if not
     *         found, or an error.
     */
    override fun observe(id: String): Flow<Result<Box?>> {
        return boxDao.observe(id).map { Result.Success(it) }
    }

    /**
     * Inserts a list of new boxes into the data source.
     *
     * @param data The list of boxes to insert.
     * @return A [Result] indicating the success or failure of the operation.
     */
    override suspend fun insert(data: List<Box>): Result<Unit> {
        return try {
            val entities = data.map { it.toEntity() }
            boxDao.insert(entities)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    /**
     * Updates an existing box in the data source.
     *
     * @param data The box to update.
     * @return A [Result] indicating the success or failure of the operation.
     */
    override suspend fun update(data: Box): Result<Unit> {
        return try {
            boxDao.update(data.toEntity())
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    /**
     * Deletes a list of boxes from the data source.
     *
     * @param data The list of IDs of the boxes to delete.
     * @return A [Result] indicating the success or failure of the operation.
     */
    override suspend fun delete(data: List<String>): Result<Unit> {
        return try {
            boxDao.delete(data)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    /**
     * Clears all boxes from the data source.
     *
     * @return A [Result] indicating the success or failure of the operation.
     */
    override suspend fun clear(): Result<Unit> {
        return try {
            boxDao.clear()
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    /**
     * Retrieves a list of collection IDs and their corresponding names.
     *
     * This function queries the underlying data source (represented by `boxDao`) to fetch
     * a list of collections. Each collection is represented by a [CollectionIdAndName] object,
     * which contains the ID and the name of the collection.
     *
     * The function returns a [Flow] that emits a [Result] object. This allows for asynchronous
     * and reactive handling of the data.
     *
     * - **Success:** If the data is successfully retrieved, the [Result] will be a [Result.Success]
     *   containing a [List] of [CollectionIdAndName?] objects. The list may contain null elements if
     *   the underlying data allows for it.
     * - **Failure:** If any error occurs during data retrieval, the [Result] will be a [Result.Failure]
     *   containing the [Throwable] that caused the error.
     *
     * @return A [Flow] emitting a [Result] containing a [List] of [CollectionIdAndName?] objects, or an error.
     *
     * @throws Any exception that `boxDao.listOfCollectionIdsAndNames()` throws will be propagated within Result.Failure.
     */
    override fun listOfCollectionIdsAndNames(): Flow<Result<List<CollectionIdAndName?>>> {
        return boxDao.listOfCollectionIdsAndNames().map { Result.Success(it) }
    }
}
