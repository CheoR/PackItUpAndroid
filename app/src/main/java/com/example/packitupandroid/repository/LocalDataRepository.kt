package com.example.packitupandroid.repository

import com.example.packitupandroid.data.local.LocalDataSource
import com.example.packitupandroid.model.Box
import com.example.packitupandroid.model.Collection
import com.example.packitupandroid.model.Item

interface DataRepository {
    fun loadItems(): List<Item>
    fun loadBoxes() : List<Box>
    fun loadCollections() : List<Collection>
}

class LocalDataRepository (
    private val localDataSource: LocalDataSource = LocalDataSource(),
) : DataRepository {

    override fun loadItems(): List<Item> {
        return localDataSource.loadItems()
    }
    override fun loadBoxes(): List<Box> {
        return localDataSource.loadBoxes()
    }

    override fun loadCollections(): List<Collection> {
        return localDataSource.loadCollections()
    }
}