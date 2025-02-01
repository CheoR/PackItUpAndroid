package com.example.packitupandroid.data.repository

import com.example.packitupandroid.data.model.Box
import com.example.packitupandroid.data.model.CollectionIdAndName
import com.example.packitupandroid.utils.Result
import kotlinx.coroutines.flow.Flow


/**
 * Repository that provides insert, update, delete, and retrieve operations for [Box] entities.
 *
 * This interface extends [BaseRepository] and provides specific data access
 * functionalities for [Box] entities. It includes operations for inserting,
 * updating, deleting, and retrieving [Box] data from a given data source.
 */
interface BoxesRepository : BaseRepository<Box> {
    fun listOfCollectionIdsAndNames(): Flow<Result<List<CollectionIdAndName?>>>
}
