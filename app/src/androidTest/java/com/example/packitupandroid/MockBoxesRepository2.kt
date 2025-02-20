package com.example.packitupandroid

import com.example.packitupandroid.data.database.entities.BoxEntity
import com.example.packitupandroid.data.model.Box
import com.example.packitupandroid.data.model.CollectionIdAndName
import com.example.packitupandroid.data.repository.BoxesRepository
import com.example.packitupandroid.data.repository.toBox
import com.example.packitupandroid.data.repository.toEntity
import com.example.packitupandroid.utils.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map


private val collectionsOptions = listOf(
    CollectionIdAndName("1", "Collection 1"),
    CollectionIdAndName("2", "Collection 2"),
    CollectionIdAndName("3", "Collection 3"),
    CollectionIdAndName("4", "Collection 4")
)

class MockBoxesRepository2: BoxesRepository {
    private val boxes = mutableListOf<BoxEntity>()
    private val boxFlow = MutableStateFlow<List<BoxEntity>>(emptyList())

    override fun listOfCollectionIdsAndNames(): Flow<Result<List<CollectionIdAndName?>>> {
        return flowOf(Result.Success(collectionsOptions))
    }

    override suspend fun get(id: String): Result<Box?> {
        return Result.Success(boxes.find { it.id == id }?.toBox())
    }

    override fun observeAll(): Flow<Result<List<Box?>>> {
        return boxFlow.map { listOfBoxEntities -> Result.Success(listOfBoxEntities.map { it.toBox() }) }
    }

    override fun observe(id: String): Flow<Result<Box?>> {
        TODO("Not yet implemented")
    }

    override suspend fun insert(data: List<Box>): Result<Unit> {
        this.boxes.addAll(data.map { it.toEntity() })
        boxFlow.value = this.boxes
        return Result.Success(Unit)
    }

    override suspend fun update(data: Box): Result<Unit> {
        val index = boxes.indexOfFirst { it.id == data.id }
        if (index != -1) {
            this.boxes[index] = data.toEntity()
            boxFlow.value = this.boxes
        }
        return Result.Success(Unit)
    }

    override suspend fun delete(data: List<String>): Result<Unit> {
        this.boxes.removeAll { data.contains(it.id) }
        boxFlow.value = this.boxes
        return Result.Success(Unit)
    }

    override suspend fun clear(): Result<Unit> {
        this.boxes.clear()
        boxFlow.value = this.boxes
        return Result.Success(Unit)
    }
}
