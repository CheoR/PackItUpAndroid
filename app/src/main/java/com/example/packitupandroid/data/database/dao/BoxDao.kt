package com.example.packitupandroid.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.packitupandroid.data.database.entities.BoxEntity
import com.example.packitupandroid.data.model.QueryBox
import com.example.packitupandroid.data.model.QueryDropdownOptions
import kotlinx.coroutines.flow.Flow

@Dao
interface BoxDao {
    @Query("SELECT * FROM boxes WHERE id = :id")
    fun getBox(id: String): Flow<BoxEntity>

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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(box: BoxEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(boxes: List<BoxEntity>)

    @Update
    suspend fun update(box: BoxEntity)

    @Delete
    suspend fun delete(box: BoxEntity)

    @Delete
    suspend fun deleteAll(boxes: List<BoxEntity>)

    @Query("DELETE FROM boxes")
    suspend fun clearAllBoxes()
}
