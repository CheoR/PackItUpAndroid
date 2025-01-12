package com.example.packitupandroid.data.database.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update


/**
 * Base Data Access Object (DAO) interface providing common operations for all entities.
 *
 * @param T The type of the entity.
 */
interface BaseDao<T> {

    /**
     * Inserts an entity into the database. If the entity already exists, it replaces it.
     *
     * @param entity The entity to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: T)

    /**
     * Inserts a list of entities into the database. If any entity already exists, it replaces it.
     *
     * @param entities The list of entities to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(entities: List<T>)

    /**
     * Updates an existing entity in the database.
     *
     * @param entity The entity to be updated.
     */
    @Update
    suspend fun update(entity: T)

    /**
     * Updates a list of existing entities in the database.
     *
     * @param entities The list of entities to be updated.
     */
    @Update
    suspend fun updateAll(entities: List<T>)

    /**
     * Deletes an entity from the database.
     *
     * @param entity The entity to be deleted.
     */
    @Delete
    suspend fun delete(entity: T)

    /**
     * Deletes a list of entities from the database.
     *
     * @param entities The list of entities to be deleted.
     */
    @Delete
    suspend fun deleteAll(entities: List<T>)
}
