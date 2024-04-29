package com.example.packitupandroid.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.example.packitupandroid.data.model.QuerySummary
import kotlinx.coroutines.flow.Flow

@Dao
interface SummaryDao {
    @Query("""
        WITH boxesData AS (
            SELECT COUNT(b.id) AS count
            FROM boxes b
        ),
        
        collectionsData AS (
            SELECT COUNT(c.id) as count
            FROM collections c
        ),
        
        itemsData AS (
            SELECT COUNT(i.id) as count, ROUND(SUM(i.value)) AS value, MAX(CASE WHEN i.is_fragile = 1 THEN 1 ELSE 0 END) AS is_fragile
            FROM items i
        )
        
        SELECT
            boxesData.count AS box_count,
            collectionsData.count AS collection_count,
            itemsData.count AS item_count,
            itemsData.value AS value,
            itemsData.is_fragile AS is_fragile
        FROM boxesData, collectionsData, itemsData
    """)
    fun getSummary(): Flow<QuerySummary>

    @Transaction
    suspend fun clearAllSummary() {
        deleteItems()
        deleteBoxes()
        deleteCollections()
    }

    @Query("DELETE FROM items")
    fun deleteItems()

    @Query("DELETE FROM boxes")
    fun deleteBoxes()

    @Query("DELETE FROM collections")
    fun deleteCollections()
}
