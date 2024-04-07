package com.example.packitupandroid.data.repository

import com.example.packitupandroid.data.database.dao.ItemDao
import com.example.packitupandroid.data.database.entities.ItemEntity
import com.example.packitupandroid.data.repository.ItemsRepository
import kotlinx.coroutines.flow.Flow

class OfflineItemsRepository(private val itemDao: ItemDao) : ItemsRepository {
    override fun getAllItemsStream(): Flow<List<ItemEntity>> = itemDao.getAllItems()

    override fun getItemStream(id: String): Flow<ItemEntity?> = itemDao.getItem(id)

    override suspend fun insertItem(item: ItemEntity) = itemDao.insert(item)

    override suspend fun insertAll(items: List<ItemEntity>) = itemDao.insertAll(items)

    override suspend fun deleteItem(item: ItemEntity) = itemDao.delete(item)

    override suspend fun deleteAll(items: List<ItemEntity>) = itemDao.deleteAll(items)

    override suspend fun updateItem(item: ItemEntity) = itemDao.update(item)
}