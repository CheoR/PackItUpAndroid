package com.example.packitupandroid.data.repository

import com.example.packitupandroid.data.model.BoxIdAndName
import com.example.packitupandroid.data.model.Item
import com.example.packitupandroid.utils.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOf


class MockItemsRepository() : ItemsRepository {
    private val _elements = MutableStateFlow<Result<List<Item?>>>(Result.Loading)
    val elements: StateFlow<Result<List<Item?>>> = _elements.asStateFlow()
    val dropdownOptions = mutableListOf<BoxIdAndName?>()
//    private var currentBoxId: String? = null

    suspend fun load(data: List<Item>) : Result<Unit> {
        insert(data)
        return Result.Success(Unit)
    }
//
//    fun setDropdownOptions(options: List<BoxIdAndName?>) { dropdownOptions.addAll(options) }
//    fun unsetDropdownOptions() { dropdownOptions.clear() }

    override fun listOfBoxIdsAndNames(): Flow<Result<List<BoxIdAndName?>>> {
        return flowOf(Result.Success(dropdownOptions))
    }

    override suspend fun get(id: String): Result<Item?> {
        return when (val result = _elements.value) {
            is Result.Success -> {
                val element = result.data.firstOrNull { it?.id == id }
                if (element != null) {
                    Result.Success(element)
                } else {
                    Result.Error(Exception("Item with id $id not found"))
                }
            }
            else -> Result.Success(null)

        }
    }

    override fun observeAll(): Flow<Result<List<Item?>>> {
        return elements
    }

    override fun observe(id: String): Flow<Result<Item?>> {
        val element = when(val result = _elements.value) {
            is Result.Success -> {
                val item = result.data.firstOrNull { it?.id == id }
                if (item != null) {
                    Result.Success(item)
                } else {
                    Result.Error(Exception("Item with id $id not found"))
                }
            }
            else -> Result.Success(null)
        }
        return flowOf(element)
    }

    override suspend fun insert(data: List<Item>): Result<Unit> {
        val currData = (_elements.value as? Result.Success)?.data ?: emptyList()
        val updatedData = currData + data

        _elements.value = Result.Success(updatedData)
        return Result.Success(Unit)
    }

    override suspend fun update(data: Item): Result<Unit> {
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
}