package com.example.packitupandroid.repository

import com.example.packitupandroid.data.local.LocalDataSource
import com.example.packitupandroid.model.Box
import com.example.packitupandroid.model.Collection
import com.example.packitupandroid.model.Item

interface DataRepository {
    fun getItems(): List<Item>
    fun getBoxes() : List<Box>
    fun getCollections() : List<Collection>
}

class LocalDataRepository(
    private val localDataSource: LocalDataSource,
) : DataRepository {

    override fun getItems(): List<Item> {
        return localDataSource.loadItems()
    }
    override fun getBoxes(): List<Box> {
        return localDataSource.loadBoxes()
    }

    override fun getCollections(): List<Collection> {
        return localDataSource.loadCollections()
    }
}