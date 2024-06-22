package com.example.packitupandroid.data.repository

import com.example.packitupandroid.data.database.dao.CollectionDao
import com.example.packitupandroid.data.database.entities.CollectionEntity
import com.example.packitupandroid.data.model.QueryCollection
import com.example.packitupandroid.data.model.QueryDropdownOptions
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

class OfflineCollectionsRepository(private val collectionDao: CollectionDao) : CollectionsRepository {
    override suspend fun getCollection(id: String): CollectionEntity? = collectionDao.getCollection(id).firstOrNull()
    override suspend fun getQueryCollection(id: String): QueryCollection? = collectionDao.getQueryCollection(id).firstOrNull()
    override fun getAllCollectionsStream(): Flow<List<QueryCollection>> = collectionDao.getAllCollections()
    override fun getCollectionStream(id: String): Flow<CollectionEntity?> = collectionDao.getCollection(id)
    override suspend fun getDropdownSelections(): Flow<List<QueryDropdownOptions>> = collectionDao.getDropdownSelections()
    override suspend fun insertCollection(collection: CollectionEntity) = collectionDao.insert(collection)
    override suspend fun insertAll(collections: List<CollectionEntity>) = collectionDao.insertAll(collections)
    override suspend fun updateCollection(collection: CollectionEntity) = collectionDao.update(collection)
    override suspend fun deleteCollection(collection: CollectionEntity) = collectionDao.delete(collection)
    override suspend fun deleteAll(collections: List<CollectionEntity>) = collectionDao.deleteAll(collections)
    override suspend fun clearAllCollections() = collectionDao.clearAllCollections()
}
