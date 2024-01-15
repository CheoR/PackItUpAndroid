package com.example.packitupandroid.repository

import com.example.packitupandroid.data.local.LocalDataSource
import com.example.packitupandroid.model.Box

interface BoxesRepository {
    fun getBoxes() : List<Box>
}

class LocalBoxRepository(
    private val localDataSource: LocalDataSource,
) : BoxesRepository {
    override fun getBoxes(): List<Box> {
        return localDataSource.loadBoxes()
    }
}