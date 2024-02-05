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
    private var collections : List<Collection>? = null
    private var boxes : List<Box>? = null
    private var items : List<Item>? = null

    override suspend fun loadItems(): List<Item> {
        if(items == null) {
            items = localDataSource.loadItems()
        }
        return items as List<Item>
//        return localDataSource.loadItems()
    }

    override suspend fun loadBoxes(): List<Box> {
        if (boxes == null) {
            boxes = localDataSource.loadBoxes()
        }
        return boxes as List<Box>
//        return localDataSource.loadBoxes()
    }

    override suspend fun loadCollections(): List<Collection> {
//        return localDataSource.loadCollections()
        if(collections ==  null) {
            collections = localDataSource.loadCollections()
        }
        return collections as List<Collection>
    }
}
