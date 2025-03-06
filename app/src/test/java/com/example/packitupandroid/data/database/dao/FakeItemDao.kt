package com.example.packitupandroid.data.database.dao

import com.example.packitupandroid.data.database.entities.ItemEntity
import com.example.packitupandroid.data.model.BoxIdAndName
import com.example.packitupandroid.data.model.Item
import com.example.packitupandroid.data.repository.toItem
import com.example.packitupandroid.source.local.LocalDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update


class FakeItemDao : ItemDao {
    private val _elements = MutableStateFlow<List<ItemEntity>>(emptyList())
    private val elements: Flow<List<ItemEntity>> = _elements.asStateFlow()
    private var shouldReturnError = false
//    private var itemToReturn: Item? = null
    private val dataSource = LocalDataSource()
    private val boxes = dataSource.loadBoxes()
    private var boxIdsAndNamesToReturn: List<BoxIdAndName?>? = boxes.map { BoxIdAndName(it.id, it.name) }
//
//    fun setShouldReturnError(value: Boolean) {
//        shouldReturnError = value
//    }
//
//    fun setItemToReturn(item: Item?) {
//        itemToReturn = item
//    }
//
//    fun setBoxIdsAndNamesToReturn(boxIdsAndNames: List<BoxIdAndName?>?) {
//        boxIdsAndNamesToReturn = boxIdsAndNames
//    }

    override suspend fun insert(entities: List<ItemEntity>) {
        _elements.update {
            it.plus(entities)
        }
    }

    override suspend fun update(entity: ItemEntity) {
        val existingIndex = _elements.value.indexOfFirst { it.id == entity.id }
        if (existingIndex != -1) {
            _elements.update {
                val updated = it.toMutableList()
                updated[existingIndex] = entity
                updated
            }
        }
    }

    override fun get(id: String): Item? {
        return _elements.value.firstOrNull { it.id == id }?.toItem()
    }

    override fun observe(id: String): Flow<Item?> {
        return elements.map { list -> list.firstOrNull { it.id == id }?.toItem() }
    }

    override fun observeAll(): Flow<List<Item?>> {
        return elements.map { list -> list.map { it.toItem() } }
    }

    override suspend fun clear() {
       _elements.value = emptyList()
    }

    override suspend fun delete(ids: List<String>) {
        _elements.update { elements ->
            elements.filterNot { it.id in ids }
        }
    }

    override fun listOfBoxIdsAndNames(): Flow<List<BoxIdAndName?>> {
        if (shouldReturnError) {
            return flowOf(listOf(null))
        }

        return flowOf(boxIdsAndNamesToReturn ?: emptyList())
    }
}
