package com.example.packitupandroid.data.repository

import com.example.packitupandroid.data.model.Box
import com.example.packitupandroid.data.model.QueryBox
import com.example.packitupandroid.data.model.QueryDropdownOptions
import kotlinx.coroutines.flow.Flow


/**
 * Repository that provides insert, update, delete, and retrieve operations for [Box] entities.
 *
 * This interface extends [BaseRepository] and provides specific data access
 * functionalities for [Box] entities. It includes operations for inserting,
 * updating, deleting, and retrieving [Box] data from a given data source.
 */
interface BoxesRepository : BaseRepository<Box> {

    /**
     * Retrieve a QueryBox from the given data source that matches with the [id].
     */
    suspend fun getQueryBox(id: String): QueryBox?

    /**
     * Retrieve just @name and @id from all boxes from given data source.
     */
    suspend fun getDropdownSelections(): Flow<List<QueryDropdownOptions>>
}
