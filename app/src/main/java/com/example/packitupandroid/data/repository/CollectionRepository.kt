package com.example.packitupandroid.data.repository

import com.example.packitupandroid.data.model.Collection


/**
 * Repository that provides insert, update, delete, and retrieve operations for [Collection] entities.
 *
 * This interface extends [BaseRepository] and provides specific data access
 * functionalities for [Collection] entities. It includes operations for inserting,
 * updating, deleting, and retrieving [Collection] data from a given data source.
 */
interface CollectionsRepository : BaseRepository<Collection>
