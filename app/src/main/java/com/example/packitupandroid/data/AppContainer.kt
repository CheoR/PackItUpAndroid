package com.example.packitupandroid.data

import android.content.Context
import com.example.packitupandroid.data.database.AppDatabase
import com.example.packitupandroid.data.repository.BoxesRepository
import com.example.packitupandroid.data.repository.CollectionsRepository
import com.example.packitupandroid.data.repository.ItemsRepository
import com.example.packitupandroid.data.repository.OfflineBoxesRepository
import com.example.packitupandroid.data.repository.OfflineCollectionsRepository
import com.example.packitupandroid.data.repository.OfflineItemsRepository
import com.example.packitupandroid.data.repository.OfflineSummaryRepository
import com.example.packitupandroid.data.repository.SummaryRepository


/**
 * App container for dependency injection.
 *
 * This interface defines the contract for an app container, which provides access to
 * various repositories used throughout the application.
 */
interface AppContainer {
    /**
     * Provides access to the [ItemsRepository].
     *
     * This repository provides access to item-related data.
     */
    val itemsRepository: ItemsRepository
    /**
     * Provides access to the [BoxesRepository].
     *
     * This repository provides access to box-related data.
     */
    val boxesRepository: BoxesRepository
    /**
     * Provides access to the [CollectionsRepository].
     *
     * This repository provides access to collection-related data.
     */
    val collectionsRepository: CollectionsRepository
    /**
     * Provides access to the [SummaryRepository].
     *
     * This repository provides access to summary-related data.
     */
    val summaryRepository: SummaryRepository
}

/**
 * Default implementation of the [AppContainer] interface.
 *
 * This class creates and provides instances of the various repositories used in the application.
 * It utilizes the [AppDatabase] to access the underlying data.
 *
 * @param context The application context.
 * @param useMockData Whether to use mock data or not.
 */
class DefaultAppContainer(
    private val context: Context,
    private val useMockData: Boolean,
) : AppContainer {

    private val database: AppDatabase by lazy { AppDatabase.getDatabase(context, useMockData) }

    /**
     * Provides an instance of [ItemsRepository].
     *
     * This implementation uses [OfflineItemsRepository] and retrieves the [ItemDao] from the database.
     * It is lazily initialized to ensure that the database is only accessed when needed.
     */
    override val itemsRepository: ItemsRepository by lazy {
        // Call getDatabase() to instantiate database instance
        // on AppDatabase class passing in context and call
        // .itemDao() to create Dao instance
        OfflineItemsRepository(database.itemDao())
    }

    /**
     * Provides an instance of [BoxesRepository].
     *
     * This implementation uses [OfflineBoxesRepository] and retrieves the [BoxDao] from the database.
     * It is lazily initialized to ensure that the database is only accessed when needed.
     */
    override val boxesRepository: BoxesRepository by lazy {
        OfflineBoxesRepository(database.boxDao())
    }

    /**
     * Provides an instance of [CollectionsRepository].
     *
     * This implementation uses [OfflineCollectionsRepository] and retrieves the [CollectionDao] from the database.
     * It is lazily initialized to ensure that the database is only accessed when needed.
     */
    override val collectionsRepository: CollectionsRepository by lazy {
        OfflineCollectionsRepository(database.collectionDao())
    }

    /**
     * Provides an instance of [SummaryRepository].
     *
     * This implementation uses [OfflineSummaryRepository] and retrieves the [SummaryDao] from the database.
     * It is lazily initialized to ensure that the database is only accessed when needed.
     */
    override val summaryRepository: SummaryRepository by lazy {
        OfflineSummaryRepository(database.summaryDao())
    }
}
