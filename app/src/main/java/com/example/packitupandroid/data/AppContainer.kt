package com.example.packitupandroid.data

import com.example.packitupandroid.repository.LocalDataRepository

interface AppContainer {
    val localDataRepository: LocalDataRepository
}

class DefaultAppContainer : AppContainer {

    override val localDataRepository: LocalDataRepository by lazy {
        LocalDataRepository()
    }

}