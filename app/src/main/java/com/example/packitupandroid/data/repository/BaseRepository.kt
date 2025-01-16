package com.example.packitupandroid.data.repository

import com.example.packitupandroid.data.model.BaseCardData
import com.example.packitupandroid.utils.Result
import kotlinx.coroutines.flow.Flow

/**
 * A generic interface for repositories, providing an abstraction layer for data access.
 *
 * This interface defines common data operations for entities of type [D],
 * returning results wrapped in the [Result] sealed class. It serves as a base
 * for all repositories in the application, providing a consistent API for data
 * access.
 *
 * @param D The type of the data entity handled by this repository, which must
 *          implement [BaseCardData].
 */
interface BaseRepository<D: BaseCardData> {

    /**
     * Retrieves an entity by its unique ID.
     *
     * @param id The ID of the entity to retrieve.
     * @return A [Result] containing the entity, or `null` if not found, or an error.
     */
    suspend fun get(id: String): Result<D?>

    /**
     * Retrieves all entities as a stream.
     *
     * @return A [Flow] emitting a [Result] containing a list of all entities, or
     *         an empty list if none are found, or an error.
     */
    fun observeAll(): Flow<Result<List<D?>>>

    /**
     * Retrieves an entity by its unique ID as a stream.
     *
     * @param id The ID of the entity to retrieve.
     * @return A [Flow] emitting a [Result] containing the entity, or `null` if
     *         not found, or an error.
     */
    fun observe(id: String): Flow<Result<D?>>

    /**
     * Inserts a list of new entities into the data source.
     *
     * @param data The list of data to insert.
     * @return A [Result] indicating the success or failure of the operation.
     */
    suspend fun insert(data: List<D>): Result<Unit>

    /**
     * Updates an existing entity in the data source.
     *
     * @param data The data to update.
     * @return A [Result] indicating the success or failure of the operation.
     */
    suspend fun update(data: D): Result<Unit>

    /**
     * Deletes a list of data from the data source.
     *
     * @param data The list of IDs of the entities to delete.
     * @return A [Result] indicating the success or failure of the operation.
     */
    suspend fun delete(data: List<String>): Result<Unit>

    /**
     * Clears all entities from the data source.
     *
     * @return A [Result] indicating the success or failure of the operation.
     */
    suspend fun clear(): Result<Unit>
}

