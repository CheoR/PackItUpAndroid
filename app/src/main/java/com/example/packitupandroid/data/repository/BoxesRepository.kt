package com.example.packitupandroid.data.repository

import com.example.packitupandroid.data.database.entities.BoxEntity
import com.example.packitupandroid.data.model.QueryBox
import kotlinx.coroutines.flow.Flow

interface BoxesRepository {
    /**
     * Retrieve a box from the given data source that matches with the [id].
     */
    suspend fun getBox(id: String): BoxEntity?

    /**
     * Retrieve all the boxes from the given data source.
     */
    fun getAllBoxesStream(): Flow<List<QueryBox>>

    /**
     * Retrieve a box from the given data source that matches with the [id].
     */
    fun getBoxStream(id: String): Flow<BoxEntity?>

    /**
     * Insert box in the data source
     */
    suspend fun insertBox(box: BoxEntity)

    /**
     * Insert more than one box in the data source
     */
    suspend fun insertAll(boxes: List<BoxEntity>)

    /**
     * Update box in the data source
     */
    suspend fun updateBox(box: BoxEntity)

    /**
     * Delete box from the data source
     */
    suspend fun deleteBox(box: BoxEntity)

    /**
     * Delete more than one item from the data source
     */
    suspend fun deleteAll(boxes: List<BoxEntity>)

    /**
     * Clear all boxes in the data source
     */
    suspend fun clearAllBoxes()
}
