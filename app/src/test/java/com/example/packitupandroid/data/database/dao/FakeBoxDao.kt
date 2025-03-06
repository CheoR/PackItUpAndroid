package com.example.packitupandroid.data.database.dao

import com.example.packitupandroid.data.database.entities.BoxEntity
import com.example.packitupandroid.data.model.Box
import com.example.packitupandroid.data.model.CollectionIdAndName
import com.example.packitupandroid.data.repository.toBox
import com.example.packitupandroid.source.local.LocalDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update


class FakeBoxDao : BoxDao {
    private val _elements = MutableStateFlow<List<BoxEntity>>(emptyList())
    private val elements: Flow<List<BoxEntity>> = _elements
    private var shouldReturnError = false
    private val dataSource = LocalDataSource()
    private val collections = dataSource.loadCollections()
    private var collectionIdsAndNamesToReturn: List<CollectionIdAndName?>? = collections.map { CollectionIdAndName(it.id, it.name) }
//
//    fun setShouldReturnError(value: Boolean) {
//        shouldReturnError = value
//    }
//
//    fun setBoxToReturn(box: Box?) {
//        boxToReturn = box
//    }
//
//    fun setCollectionIdsAndNamesToReturn(collectionIdsAndNames: List<CollectionIdAndName?>?) {
//        collectionIdsAndNamesToReturn = collectionIdsAndNames
//    }

    override fun get(id: String): Box? {
        return _elements.value.firstOrNull { it.id == id }?.toBox()
    }

    override fun observe(id: String): Flow<Box?> {
        return elements.map { list -> list.firstOrNull { it.id == id }?.toBox() }
    }

    override fun observeAll(): Flow<List<Box?>> {
        return elements.map { list -> list.map { it.toBox() } }
    }

    override suspend fun clear() {
        _elements.value = emptyList()
    }

    override suspend fun delete(ids: List<String>) {
        _elements.update { elements ->
            elements.filterNot { it.id in ids }
        }
    }

    override fun listOfCollectionIdsAndNames(): Flow<List<CollectionIdAndName?>> {
        if(shouldReturnError) {
            return flowOf(listOf(null))
        }
        return flowOf(collectionIdsAndNamesToReturn ?: emptyList())
    }

    override suspend fun insert(entities: List<BoxEntity>) {
        _elements.update {
            it.plus(entities)
        }
    }

    override suspend fun update(entity: BoxEntity) {
        val existingIndex = _elements.value.indexOfFirst { it.id == entity.id }
        if (existingIndex != -1) {
            _elements.update {
                val updated = it.toMutableList()
                updated[existingIndex] = entity
                updated
            }
        }
    }
}
