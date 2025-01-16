package com.example.packitupandroid.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.packitupandroid.data.database.entities.CollectionEntity
import com.example.packitupandroid.data.model.QueryCollection
import com.example.packitupandroid.data.model.QueryDropdownOptions
import kotlinx.coroutines.flow.Flow
import com.example.packitupandroid.data.model.Collection


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
     * Retrieves detailed information about a specific collection, including aggregated data from its boxes and items.
     *
     * This query performs the following operations:
     * 1. **Calculates Box Summary (BoxSummary CTE):**
     *    - For each box, it calculates the sum of item values (`value`).
     *    - Determines if any item in the box is fragile (`is_fragile` - 1 if any item is fragile, 0 otherwise).
     *    - Counts the number of items in each box (`item_count`).
     * 2. **Joins with Collections:**
     *    - Joins the `BoxSummary` with the `collections` table to associate box summaries with their respective collections.
     * 3. **Aggregates Collection Data:**
     *    - For the specified collection, it calculates the total value of all items in all its boxes (`value`).
     *    - Determines if any item in any of the collection's boxes is fragile (`is_fragile`).
     *    - Counts the number of boxes in the collection (`box_count`).
     *    - Sums the total item count across all boxes in the collection (`item_count`).
     * 4. **Filters by Collection ID:**
     *    - Filters the results to only include the collection with the provided `id`.
     * 5. **Groups by Collection ID:**
     *    - Groups the results by collection ID to aggregate data for each collection.
     * 6. **Orders by Last Modified:**
     *    - Orders the results by the `last_modified` timestamp of the collection.
     *
     * @param id The unique identifier of the collection to retrieve.
     * @return A Flow emitting a single [QueryCollection] object containing the aggregated data for the specified collection.
     *         The Flow will emit any time the data in the database that affects this query changes.
     *
     * The [QueryCollection] object will contain:
     * - `id`: The ID of the collection.
     * - `name`: The name of the collection.
     * - `description`: The description of the collection.
     * - `last_modified`: The last */
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

    /**
     * Represents a collection with aggregated data from associated boxes and items.
     *
     * @property id The unique identifier of the collection.
     * @property name The name of the collection.
     * @property description A description of the collection.
     * @property last_modified The timestamp of the last modification of the collection.
     * @property value The total value of all items in all boxes within the collection.
     * @property is_fragile Indicates if any item in any box within the collection is fragile (1 if yes, 0 if no).
     * @property box_count The total number of boxes within the collection.
     * @property item_count The total number of items in all boxes within the collection.
     */
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
    fun getAllQueryCollections(): Flow<List<QueryCollection>>

    /**
     * Represents the options for a dropdown selection, containing an ID and a name.
     *
     * @property id The unique identifier of the option.
     * @property name The display name of the option.
     */
    @Query("SELECT c.id, c.name FROM collections c")
    fun getDropdownSelections(): Flow<List<QueryDropdownOptions>>
}
