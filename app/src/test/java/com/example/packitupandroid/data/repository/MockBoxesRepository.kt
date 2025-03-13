package com.example.packitupandroid.data.repository

import com.example.packitupandroid.data.model.Box
import com.example.packitupandroid.data.model.CollectionIdAndName
import com.example.packitupandroid.utils.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlin.collections.contains


class MockBoxesRepository(
    private val itemsRepository: MockItemsRepository,
): BoxesRepository {
    private val _elements = MutableStateFlow<Result<List<Box?>>>(Result.Loading)
    val elements: StateFlow<Result<List<Box?>>> = _elements.asStateFlow()
    val dropdownOptions = mutableListOf<CollectionIdAndName?>()

    suspend fun load(data: List<Box>) : Result<Unit> {
        insert(data)
        return Result.Success(Unit)
    }

    override fun listOfCollectionIdsAndNames(): Flow<Result<List<CollectionIdAndName?>>> {
        return flowOf(Result.Success(dropdownOptions))
    }

    override suspend fun get(id: String): Result<Box?> {
        return when (val result = _elements.value) {
            is Result.Success -> {
                val element = result.data.firstOrNull { it?.id == id }
                if (element != null) {
                    Result.Success(element)
                } else {
                    Result.Error(Exception("Box with id $id not found"))
                }
            }
            else -> Result.Success(null)
        }
    }

    override fun observeAll(): Flow<Result<List<Box?>>> {
        return elements
    }

    override fun observe(id: String): Flow<Result<Box?>> {
        val element = when(val result = _elements.value) {
            is Result.Success -> {
                val box = result.data.firstOrNull { it?.id == id }
                if (box != null) {
                    Result.Success(box)
                } else {
                    Result.Error(Exception("Box with id $id not found"))
                }
            }
            else -> Result.Success(null)
        }
        return flowOf(element)
    }

    override suspend fun insert(data: List<Box>): Result<Unit> {
        val currData = (_elements.value as? Result.Success)?.data ?: emptyList()
        val updatedData = currData + data

        _elements.value = Result.Success(updatedData)
        return Result.Success(Unit)
    }

    override suspend fun update(data: Box): Result<Unit> {
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
        val result = itemsRepository.observeAll().first()
        val currItems = (result as? Result.Success)?.data ?: emptyList()
        val itemsToDelete = currItems.filter { it?.boxId in ids }.map { it?.id }

        itemsRepository.delete(itemsToDelete.filterNotNull())
    }
}
