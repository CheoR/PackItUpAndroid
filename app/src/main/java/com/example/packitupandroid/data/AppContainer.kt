package com.example.packitupandroid.data

import android.content.Context
import com.example.packitupandroid.data.database.AppDatabase
import com.example.packitupandroid.data.repository.BoxesRepository
import com.example.packitupandroid.data.repository.CollectionsRepository
import com.example.packitupandroid.data.repository.ItemsRepository
import com.example.packitupandroid.data.repository.LocalDataRepository
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
     * Provides access to the [LocalDataRepository].
     */
    val localDataRepository: LocalDataRepository
    /**
     * Provides access to the [ItemsRepository].
     */
    val itemsRepository: ItemsRepository
    /**
     * Provides access to the [BoxesRepository].
     */
    val boxesRepository: BoxesRepository
    /**
     * Provides access to the [CollectionsRepository].
     */
    val collectionsRepository: CollectionsRepository
    /**
     * Provides access to the [SummaryRepository].
     */
    val summaryRepository: SummaryRepository
}

/**
 * [AppContainer] implementation that provides concrete instances of repositories.
 *
 * This class implements the [AppContainer] interface and provides concrete instances of
 * repositories, using the [AppDatabase] to access the database.
 *
 * @param context The context used to create the database.
 */
class DefaultAppContainer(private val context: Context) : AppContainer {

    /**
     * Provides a [LocalDataRepository] instance.
     */
    override val localDataRepository: LocalDataRepository by lazy {
        LocalDataRepository()
    }

    /**
     * Provides an [ItemsRepository] instance.
     *
     * This instance uses the [OfflineItemsRepository] and the [AppDatabase] to access item data.
     */
    override val itemsRepository: ItemsRepository by lazy {
        // Call getDatabase() to instantiate database instance
        // on AppDatabase class passing in context and call
        // .itemDao() to create Dao instance
        OfflineItemsRepository(AppDatabase.getDatabase(context).itemDao())
    }

    /**
     * Provides a [BoxesRepository] instance.
     *
     * This instance uses the [OfflineBoxesRepository] and the [AppDatabase] to access box data.
     */
    override val boxesRepository: BoxesRepository by lazy {
        OfflineBoxesRepository(AppDatabase.getDatabase(context).boxDao())
    }

    /**
     * Provides a [CollectionsRepository] instance.
     *
     * This instance uses the [OfflineCollectionsRepository] and the [AppDatabase] to access collection data.
     */
    override val collectionsRepository: CollectionsRepository by lazy {
        OfflineCollectionsRepository(AppDatabase.getDatabase(context).collectionDao())
    }

    /**
     * Provides a [SummaryRepository] instance.
     *
     * This instance uses the [OfflineSummaryRepository] and the [AppDatabase] to access summary data.
     */
    override val summaryRepository: SummaryRepository by lazy {
        OfflineSummaryRepository(AppDatabase.getDatabase(context).summaryDao())
    }
}
