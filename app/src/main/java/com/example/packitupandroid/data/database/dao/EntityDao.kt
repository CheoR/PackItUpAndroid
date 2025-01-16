package com.example.packitupandroid.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update


/**
 * Base Data Access Object (DAO) interface providing common operations for all entities.
 *
 * This interface defines the basic data access operations that are common to all entities
 * in the application's database. It uses generics to allow these operations to be used
 * with [ItemEntity], [BoxEntity], and [CollectionEntity] entity type.
 *
 * @param E The type of the entity.
 */
@Dao
interface EntityDao<E> {
    /**
     * Inserts a list of entities into the database. If any entity already exists, it replaces it.
     *
     * This function inserts a list of entities into the database. If an entity with the same
     * primary key already exists, it will be replaced with the new entity.
     *
     * @param entities The list of entities to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entities: List<E>)

    /**
     * Updates an existing entity in the database.
     *
     * This function updates an existing entity in the database. The entity to be updated
     * is identified by its primary key.
     *
     * @param entity The entity to be updated.
     */
    @Update
    suspend fun update(entity: E)
}
