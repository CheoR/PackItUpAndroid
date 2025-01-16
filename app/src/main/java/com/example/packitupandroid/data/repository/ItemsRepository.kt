package com.example.packitupandroid.data.repository

import com.example.packitupandroid.data.model.Item


/**
 * Repository that provides insert, update, delete, and retrieve operations for [Item] entities.
 *
 * This interface extends [BaseRepository] and provides specific data access
 * functionalities for [Item] entities. It includes operations for inserting,
 * updating, deleting, and retrieving [Item] data from a given data source.
 */
interface ItemsRepository : BaseRepository<Item>
