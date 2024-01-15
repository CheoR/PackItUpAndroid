package com.example.packitupandroid.repository

import com.example.packitupandroid.data.local.LocalDataSource
import com.example.packitupandroid.model.Collection

interface CollectionsRepository {
    fun getCollections() : List<Collection>
}

class LocalCollectionRepository(
    private val localDataSource: LocalDataSource,
) : CollectionsRepository {
    override fun getCollections(): List<Collection> {
        return localDataSource.loadCollections()
    }
}