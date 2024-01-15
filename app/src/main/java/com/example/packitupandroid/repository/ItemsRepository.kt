package com.example.packitupandroid.repository

import com.example.packitupandroid.data.local.LocalDataSource
import com.example.packitupandroid.model.Item

interface ItemsRepository {
    fun loadItems() : List<Item>
}

class LocalItemsRepository(
    private val localDataSource: LocalDataSource,
) : ItemsRepository {
    override fun loadItems(): List<Item> {
        return localDataSource.loadItems()
    }
}