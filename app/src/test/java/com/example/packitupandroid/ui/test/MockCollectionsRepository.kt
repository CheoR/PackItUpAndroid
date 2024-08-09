package com.example.packitupandroid.ui.test

import com.example.packitupandroid.data.database.entities.CollectionEntity
import com.example.packitupandroid.data.database.entities.toCollection
import com.example.packitupandroid.data.model.QueryCollection
import com.example.packitupandroid.data.model.QueryDropdownOptions
import com.example.packitupandroid.data.model.toQueryCollection
import com.example.packitupandroid.data.repository.CollectionsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

class MockCollectionsRepository : CollectionsRepository {
    private val collections = mutableListOf<CollectionEntity>()
    private val collectionFlow = MutableStateFlow<List<CollectionEntity>>(emptyList())

    override suspend fun getCollection(id: String): CollectionEntity? {
        return this.collections.find { it.id == id }
    }

    override suspend fun getQueryCollection(id: String): QueryCollection? {
        return this.getCollection(id)?.toCollection()?.toQueryCollection()
    }

    override fun getAllCollectionsStream(): Flow<List<QueryCollection>> {
        return flowOf(this.collections.map { it.toCollection().toQueryCollection() })
    }

    override fun getCollectionStream(id: String): Flow<CollectionEntity?> {
        return collectionFlow.map { it.find { collection -> collection.id == id } }
    }

    override suspend fun getDropdownSelections(): Flow<List<QueryDropdownOptions>> {
        return flowOf(collections.map { it -> QueryDropdownOptions(it.id, it.name) })
    }

    override suspend fun insertCollection(collection: CollectionEntity) {
        this.collections.add(collection)
        collectionFlow.value = this.collections.toList()
    }

    override suspend fun insertAll(collections: List<CollectionEntity>) {
        this.collections.addAll(collections)
        collectionFlow.value = this.collections
    }

    override suspend fun updateCollection(collection: CollectionEntity) {
        val index = collections.indexOfFirst { it.id == collection.id }
        if (index != -1) {
            this.collections[index] = collection
            collectionFlow.value = this.collections
        }
    }

    override suspend fun deleteCollection(collection: CollectionEntity) {
        this.collections.removeAll { it.id == collection.id}
        collectionFlow.value = this.collections
    }

    override suspend fun deleteAll(collections: List<CollectionEntity>) {
        this.collections.removeAll(collections)
        collectionFlow.value = this.collections.toList()
    }

    override suspend fun clearAllCollections() {
        this.collections.clear()
        collectionFlow.value = this.collections

    }
}
