package com.example.packitupandroid.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.packitupandroid.data.database.entities.CollectionEntity
import com.example.packitupandroid.data.model.Collection
import kotlinx.coroutines.flow.Flow


/**
 * Data Access Object (DAO) for interacting with the 'collections' table in the database.
 *
 * This interface provides methods for retrieving, observing, and deleting collections.
 * It extends both [EntityDao] and [DataDao] to inherit common data access operations.
 */
@Dao
interface CollectionDao : EntityDao<CollectionEntity>, DataDao<Collection> {

    /**
     * Retrieves a collection from the database by its unique ID.
     *
     * This function fetches a [Collection] from the 'collections' table based on the provided ID.
     * It returns the collection or null if no collection with the given ID is found.
     *
     * @param id The ID of the collection to retrieve.
     * @return The [Collection] with the given ID, or null if not found.
     */
    @Query("""
    WITH BoxSummary AS (
        SELECT 
            b.id,
            b.collection_id,
            ROUND(SUM(i.value), 2) AS value,
            MAX(CASE WHEN i.is_fragile = 1 THEN 1 ELSE 0 END) AS isFragile,
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
        c.last_modified as lastModified,
        ROUND(SUM(bs.value), 2) AS value, /* sum of all the items in all the boxes that belong to this collection */
        MAX(CASE WHEN bs.isFragile = 1 THEN 1 ELSE 0 END) AS isFragile, /* if any item in collection is fragile */
        COUNT( bs.id) AS boxCount,
        SUM(bs.item_count) AS itemCount
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
    override fun get(id: String): Collection?

    /**
     * Observes a collection from the database by its unique ID.
     *
     * This function sets up a flow to observe changes to a [Collection] in the 'collections'
     * table based on the provided ID. It emits the collection or null if no collection with
     * the given ID is found.
     *
     * @param id The ID of the collection to observe.
     * @return A [Flow] emitting the [Collection] with the given ID, or null if not found.
     */
    @Query("""
    WITH BoxSummary AS (
        SELECT 
            b.id,
            b.collection_id,
            ROUND(SUM(i.value), 2) AS value,
            MAX(CASE WHEN i.is_fragile = 1 THEN 1 ELSE 0 END) AS isFragile,
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
        c.last_modified as lastModified,
        ROUND(SUM(bs.value), 2) AS value, /* sum of all the items in all the boxes that belong to this collection */
        MAX(CASE WHEN bs.isFragile = 1 THEN 1 ELSE 0 END) AS isFragile, /* if any item in collection is fragile */
        COUNT( bs.id) AS boxCount,
        SUM(bs.item_count) AS itemCount
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
    override fun observe(id: String): Flow<Collection?>

    /**
     * Observes all collections from the database, ordered by last modified timestamp in ascending order.
     *
     * This function sets up a flow to observe changes to all [Collection] objects in the
     * 'collections' table. It emits a list of all collections, or an empty list if no
     * collections are found. The collections are ordered by their last modified timestamp
     * in ascending order.
     *
     * @return A [Flow] emitting a list of all [Collection] objects, or an empty list if none are found.
     */
    @Query("""
    WITH BoxSummary AS (
        SELECT 
            b.id,
            b.collection_id,
            ROUND(SUM(i.value), 2) AS value,
            MAX(CASE WHEN i.is_fragile = 1 THEN 1 ELSE 0 END) AS isFragile,
            COUNT(i.id) AS item_count
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
        c.last_modified as lastModified,
        ROUND(SUM(bs.value), 2) AS value,
        MAX(CASE WHEN bs.isFragile = 1 THEN 1 ELSE 0 END) AS isFragile,
        COUNT( bs.id) AS boxCount,
        SUM(bs.item_count) AS itemCount
    FROM 
        collections c
    LEFT JOIN 
        BoxSummary bs ON c.id = bs.collection_id
    GROUP BY 
        c.id
    ORDER BY 
        c.last_modified;
    """)
    override fun observeAll(): Flow<List<Collection?>>

    /**
     * Deletes all collections from the database.
     *
     * This function removes all records from the 'collections' table.
     */
    @Query("DELETE FROM collections")
    override suspend fun clear()

    /**
     * Deletes collections from the database based on their unique IDs.
     *
     * This function removes records from the 'collections' table where the ID matches
     * one of the IDs in the provided list.
     *
     * @param ids A list of IDs of the collections to delete.
     */
    @Query("DELETE FROM collections WHERE id IN (:ids)")
    override suspend fun delete(ids: List<String>)
}
