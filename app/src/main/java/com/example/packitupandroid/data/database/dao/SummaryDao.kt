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
}
