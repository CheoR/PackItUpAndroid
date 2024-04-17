package com.example.packitupandroid.data.repository

import com.example.packitupandroid.data.database.dao.BoxDao
import com.example.packitupandroid.data.database.entities.BoxEntity
import com.example.packitupandroid.data.model.QueryBox
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

class OfflineBoxesRepository(private val boxDao: BoxDao) : BoxesRepository {
    override suspend fun getBox(id: String): BoxEntity? = boxDao.getBox(id).firstOrNull()
    override fun getAllBoxesStream(): Flow<List<QueryBox>> = boxDao.getAllBoxes()
    override fun getBoxStream(id: String): Flow<BoxEntity?> = boxDao.getBox(id)
    override suspend fun insertBox(box: BoxEntity) = boxDao.insert(box)
    override suspend fun insertAll(boxes: List<BoxEntity>) = boxDao.insertAll(boxes)

    override suspend fun updateBox(box: BoxEntity) = boxDao.update(box)
    override suspend fun deleteBox(box: BoxEntity) = boxDao.delete(box)
    override suspend fun deleteAll(boxes: List<BoxEntity>) = boxDao.deleteAll(boxes)

    override suspend fun clearAllBoxes() = boxDao.clearAllBoxes()
}
