package com.example.packitupandroid.data.database.dao

import kotlinx.coroutines.flow.Flow


/**
 * A common Data Access Object (DAO) interface to interact with the database.
 * To be used with [BaseDao] because `BaseDao` cannot access table name at runtime.
 * Interfaces that implement [CommonDao] must its own query function for data access to table name.
 *
 * @param T The type of the entity to be handled by this DAO.
 */
interface CommonDao<T> {

    /**
     * Retrieves an entity by its ID.
     *
     * @param id The ID of the entity to retrieve.
     * @return A [Flow] emitting the entity with the given ID, or null if not found.
     */
    fun get(id: String): Flow<T?>

    /**
     * Retrieves all entities.
     *
     * @return A [Flow] emitting a list of all entities, or an empty list if none are found.
     */
    fun getAll(): Flow<List<T?>>

    /**
     * Clears all entities from the database.
     */
    suspend fun clear()
}
