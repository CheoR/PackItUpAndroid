package com.example.packitupandroid.data.repository

import com.example.packitupandroid.data.model.Collection
import com.example.packitupandroid.data.model.QueryCollection
import com.example.packitupandroid.data.model.QueryDropdownOptions
import kotlinx.coroutines.flow.Flow


/**
 * Repository that provides insert, update, delete, and retrieve operations for [Collection] entities.
 *
 * This interface extends [BaseRepository] and provides specific data access
 * functionalities for [Collection] entities. It includes operations for inserting,
 * updating, deleting, and retrieving [Collection] data from a given data source.
 */
interface CollectionsRepository : BaseRepository<Collection> {

    /**
     * Retrieve a QueryCollection from the given data source that matches with the [id].
     */
    suspend fun getQueryCollection(id: String): QueryCollection?

    /**
     * Retrieve just @name and @id from all boxes from given data source.
     */
    suspend fun getDropdownSelections(): Flow<List<QueryDropdownOptions>>
}
