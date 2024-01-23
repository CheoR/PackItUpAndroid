package com.example.packitupandroid.repository

import com.example.packitupandroid.data.local.LocalDataSource
import com.example.packitupandroid.model.Box
import com.example.packitupandroid.model.Collection
import com.example.packitupandroid.model.Item

interface DataRepository {
    suspend fun loadItems(): List<Item>
    suspend fun loadBoxes(): List<Box>
    suspend fun loadCollections(): List<Collection>
}

class LocalDataRepository (
    private val localDataSource: LocalDataSource = LocalDataSource(),
) : DataRepository {

    override suspend fun loadItems(): List<Item> {
        return localDataSource.loadItems()
    }

    override suspend fun loadBoxes(): List<Box> {
        return localDataSource.loadBoxes()
    }

    override suspend fun loadCollections(): List<Collection> {
        return localDataSource.loadCollections()
    }
}
