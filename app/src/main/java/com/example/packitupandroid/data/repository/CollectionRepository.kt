package com.example.packitupandroid.data.repository

import com.example.packitupandroid.data.database.entities.CollectionEntity
import com.example.packitupandroid.data.model.QueryCollection
import com.example.packitupandroid.data.model.QueryDropdownOptions
import kotlinx.coroutines.flow.Flow

interface CollectionsRepository {
    /**
     * Retrieve a Collection from the given data source that matches with the [id].
     */
    suspend fun getCollection(id: String): CollectionEntity?

    /**
     * Retrieve a QueryCollection from the given data source that matches with the [id].
     */
    suspend fun getQueryCollection(id: String): QueryCollection?

    /**
     * Retrieve all the Collections from the given data source.
     */
    fun getAllCollectionsStream(): Flow<List<QueryCollection>>

    /**
     * Retrieve a Collection from the given data source that matches with the [id].
     */
    fun getCollectionStream(id: String): Flow<CollectionEntity?>

    /**
     * Retrieve just @name and @id from all boxes from given data source.
     */
    suspend fun getDropdownSelections(): Flow<List<QueryDropdownOptions>>

    /**
     * Insert Collection in the data source
     */
    suspend fun insertCollection(collection: CollectionEntity)

    /**
     * Insert more than one Collection in the data source
     */
    suspend fun insertAll(collections: List<CollectionEntity>)

    /**
     * Update Collection in the data source
     */
    suspend fun updateCollection(collection: CollectionEntity)

    /**
     * Delete Collection from the data source
     */
    suspend fun deleteCollection(collection: CollectionEntity)

    /**
     * Delete more than one item from the data source
     */
    suspend fun deleteAll(collections: List<CollectionEntity>)

    /**
     * Clear all Collections in the data source
     */
    suspend fun clearAllCollections()
}
