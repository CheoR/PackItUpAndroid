package com.example.packitupandroid.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.packitupandroid.data.database.entities.BoxEntity
import com.example.packitupandroid.data.model.QueryBox
import com.example.packitupandroid.data.model.QueryDropdownOptions
import kotlinx.coroutines.flow.Flow


/**
 * Data Access Object (DAO) for interacting with the 'boxes' table in the database.
 *
 * This interface provides methods for retrieving and deleting boxes.
 * It extends both [BaseDao] and [CommonDao] to inherit common data access operations.
 */
@Dao
interface BoxDao : BaseDao<BoxEntity>, CommonDao<BoxEntity> {

    /**
     * Retrieves a box from the database by its unique ID.
     *
     * @param id The ID of the box to retrieve.
     * @return A [Flow] emitting the [BoxEntity] with the given ID, or null if not found.
     */
    @Query("SELECT * FROM boxes WHERE id = :id")
    override fun get(id: String): Flow<BoxEntity?>

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
    fun getAllBoxes(): Flow<List<QueryBox>>

    @Query("SELECT b.id, b.name FROM boxes b")
    fun getDropdownSelections(): Flow<List<QueryDropdownOptions>>

    /**
     * Retrieves all boxes from the database, ordered by last modified timestamp in ascending order.
     *
     * @return A [Flow] emitting a list of all [BoxEntity] objects, or an empty list if none are found.
     */
    @Query("SELECT * FROM boxes ORDER BY last_modified ASC")
    override fun getAll(): Flow<List<BoxEntity?>>

    /**
     * Deletes all boxes from the database.
     */
    @Query("DELETE FROM boxes")
    override suspend fun clear()
}
