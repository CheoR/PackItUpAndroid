package com.example.packitupandroid.data.database.dao

import com.example.packitupandroid.data.database.entities.CollectionEntity
import com.example.packitupandroid.data.model.Collection
import com.example.packitupandroid.data.repository.toCollection
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update


class FakeCollectionDao(
    private val boxDao: FakeBoxDao,
    private val itemDao: FakeItemDao
) : CollectionDao {
    private val _elements = MutableStateFlow<List<CollectionEntity>>(emptyList())
    private val elements: Flow<List<CollectionEntity>> = _elements
//    private var shouldReturnError = false
//    private var collectionToReturn: Collection? = null
//    private val dataSource = LocalDataSource()
//    private var collectionIdsAndNamesToReturn: List<CollectionIdAndName?>? = dataSource.loadCollections().map { CollectionIdAndName(it.id, it.name) }

//
//    fun setShouldReturnError(value: Boolean) {
//        shouldReturnError = value
//    }
//
//    fun setCollectionToReturn(collection: Collection?) {
//        collectionToReturn = collection
//    }
//

    override fun get(id: String): Collection? {
        return _elements.value.firstOrNull { it.id == id }?.toCollection()
    }

    override fun observe(id: String): Flow<Collection?> {
        return elements.map { list -> list.firstOrNull { it.id == id }?.toCollection() }
    }

    override fun observeAll(): Flow<List<Collection?>> {
        return elements.map { list -> list.map { it.toCollection() } }
    }

    override suspend fun clear() {
        _elements.value = emptyList()
    }

    override suspend fun delete(ids: List<String>) {
        _elements.update { elements ->
            elements.filterNot { it.id in ids }
        }
        cascadeDelete(ids)
    }

    override suspend fun insert(entities: List<CollectionEntity>) {
        _elements.update {
            it.plus(entities)
        }
    }

    override suspend fun update(entity: CollectionEntity) {
        val existingIndex = _elements.value.indexOfFirst { it.id == entity.id }
        if (existingIndex != -1) {
            _elements.update {
                val updated = it.toMutableList()
                updated[existingIndex] = entity
                updated
            }
        }
    }

    suspend fun cascadeDelete(ids: List<String>) {
        val currBoxes = boxDao.observeAll().first()
        val boxesToDelete = currBoxes.filter { it?.collectionId in ids }.map { it?.id }

        val currItems = itemDao.observeAll().first()
        val itemsToDelete = currItems.filter { it?.boxId in boxesToDelete }.map { it?.id }

        itemDao.delete(itemsToDelete.filterNotNull())
        boxDao.delete(boxesToDelete.filterNotNull())
    }
}
