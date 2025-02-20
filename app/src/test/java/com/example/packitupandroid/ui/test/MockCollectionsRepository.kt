package com.example.packitupandroid.ui.test

import com.example.packitupandroid.data.database.entities.CollectionEntity
import com.example.packitupandroid.data.model.Collection
import com.example.packitupandroid.data.repository.CollectionsRepository
import com.example.packitupandroid.data.repository.toCollection
import com.example.packitupandroid.data.repository.toEntity
import com.example.packitupandroid.utils.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map


class MockCollectionsRepository : CollectionsRepository {
    private val collections = mutableListOf<CollectionEntity>()
    private val collectionFlow = MutableStateFlow<List<CollectionEntity>>(emptyList())

    override suspend fun get(id: String): Result<Collection?> {
        return Result.Success(collections.find { it.id == id }?.toCollection())
    }

    override fun observeAll(): Flow<Result<List<Collection?>>> {
        return collectionFlow.map { listOfCollectionEntities -> Result.Success(listOfCollectionEntities.map { it.toCollection() }) }
    }

    override fun observe(id: String): Flow<Result<Collection?>> {
        TODO("Not yet implemented")
    }

    override suspend fun insert(data: List<Collection>): Result<Unit> {
        this.collections.addAll(data.map { it.toEntity() })
        collectionFlow.value = this.collections
        return Result.Success(Unit)
    }

    override suspend fun update(data: Collection): Result<Unit> {
        val index = collections.indexOfFirst { it.id == data.id }
        if (index != -1) {
            this.collections[index] = data.toEntity()
            collectionFlow.value = this.collections
        }
        return Result.Success(Unit)
    }

    override suspend fun delete(data: List<String>): Result<Unit> {
        this.collections.removeAll { data.contains(it.id) }
        collectionFlow.value = this.collections
        return Result.Success(Unit)
    }

    override suspend fun clear(): Result<Unit> {
        this.collections.clear()
        collectionFlow.value = this.collections
        return Result.Success(Unit)
    }
}
