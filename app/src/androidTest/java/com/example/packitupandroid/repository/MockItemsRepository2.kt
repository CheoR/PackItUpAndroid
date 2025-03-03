package com.example.packitupandroid.repository

import com.example.packitupandroid.data.database.entities.ItemEntity
import com.example.packitupandroid.data.model.BoxIdAndName
import com.example.packitupandroid.data.model.Item
import com.example.packitupandroid.data.repository.ItemsRepository
import com.example.packitupandroid.data.repository.toEntity
import com.example.packitupandroid.data.repository.toItem
import com.example.packitupandroid.utils.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map


private val boxesOptions = listOf(
    BoxIdAndName("1", "Collection 1"),
    BoxIdAndName("2", "Collection 2"),
    BoxIdAndName("3", "Collection 3"),
    BoxIdAndName("4", "Collection 4")
)

class MockItemsRepository2 : ItemsRepository {
    private val items = mutableListOf<ItemEntity>()
    private val itemFlow = MutableStateFlow<List<ItemEntity>>(emptyList())
//    private val _uiState = MutableStateFlow(ItemsPackItUpUiState())
//    val uiState: StateFlow<ItemsPackItUpUiState> = _uiState.asStateFlow()

    override fun listOfBoxIdsAndNames(): Flow<Result<List<BoxIdAndName?>>> {
        return flowOf(Result.Success(boxesOptions))
    }

    override suspend fun get(id: String): Result<Item?> {
        return Result.Success(items.find { it.id == id }?.toItem())
    }

    override fun observeAll(): Flow<Result<List<Item?>>> {
        return itemFlow.map { listOfItemEntities -> Result.Success(listOfItemEntities.map { it.toItem() }) }
    }

    override fun observe(id: String): Flow<Result<Item?>> {
        TODO("Not yet implemented")
    }

    override suspend fun delete(data: List<String>): Result<Unit> {
        this.items.removeAll { data.contains(it.id) }
        itemFlow.value = this.items
        return Result.Success(Unit)
    }

    override suspend fun clear(): Result<Unit> {
        this.items.clear()
        itemFlow.value = this.items
        return Result.Success(Unit)
    }

    override suspend fun update(data: Item): Result<Unit> {
        val index = items.indexOfFirst { it.id == data.id }
        if (index != -1) {
            this.items[index] = data.toEntity()
            itemFlow.value = this.items
        }
        return Result.Success(Unit)
    }

    override suspend fun insert(data: List<Item>): Result<Unit> {
        this.items.addAll(data.map { it.toEntity() })
        itemFlow.value = this.items
        return Result.Success(Unit)
    }
}
