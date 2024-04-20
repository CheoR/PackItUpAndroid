package com.example.packitupandroid.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.packitupandroid.data.model.QuerySummary
import kotlinx.coroutines.flow.Flow

@Dao
interface SummaryDao {
    @Query("""
    WITH BoxSummary AS (
        SELECT 
            b.id,
            b.last_modified,
            b.collection_id,
            SUM(i.value) AS value,
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
        COUNT(DISTINCT c.id) AS collection_count,
        ROUND(SUM(bs.value), 2) AS value,
        MAX(CASE WHEN bs.is_fragile = 1 THEN 1 ELSE 0 END) AS is_fragile,
        COUNT( bs.id) AS box_count,
        SUM(bs.item_count) AS item_count
    FROM 
        collections c
    LEFT JOIN 
        BoxSummary bs ON c.id = bs.collection_id;
    """)
    fun getSummary(): Flow<QuerySummary>
}
