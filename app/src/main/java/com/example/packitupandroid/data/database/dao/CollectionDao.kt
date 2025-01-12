package com.example.packitupandroid.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.packitupandroid.data.database.entities.CollectionEntity
import com.example.packitupandroid.data.model.QueryCollection
import com.example.packitupandroid.data.model.QueryDropdownOptions
import kotlinx.coroutines.flow.Flow


/**
 * Data Access Object (DAO) for interacting with the 'collections' table in the database.
 *
 * This interface provides methods for retrieving and deleting collections.
 * It extends both [BaseDao] and [CommonDao] to inherit common data access operations.
 */
@Dao
interface CollectionDao : BaseDao<CollectionEntity>, CommonDao<CollectionEntity> {

    /**
     * Retrieves a collection from the database by its unique ID.
     *
     * @param id The ID of the collection to retrieve.
     * @return A [Flow] emitting the [CollectionEntity] with the given ID, or null if not found.
     */
    @Query("SELECT * FROM collections WHERE id = :id")
    override fun get(id: String): Flow<CollectionEntity?>

    @Query("""
    WITH BoxSummary AS (
        SELECT 
            b.id,
            b.name,
            b.description,
            b.last_modified,
            b.collection_id,
            ROUND(SUM(i.value), 2) AS value,
            MAX(CASE WHEN i.is_fragile = 1 THEN 1 ELSE 0 END) AS is_fragile,
            COUNT( i.id) AS item_count
        FROM 
            boxes b
        LEFT JOIN 
            items i ON b.id = i.box_id
        GROUP BY 
            b.id
    )
    
    SELECT 
        c.id,
        c.name,
        c.description,
        c.last_modified,
        ROUND(SUM(bs.value), 2) AS value,
        MAX(CASE WHEN bs.is_fragile = 1 THEN 1 ELSE 0 END) AS is_fragile,
        COUNT( bs.id) AS box_count,
        SUM(bs.item_count) AS item_count
    FROM 
        collections c
    LEFT JOIN 
        BoxSummary bs ON c.id = bs.collection_id
    WHERE
        c.id = :id
    GROUP BY 
        c.id
    ORDER BY 
        c.last_modified;
    """)
    fun getQueryCollection(id: String): Flow<QueryCollection>

    @Query("""
    WITH BoxSummary AS (
        SELECT 
            b.id,
            b.name,
            b.description,
            b.last_modified,
            b.collection_id,
            ROUND(SUM(i.value), 2) AS value,
            MAX(CASE WHEN i.is_fragile = 1 THEN 1 ELSE 0 END) AS is_fragile,
            COUNT( i.id) AS item_count
        FROM 
            boxes b
        LEFT JOIN 
            items i ON b.id = i.box_id
        GROUP BY 
            b.id
    )
    
    SELECT 
        c.id,
        c.name,
        c.description,
        c.last_modified,
        ROUND(SUM(bs.value), 2) AS value,
        MAX(CASE WHEN bs.is_fragile = 1 THEN 1 ELSE 0 END) AS is_fragile,
        COUNT( bs.id) AS box_count,
        SUM(bs.item_count) AS item_count
    FROM 
        collections c
    LEFT JOIN 
        BoxSummary bs ON c.id = bs.collection_id
    GROUP BY 
        c.id
    ORDER BY 
        c.last_modified;
    """)
    fun getAllCollections(): Flow<List<QueryCollection>>

    @Query("SELECT c.id, c.name FROM collections c")
    fun getDropdownSelections(): Flow<List<QueryDropdownOptions>>

    /**
     * Retrieves all collections from the database, ordered by last modified timestamp in ascending order.
     *
     * @return A [Flow] emitting a list of all [CollectionEntity] objects, or an empty list if none are found.
     */
    @Query("SELECT * FROM collections ORDER BY last_modified ASC")
    override fun getAll(): Flow<List<CollectionEntity?>>

    /**
     * Deletes all collections from the database.
     */
    @Query("DELETE FROM collections")
    override suspend fun clear()
}
