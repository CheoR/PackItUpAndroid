package com.example.packitupandroid.data.database.dao

import com.example.packitupandroid.data.model.BaseCardData
import kotlinx.coroutines.flow.Flow


/**
 * A common Data Access Object (DAO) interface to interact with the database.
 *
 * This interface defines common data access operations for entities that implement
 * the [BaseCardData] interface. It is intended to be used in conjunction with
 * [EntityDao] because `BaseDao` cannot access table names at runtime.
 *
 * Implementations of this interface must provide their own query functions for
 * data access to specific table names.
 *
 * @param D The type of the entity to be handled by this DAO, which must implement [BaseCardData].
 */
interface DataDao<D: BaseCardData> {

    /**
     * Retrieves an entity by its ID.
     *
     * This function fetches an entity of type [D] from the database based on the provided ID.
     * It returns the entity or null if no entity with the given ID is found.
     *
     * @param id The ID of the entity to retrieve.
     * @return The entity of type [D] with the given ID, or null if not found.
     */
    fun get(id: String): D?

    /**
     * Observes an entity by its ID.
     *
     * This function sets up a flow to observe changes to an entity of type [D] in the
     * database based on the provided ID. It emits the entity or null if no entity with
     * the given ID is found.
     *
     * @param id The ID of the entity to observe.
     * @return A [Flow] emitting the entity of type [D] with the given ID, or null if not found.
     */
    fun observe(id: String): Flow<D?>

    /**
     * Observes all entities.
     *
     * This function sets up a flow to observe changes to all entities of type [D] in the
     * database. It emits a list of all entities, or an empty list if no entities are found.
     *
     * @return A [Flow] emitting a list of all entities of type [D], or an empty list if none are found.
     */
    fun observeAll(): Flow<List<D?>>

    /**
     * Clears all entities from the database.
     *
     * This function removes all records of type [D] from the corresponding table in the database.
     */
    suspend fun clear()

    /**
     * Deletes entities from the database based on their unique IDs.
     *
     * This function removes records of type [D] from the corresponding table in the database
     * where the ID matches one of the IDs in the provided list.
     *
     * @param ids A list of IDs of the entities to delete.
     */
    suspend fun delete(ids: List<String>)
}
