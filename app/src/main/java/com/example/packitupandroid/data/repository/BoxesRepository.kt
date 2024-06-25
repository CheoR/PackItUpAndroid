package com.example.packitupandroid.data.repository

import com.example.packitupandroid.data.database.entities.BoxEntity
import com.example.packitupandroid.data.model.QueryBox
import com.example.packitupandroid.data.model.QueryDropdownOptions
import kotlinx.coroutines.flow.Flow

interface BoxesRepository {
    /**
     * Retrieve a box from the given data source that matches with the [id].
     */
    suspend fun getBox(id: String): BoxEntity?

    /**
     * Retrieve a QueryBox from the given data source that matches with the [id].
     */
    suspend fun getQueryBox(id: String): QueryBox?

    /**
     * Retrieve all the boxes from the given data source.
     */
    fun getAllBoxesStream(): Flow<List<QueryBox>>

    /**
     * Retrieve just @name and @id from all boxes from given data source.
     */
    suspend fun getDropdownSelections(): Flow<List<QueryDropdownOptions>>

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
