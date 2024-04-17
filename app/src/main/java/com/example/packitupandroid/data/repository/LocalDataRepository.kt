package com.example.packitupandroid.data.repository

import com.example.packitupandroid.data.model.Box
import com.example.packitupandroid.data.model.Item
import com.example.packitupandroid.data.source.local.LocalDataSource

interface DataRepository {
    suspend fun loadItems(): List<Item>
    suspend fun loadBoxes(): List<Box>
//    suspend fun loadCollections(): List<Collection>
}

class LocalDataRepository (
    private val localDataSource: LocalDataSource = LocalDataSource(),
) : DataRepository {
    //    private var collections : List<Collection>? = null
    private var boxes : List<Box>? = null
    private var items : List<Item>? = null

    override suspend fun loadItems(): List<Item> {
        if(items == null) {
            items = localDataSource.loadItems()
        }
        return items as List<Item>
    }

    override suspend fun loadBoxes(): List<Box> {
        if (boxes == null) {
            boxes = localDataSource.loadBoxes()
        }
        return boxes as List<Box>
    }

//    override suspend fun loadCollections(): List<Collection> {
//        if(collections ==  null) {
//            collections = localDataSource.loadCollections()
//        }
//        return collections as List<Collection>
//    }
}
