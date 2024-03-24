package com.example.packitupandroid.data

import android.content.Context
import com.example.packitupandroid.repository.LocalDataRepository

/**
 * App container for Dependency injection.
 */
interface AppContainer {
    val localDataRepository: LocalDataRepository
    val itemsRepository: ItemsRepository
}

/**
 * [AppContainer] implementation provides [OfflineItemsRepository] instance and [LocalDataRepository]
 * instance
 */
class DefaultAppContainer(private val context: Context) : AppContainer {

    override val localDataRepository: LocalDataRepository by lazy {
        LocalDataRepository()
    }
    /**
     * [ItemsRepository] implementation
     */
    override val itemsRepository: ItemsRepository by lazy {
        // Call getDatabase() to instantiate database instance
        // on AppDatabase class passing in context and call
        // .itemDao() to create Dao instance
        OfflineItemsRepository(AppDatabase.getDatabase(context).itemDao())
    }
}