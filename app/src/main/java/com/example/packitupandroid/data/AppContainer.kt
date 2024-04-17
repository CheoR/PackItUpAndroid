package com.example.packitupandroid.data

import android.content.Context
import com.example.packitupandroid.data.database.AppDatabase
import com.example.packitupandroid.data.repository.BoxesRepository
import com.example.packitupandroid.data.repository.ItemsRepository
import com.example.packitupandroid.data.repository.LocalDataRepository
import com.example.packitupandroid.data.repository.OfflineBoxesRepository
import com.example.packitupandroid.data.repository.OfflineItemsRepository

/**
 * App container for Dependency injection.
 */
interface AppContainer {
    val localDataRepository: LocalDataRepository
    val itemsRepository: ItemsRepository
    val boxesRepository: BoxesRepository
}

/**
 * [AppContainer] implementation provides [OfflineItemsRepository] instance, [LocalDataRepository]
 * instance, and [OfflineBoxesRepository] instance.
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

    /**
     * [BoxesRepository] implementation
     */
    override val boxesRepository: BoxesRepository by lazy {
        OfflineBoxesRepository(AppDatabase.getDatabase(context).boxDao())
    }
}
