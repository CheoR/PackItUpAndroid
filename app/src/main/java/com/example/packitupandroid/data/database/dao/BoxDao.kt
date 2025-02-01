package com.example.packitupandroid.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.packitupandroid.data.database.entities.BoxEntity
import com.example.packitupandroid.data.model.Box
import com.example.packitupandroid.data.model.CollectionIdAndName
import kotlinx.coroutines.flow.Flow


/**
 * Data Access Object (DAO) for interacting with the 'boxes' table in the database.
 *
 * This interface provides methods for retrieving, observing, and deleting boxes.
 * It extends both [EntityDao] and [DataDao] to inherit common data access operations.
 */
@Dao
interface BoxDao : EntityDao<BoxEntity>, DataDao<Box> {

    /**
     * Retrieves a box from the database by its unique ID.
     *
     * This function fetches a [Box] from the 'boxes' table based on the provided ID.
     * It returns the box or null if no box with the given ID is found.
     *
     * @param id The ID of the box to retrieve.
     * @return The [Box] with the given ID, or null if not found.
     */
    @Query("""
     SELECT
        b.id, 
        b.name,
        b.description,
        b.collection_id AS collectionId,
        b.last_modified AS lastModified,
        ROUND(SUM(i.value), 2) AS value, /* total sum of item values in given box */
        MAX(CASE WHEN i.is_fragile THEN 1 ELSE 0 END) AS isFragile,
        COUNT(i.id) AS itemCount
    FROM boxes b
    LEFT JOIN items i ON b.id = i.box_id
    WHERE b.id = :id
    GROUP BY b.id
    ORDER BY b.last_modified ASC;       
    """)
    override fun get(id: String): Box?

    /**
     * Observes a box from the database by its unique ID.
     *
     * This function sets up a flow to observe changes to a [Box] in the 'boxes' table
     * based on the provided ID. It emits the box or null if no box with the given ID
     * is found.
     *
     * @param id The ID of the box to observe.
     * @return A [Flow] emitting the [Box] with the given ID, or null if not found.
     */
    @Query("""
     SELECT
        b.id, 
        b.name,
        b.description,
        b.collection_id AS collectionId,
        b.last_modified AS lastModified,
        ROUND(SUM(i.value), 2) AS value, /* total sum of item values in given box */
        MAX(CASE WHEN i.is_fragile THEN 1 ELSE 0 END) AS isFragile,
        COUNT(i.id) AS itemCount
    FROM boxes b
    LEFT JOIN items i ON b.id = i.box_id
    WHERE b.id = :id
    GROUP BY b.id
    ORDER BY b.last_modified ASC;       
    """)
    override fun observe(id: String): Flow<Box?>

    /**
     * Observes all boxes from the database, ordered by last modified timestamp in ascending order.
     *
     * This function sets up a flow to observe changes to all [Box] objects in the 'boxes'
     * table. It emits a list of all boxes, or an empty list if no boxes are found.
     * The boxes are ordered by their last modified timestamp in ascending order.
     *
     * @return A [Flow] emitting a list of all [Box] objects, or an empty list if none are found.
     */
    @Query("""
    SELECT
        b.id, 
        b.name,
        b.description,
        b.last_modified AS lastModified,
        b.collection_id AS collectionId,
        ROUND(SUM(i.value), 2) AS value,
        MAX(CASE WHEN i.is_fragile THEN 1 ELSE 0 END) AS isFragile,
        COUNT(i.id) AS itemCount
    FROM boxes b    
    LEFT JOIN items i ON b.id = i.box_id    
    GROUP BY b.id
    ORDER BY b.last_modified ASC;
    """)
    override fun observeAll(): Flow<List<Box?>>

    /**
     * Deletes all boxes from the database.
     *
     * This function removes all records from the 'boxes' table.
     */
    @Query("DELETE FROM boxes")
    override suspend fun clear()

    /**
     * Deletes boxes from the database based on their unique IDs.
     *
     * This function removes records from the 'boxes' table where the ID matches
     * one of the IDs in the provided list.
     *
     * @param ids A list of IDs of the boxes to delete.
     */
    @Query("DELETE FROM boxes WHERE id IN (:ids)")
    override suspend fun delete(ids: List<String>)

    /**
     * Data class representing a collection's ID and name.
     *
     * @property id The unique identifier of the collection.
     * @property name The name of the collection.
     */
    @Query("SELECT id, name FROM collections")
    fun listOfCollectionIdsAndNames(): Flow<List<CollectionIdAndName?>>
}
