package com.example.packitupandroid.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.packitupandroid.data.database.entities.ItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemDao {

    // recommended to use Flow in persistence layer
    @Query("SELECT * FROM items WHERE id = :id")
    fun getItem(id: String): Flow<ItemEntity>

    @Query("SELECT * from items ORDER BY last_modified ASC")
    fun getAllItems(): Flow<List<ItemEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: ItemEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<ItemEntity>)

    @Update
    suspend fun update(item: ItemEntity)

    @Delete
    suspend fun delete(item: ItemEntity)

    @Delete
    suspend fun deleteAll(items: List<ItemEntity>)

    @Query("DELETE FROM items")
    suspend fun clearAllItems()
}
