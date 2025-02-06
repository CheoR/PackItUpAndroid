package com.example.packitupandroid.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.packitupandroid.data.model.Summary
import kotlinx.coroutines.flow.Flow


/**
 * Data Access Object (DAO) for the Summary table.
 *
 * This interface provides a method to get a stream of Summary objects.
 */
@Dao
interface SummaryDao {
    /**
     * Queries the database to get a stream of Summary objects.
     *
     * The query uses subqueries to calculate the total number of items, boxes, collections, the total value of items,
     * and whether there are any fragile items.
     *
     * @return A Flow of Summary objects.
     */
    @Query("""
        SELECT 
            (SELECT COUNT(*) FROM items) AS itemCount,
            (SELECT COUNT(*) FROM boxes) AS boxCount,
            (SELECT COUNT(*) FROM collections) AS collectionCount,
            (SELECT SUM(value) FROM items) AS value,
            (SELECT EXISTS(SELECT 1 FROM items WHERE is_fragile = 1)) AS isFragile
    """)
    fun observe(): Flow<Summary>

    /**
     * Retrieves the name of a box from the database based on its ID.
     *
     * @param id The unique identifier of the collection.
     * @return The name of the collection associated with the given ID.
     * @throws SQLiteException if there is an error executing the SQL query.
     * @throws EmptyResultDataAccessException if no collection with the provided ID is found.
     * @throws IllegalStateException if there is an issue with the database connection.
     *         SELECT name
     */
    @Query("""
        SELECT name
        FROM collections 
        WHERE id = :id
    """)
    fun getCollectionName(id: String) : String

    /**
     * Retrieves the name of a box from the database based on its ID.
     *
     * @param id The unique identifier of the box.
     * @return The name of the box associated with the given ID.
     * @throws SQLiteException if there is an error executing the SQL query.
     * @throws EmptyResultDataAccessException if no box with the provided ID is found.
     * @throws IllegalStateException if there is an issue with the database connection.
     */
    @Query("""
        SELECT name
        FROM boxes 
        WHERE id = :id
    """)
    fun getBoxName(id: String) : String

    /**
     * Retrieves the name of a item from the database based on its ID.
     *
     * @param id The unique identifier of the item.
     * @return The name of the item associated with the given ID.
     * @throws SQLiteException if there is an error executing the SQL query.
     * @throws EmptyResultDataAccessException if no item with the provided ID is found.
     * @throws IllegalStateException if there is an issue with the database connection.
     */
    @Query("""
        SELECT name
        FROM items 
        WHERE id = :id
    """)
    fun getItemName(id: String) : String
}
