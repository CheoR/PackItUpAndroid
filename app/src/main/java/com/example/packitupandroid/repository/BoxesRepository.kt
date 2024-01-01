package com.example.packitupandroid.repository

import com.example.packitupandroid.data.local.LocalDataSource
import com.example.packitupandroid.model.Box

interface BoxesRepository {
    fun getBoxes() : List<Box>
}

class LocalBoxRepository() : BoxesRepository {
    override fun getBoxes(): List<Box> {
        return LocalDataSource().loadBoxes()
    }
}