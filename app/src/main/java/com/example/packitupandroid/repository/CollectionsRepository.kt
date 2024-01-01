package com.example.packitupandroid.repository

import com.example.packitupandroid.data.local.LocalDataSource
import com.example.packitupandroid.model.Collection

interface CollectionsRepository {
    fun getCollections() : List<Collection>
}

class LocalCollectionRepository : CollectionsRepository {
    override fun getCollections(): List<Collection> {
        return LocalDataSource().loadCollections()
    }
}