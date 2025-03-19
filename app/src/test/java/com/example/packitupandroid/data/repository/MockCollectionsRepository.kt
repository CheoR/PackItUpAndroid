package com.example.packitupandroid.data.repository

import com.example.packitupandroid.data.model.Collection
import com.example.packitupandroid.utils.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf


class MockCollectionsRepository(
    private val itemsRepository: MockItemsRepository,
    private val boxesRepository: MockBoxesRepository,
) : CollectionsRepository {
    private val _elements = MutableStateFlow<Result<List<Collection?>>>(Result.Loading)
    val elements: StateFlow<Result<List<Collection?>>> = _elements.asStateFlow()

    suspend fun load(data: List<Collection>) : Result<Unit> {
        insert(data)
        return Result.Success(Unit)
    }

    override suspend fun get(id: String): Result<Collection?> {
        return when (val result = _elements.value) {
            is Result.Success -> {
                val element = result.data.firstOrNull { it?.id == id }
                if (element != null) {
                    Result.Success(element)
                } else {
                    Result.Error(Exception("Collection with id $id not found"))
                }
            }
            else -> Result.Success(null)
        }
    }

    override fun observeAll(): Flow<Result<List<Collection?>>> {
        return elements
    }

    override fun observe(id: String): Flow<Result<Collection?>> {
        val element = when(val result = _elements.value) {
            is Result.Success -> {
                val collection = result.data.firstOrNull { it?.id == id }
                if (collection != null) {
                    Result.Success(collection)
                } else {
                    Result.Error(Exception("Collection with id $id not found"))
                }
            }
            else -> Result.Success(null)
        }
        return flowOf(element)
    }

    override suspend fun insert(data: List<Collection>): Result<Unit> {
        val currData = (_elements.value as? Result.Success)?.data ?: emptyList()
        val updatedData = currData + data

        _elements.value = Result.Success(updatedData)
        return Result.Success(Unit)
    }

    override suspend fun update(data: Collection): Result<Unit> {
        val currData = (_elements.value as? Result.Success)?.data ?: emptyList()
        val updatedData = currData.map { if (it?.id == data.id) data else it }
        _elements.value = Result.Success(updatedData)

        return Result.Success(Unit)
    }

    override suspend fun delete(data: List<String>): Result<Unit> {
        val currData = (_elements.value as? Result.Success)?.data ?: emptyList()
        val updatedData = currData.filterNot { data.contains(it?.id) }
        _elements.value = Result.Success(updatedData)
        return Result.Success(Unit)
    }

    override suspend fun clear(): Result<Unit> {
        _elements.value = Result.Success(emptyList())
        return Result.Success(Unit)
    }

    suspend fun cascadeDelete(ids: List<String>) {
        val boxResult = boxesRepository.observeAll().first()
        val currBoxes = (boxResult as? Result.Success)?.data ?: emptyList()
        val boxesToDelete = currBoxes.filter { it?.collectionId in ids }.map { it?.id }

        val itemResult = itemsRepository.observeAll().first()
        val currItems = (itemResult as? Result.Success)?.data ?: emptyList()
        val itemsToDelete = currItems.filter { it?.boxId in boxesToDelete }.map { it?.id }

        itemsRepository.delete(itemsToDelete.filterNotNull())
        boxesRepository.delete(boxesToDelete.filterNotNull())
    }
}
