package com.example.packitupandroid.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.packitupandroid.data.database.entities.BoxEntity
import com.example.packitupandroid.data.model.Box
import com.example.packitupandroid.data.model.QueryBox
import com.example.packitupandroid.data.model.QueryDropdownOptions
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
     * Data class representing a box with aggregated item data.
     *
     * @property id The unique identifier of the box.
     * @property name The name of the box.
     * @property description The description of the box.
     * @property last_modified The timestamp of the last modification to the box.
     * @property collection_id The id of the collection to which this box belongs.
     * @property value The sum of the values of all items in the box, rounded to 2 decimal places.
     * @property is_fragile A flag indicating if any item in the box is fragile (1 if true, 0 otherwise).
     * @property item_count The total number of items in the box.
     */
    @Query("""
     SELECT
        b.id, 
        b.name,
        b.description,
        b.last_modified as last_modified,
        b.collection_id,
        ROUND(SUM(i.value), 2) AS value,
        MAX(CASE WHEN i.is_fragile THEN 1 ELSE 0 END) AS is_fragile,
        COUNT(i.id) AS item_count
    FROM boxes b
    LEFT JOIN items i ON b.id = i.box_id
    WHERE b.id = :id
    GROUP BY b.id
    ORDER BY last_modified ASC;       
    """)
    fun getQueryBox(id: String): Flow<QueryBox>

    /**
     * Data class representing a box with aggregated item information.
     *
     * @property id The unique identifier of the box.
     * @property name The name of the box.
     * @property description A description of the box.
     * @property last_modified The timestamp of the last modification of the box.
     * @property collection_id The ID of the collection the box belongs to.
     * @property value The sum of the values of all items in the box, rounded to 2 decimal places.
     * @property is_fragile A flag indicating if any item in the box is fragile (1 if true, 0 if false).
     * @property item_count The number of items in the box.
     */
    @Query("""
    SELECT
        b.id, 
        b.name,
        b.description,
        b.last_modified as last_modified,
        b.collection_id,
        ROUND(SUM(i.value), 2) AS value,
        MAX(CASE WHEN i.is_fragile THEN 1 ELSE 0 END) AS is_fragile,
        COUNT(i.id) AS item_count
    FROM boxes b    
    LEFT JOIN items i ON b.id = i.box_id    
    GROUP BY b.id
    ORDER BY last_modified ASC;
    """)
    fun getAllQueryBoxes(): Flow<List<QueryBox>>

    /**
     * Data class representing the options for a dropdown selection.
     *
     * @property id The unique identifier of the option.
     * @property name The display name of the option.
     */
    @Query("SELECT b.id, b.name FROM boxes b")
    fun getDropdownSelections(): Flow<List<QueryDropdownOptions>>

    /**
     * Retrieves all boxes from the database, ordered by last modified timestamp in ascending order.
     *
     * @return A [Flow] emitting a list of all [Box] objects, or an empty list if none are found.
     */
    @Query("SELECT * FROM boxes ORDER BY last_modified ASC")
    fun getAll(): Flow<List<Box?>>

}
