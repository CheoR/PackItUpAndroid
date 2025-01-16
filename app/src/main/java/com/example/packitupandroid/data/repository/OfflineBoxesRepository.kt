package com.example.packitupandroid.data.repository

import com.example.packitupandroid.data.database.dao.BoxDao
import com.example.packitupandroid.data.model.Box
import com.example.packitupandroid.data.model.QueryBox
import com.example.packitupandroid.data.model.QueryDropdownOptions
import kotlinx.coroutines.flow.firstOrNull
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
     * Retrieves a QueryBox from the database by its ID.
     *
     * This function fetches a QueryBox entity from the underlying data source (likely a Room database)
     * using the provided ID. It returns the QueryBox if found, or null if no QueryBox with the given
     * ID exists.  The function is a suspending function, meaning it must be called within a coroutine.
     * It uses `firstOrNull()` to retrieve the first matching QueryBox (if any) and handle cases where
     * the query might return no results.
     *
     * @param id The unique identifier of the QueryBox to retrieve.
     * @return The QueryBox associated with the given ID, or null if no such QueryBox exists.
     */
    override suspend fun getQueryBox(id: String): QueryBox? = boxDao.getQueryBox(id).firstOrNull()

    /**
     * Retrieves a list of dropdown selection options from the data source.
     *
     * This function queries the underlying `boxDao` to fetch a list of `QueryDropdownOptions`.
     * It returns a `Flow` that emits the list of options, allowing for reactive handling
     * of changes to the dropdown selections.  This function is marked as `suspend` as the
     * underlying data access might require a database query or other long-running operation.
     *
     * @return A `Flow` that emits a list of `QueryDropdownOptions`. Each emission represents the
     *         current set of available dropdown options.
     *
     * @throws Exception Any exceptions thrown by `boxDao.getDropdownSelections()` will propagate
     *         through the `Flow`.
     */
    override suspend fun getDropdownSelections(): Flow<List<QueryDropdownOptions>> = boxDao.getDropdownSelections()
}
