package com.example.packitupandroid.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.packitupandroid.data.database.entities.CollectionEntity
import com.example.packitupandroid.data.model.QueryCollection
import kotlinx.coroutines.flow.Flow

@Dao
interface CollectionDao {
    @Query("SELECT * FROM collections WHERE id = :id")
    fun getCollection(id: String): Flow<CollectionEntity>

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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(collection: CollectionEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(collections: List<CollectionEntity>)

    @Update
    suspend fun update(collection: CollectionEntity)

    @Delete
    suspend fun delete(collection: CollectionEntity)

    @Delete
    suspend fun deleteAll(collections: List<CollectionEntity>)

    @Query("DELETE FROM collections")
    suspend fun clearAllCollections()
}
