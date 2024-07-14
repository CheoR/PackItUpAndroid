package com.example.packitupandroid.ui.test

import com.example.packitupandroid.data.database.entities.ItemEntity
import com.example.packitupandroid.data.repository.ItemsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class MockItemsRepository : ItemsRepository {
    private val items = mutableListOf<ItemEntity>()
    private val itemFlow = MutableStateFlow<List<ItemEntity>>(emptyList())

    override suspend fun getItem(id: String): ItemEntity? {
        return this.items.find { it.id == id }
    }

    override fun getAllItemsStream(): Flow<List<ItemEntity>> {
        return itemFlow
    }

    override fun getItemStream(id: String): Flow<ItemEntity?> {
        return itemFlow.map { it.find { item -> item.id == id } }
    }

    override suspend fun insertItem(item: ItemEntity) {
        this.items.add(item)
        itemFlow.value = this.items.toList()
    }

    override suspend fun insertAll(items: List<ItemEntity>) {
        this.items.addAll(items)
        itemFlow.value = this.items
    }

    override suspend fun updateItem(item: ItemEntity) {
        val index = items.indexOfFirst { it.id == item.id }
        if (index != -1) {
            this.items[index] = item
            itemFlow.value = this.items
        }
    }

    override suspend fun deleteItem(item: ItemEntity) {
        this.items.remove(item)
        itemFlow.value = this.items
    }

    override suspend fun deleteAll(items: List<ItemEntity>) {
        this.items.removeAll(items)
        itemFlow.value = this.items.toList()
    }

    override suspend fun clearAllItems() {
        this.items.clear()
        itemFlow.value = this.items
    }

}
