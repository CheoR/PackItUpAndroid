package com.example.packitupandroid.repository

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.packitupandroid.data.database.AppDatabase
import com.example.packitupandroid.data.database.dao.BoxDao
import com.example.packitupandroid.data.database.dao.CollectionDao
import com.example.packitupandroid.data.database.dao.ItemDao
import com.example.packitupandroid.data.repository.OfflineBoxesRepository
import com.example.packitupandroid.data.repository.OfflineCollectionsRepository
import com.example.packitupandroid.data.repository.OfflineItemsRepository
import com.example.packitupandroid.source.local.TestDataSource
import com.example.packitupandroid.utils.Result
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException


@RunWith(AndroidJUnit4::class)
class OfflineCollectionsRepositoryTest {
    private val collections = TestDataSource().collections
    private val boxes = TestDataSource().boxes
    private val items = TestDataSource().items

    private lateinit var db: AppDatabase
    private lateinit var boxDao: BoxDao
    private lateinit var itemDao: ItemDao
    private lateinit var collectionDao: CollectionDao
    private lateinit var itemsRepository: OfflineItemsRepository
    private lateinit var boxesRepository: OfflineBoxesRepository
    private lateinit var collectionsRepository: OfflineCollectionsRepository

    @Before
    fun setup() {
        val context: Context = ApplicationProvider.getApplicationContext()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries() // For testing purposes only
            .build()

        itemDao = db.itemDao()
        boxDao = db.boxDao()
        collectionDao = db.collectionDao()

        itemsRepository = OfflineItemsRepository(itemDao)
        boxesRepository = OfflineBoxesRepository(boxDao)
        collectionsRepository = OfflineCollectionsRepository(collectionDao)

        runBlocking {
            collectionsRepository.insert(collections)
            boxesRepository.insert(boxes)
            itemsRepository.insert(items)
        }
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    fun offlineCollectionssRepository_InsertCollections_ObserveAll_ReturnsAllCollections() = runTest {
        val result = collectionsRepository.observeAll().first()

        assertTrue(result is Result.Success)
        assertEquals(collections.size, (result as Result.Success).data.size)
    }

    @Test
    fun offlineCollectionssRepository_GetCollectionById_ReturnsCorrectCollection() = runTest {
        val result = collectionsRepository.get(collections.first().id)

        assertTrue(result is Result.Success)
        assertEquals(collections.first().id, (result as Result.Success).data?.id)
        assertEquals(collections.first().name, result.data?.name)
    }
    
    @Test
    fun offlineCollectionsRepository_getWithNonExistingId_NullReturned() = runTest {
        val result = collectionsRepository.get("doesNotExist")
        val expected = (result as? Result.Error)
        
        assertNull(expected)
    }

    @Test
    fun offlineCollectionssRepository_ObserveCollectionById_EmitsCorrectCollection() = runTest {
        val result = collectionsRepository.observe(collections.first().id).first()

        assertTrue(result is Result.Success)
        assertEquals(collections.first().id, (result as Result.Success).data?.id)
    }

    @Test
    fun offlineCollectionssRepository_UpdateCollection_UpdatesInDb() = runTest {
        val updatedCollection = collections.first().copy(
            name = "Updated Collection 1",
            description = "Updated description",
        )
        collectionsRepository.update(updatedCollection)

        val result = collectionsRepository.observe(collections.first().id).first()

        assertTrue(result is Result.Success)

        assertEquals(updatedCollection.name, (result as Result.Success).data?.name)
        assertEquals(collections.first().description, null)

        assertNotEquals(collections.first().name, result.data?.name)
        assertNotEquals(collections.first().description, result.data?.description)
    }

    @Test
    fun offlineCollectionssRepository_DeleteCollections_CollectionsDeletedInDb() = runTest {
        val collectionsToDelete = listOf(
            collections.first().id,
            collections.last().id,
        )

        val result = collectionsRepository.delete(collectionsToDelete)

        assertTrue(result is Result.Success)

        val dbCollections = collectionsRepository.observeAll().first()
        assertEquals(collections.size - collectionsToDelete.size, (dbCollections as Result.Success).data.size)
    }

    @Test
    fun offlineCollectionssRepository_ClearCollections_ReturnsEmptyList() = runTest {
        val currentCollections = collectionsRepository.observeAll().first()
        assertEquals(collections.size, (currentCollections as Result.Success).data.size)

        val result = collectionsRepository.clear()
        assertTrue(result is Result.Success)

        val dbCollections = collectionsRepository.observeAll().first()
        assertTrue((dbCollections as Result.Success).data.isEmpty())
    }

    @Test
    fun offlineCollectionssRepository_deleteBox_AssociatedCollectionBoxCountUpdates() = runTest {
        val collectionResult = collectionsRepository.get(collections.first().id)
        val initialBoxCount = (collectionResult as Result.Success).data?.boxCount ?: 0
        val boxesTodelete = listOf(
            boxes.first().id,
        )

        boxesRepository.delete(boxesTodelete)

        val updatedCollectionResult = collectionsRepository.get(collections.first().id)
        val updatedBoxCount = (updatedCollectionResult as Result.Success).data?.boxCount ?: 0

        assertEquals(initialBoxCount - boxesTodelete.size, updatedBoxCount)
    }

    @Test
    fun offlineCollectionssRepository_ItemUpdatesValue_AssociatedCollectionValueUpdates() = runTest {
        val currCollection = collectionsRepository.get(collections.first().id)
        val initialCollectionValue = (currCollection as Result.Success).data?.value ?: 0.00
        val currItem = itemsRepository.get(items.first().id)
        val initialItemValue = (currItem as Result.Success).data?.value ?: 0.00

        val newValue = 100.0

        itemsRepository.update(items.first().copy(
            value = newValue,
        ))

        val updatedCollectionResult = collectionsRepository.get(collections.first().id)
        val updatedCollectionValue = (updatedCollectionResult as Result.Success).data?.value ?: 0.00

        assertEquals(initialCollectionValue + newValue - initialItemValue, updatedCollectionValue)
    }

    @Test
    fun offlineCollectionssRepository_ItemUpdateIsFragile_AssociatedCollectionIsFragileUpdates() = runTest {
        val currCollection = collectionsRepository.get(collections.first().id)
        val initialCollectionIsFragile = (currCollection as Result.Success).data?.isFragile
        val currItem = itemsRepository.get(items.first().id)
        val initialItemValue = (currItem as Result.Success).data?.isFragile

        assertFalse(initialCollectionIsFragile!!)

        itemsRepository.update(items.first().copy(
            isFragile = !initialItemValue!!,
        ))

        val updatedCollectionResult = collectionsRepository.get(collections.first().id)
        val updatedCollectionIsFragile = (updatedCollectionResult as Result.Success).data?.isFragile

        assertEquals(!initialCollectionIsFragile, updatedCollectionIsFragile)
    }

    @Test
    fun offlineCollectionssRepository_DeleteItem_AssociatedCollectionValueUpdates() = runTest {
        val currCollection = collectionsRepository.get(collections.first().id)
        val currCollectionValue = (currCollection as Result.Success).data?.value ?: 0.00
        val currItem = itemsRepository.get(items.first().id)
        val currItemValue = (currItem as Result.Success).data?.value ?: 0.00

        val itemsToDelete = listOf(
            items.first().id,
        )
        itemsRepository.delete(itemsToDelete)

        val updatedCollectionResult = collectionsRepository.get(collections.first().id)
        val updatedCollectionItemValue = (updatedCollectionResult as Result.Success).data?.value

        assertEquals(currCollectionValue - currItemValue, updatedCollectionItemValue)
    }

    @Test
    fun offlineCollectionssRepository_DeleteItem_AssociatedCollectionItemCountUpdates() = runTest {
        val currCollection = collectionsRepository.get(collections.first().id)
        val currItemCount = (currCollection as Result.Success).data?.itemCount

        val itemsToDelete = listOf(
            items.first().id,
        )
        itemsRepository.delete(itemsToDelete)

        val updatedCollectionResult = collectionsRepository.get(collections.first().id)
        val updatedCollectionItemCount = (updatedCollectionResult as Result.Success).data?.itemCount

        assertEquals(currItemCount!! - itemsToDelete.size, updatedCollectionItemCount)
    }
}
