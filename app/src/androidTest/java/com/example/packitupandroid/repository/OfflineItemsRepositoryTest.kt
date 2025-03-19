package com.example.packitupandroid.repository

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.packitupandroid.data.database.AppDatabase
import com.example.packitupandroid.data.database.dao.BoxDao
import com.example.packitupandroid.data.database.dao.CollectionDao
import com.example.packitupandroid.data.database.dao.ItemDao
import com.example.packitupandroid.data.model.BoxIdAndName
import com.example.packitupandroid.data.repository.OfflineBoxesRepository
import com.example.packitupandroid.data.repository.OfflineCollectionsRepository
import com.example.packitupandroid.data.repository.OfflineItemsRepository
import com.example.packitupandroid.source.local.TestDataSource
import com.example.packitupandroid.utils.Result
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
class OfflineItemsRepositoryTest {
    private val collections = TestDataSource().collections
    private val boxes = TestDataSource().boxes
    private val items = TestDataSource().items

    private lateinit var db: AppDatabase
    private lateinit var itemDao: ItemDao
    private lateinit var boxDao: BoxDao
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
    fun offlineItemsRepository_InsertItems_ObserveAll_ReturnsAllItems() = runTest {
        val result = itemsRepository.observeAll().first()

        assertTrue(result is Result.Success)
        assertEquals(items.size, (result as Result.Success).data.size)
    }

    @Test
    fun offlineItemsRepository_GetItemById_ReturnsCorrectItem() = runTest {
        val result = itemsRepository.get(items.first().id)

        assertTrue(result is Result.Success)
        assertEquals(items.first(), (result as Result.Success).data)
        assertEquals(items.first().name, result.data?.name)
    }

    @Test
    fun offlineItemsRepository_getWithNonExistingId_NullReturned() = runTest {
        val result = itemsRepository.get("doesNotExist")

        // TODO: make fun return exception instead of null
        val expected = (result as? Result.Error)
        assertNull(expected)
    }

    @Test
    fun offlineItemsRepository_ObserveItemById_EmitsCorrectItem() = runTest {
        val result = itemsRepository.observeAll().first()

        assertTrue(result is Result.Success)
        assertEquals(items.first(), (result as Result.Success).data.first())
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun offlineItemsRepository_UpdateItem_ItemUpdatesInDb() = runTest {
        val item = items.first()
        val updatedItem = item.copy(
            name = "Updated Item 1",
            description = "Updated description",
            value = 200.0,
            isFragile = false,
            boxId = boxes.last().id,
        )
        itemsRepository.update(updatedItem)

        val result2 = itemsRepository.observe(item.id).first()

        assertTrue(result2 is Result.Success)

        val item2 = (result2 as Result.Success).data

        assertEquals(updatedItem, result2.data)
        assertEquals(updatedItem.name, item2?.name)
        assertEquals(updatedItem.description, item2?.description)
        assertEquals(updatedItem.value, item2?.value)
        assertEquals(updatedItem.isFragile, item2?.isFragile)
        assertEquals(updatedItem.boxId, item2?.boxId)

        assertNotEquals(item, item2)
        assertNotEquals(item.name, item2?.name)
        assertNotEquals(item.description, item2?.description)
        assertNotEquals(item.value, item2?.value)
        assertNotEquals(item.isFragile, item2?.isFragile)
        assertNotEquals(item.boxId, item2?.boxId)
    }

    @Test
    fun offlineItemsRepository_DeleteItems_ItemsDeletedInDb() = runTest {
        val itemsToDelete = listOf(
            items.first().id,
            items.last().id,
        )
        val result = itemsRepository.delete(itemsToDelete)

        assertTrue(result is Result.Success)

        val dbItems = itemsRepository.observeAll().first()
        assertEquals(items.size - itemsToDelete.size, (dbItems as Result.Success).data.size)
    }

    @Test
    fun offlineItemsRepository_ClearItems_ClearsAllItemsInDb() = runTest {
        val currentItems = itemsRepository.observeAll().first()
        assertEquals(items.size, (currentItems as Result.Success).data.size)

        val result = itemsRepository.clear()
        assertTrue(result is Result.Success)

        val dbItems = itemsRepository.observeAll().first()
        assertTrue((dbItems as Result.Success).data.isEmpty())
    }

    @Test
    fun offlineItemsRepository_DeleteBox_AssociatedItems_AreDeleted() = runTest {
        val boxResult = boxesRepository.observe(boxes.first().id).first()
        val currItems = itemsRepository.observeAll().first()
        assertEquals(items.size, (currItems as Result.Success).data.size)

        val box = (boxResult as Result.Success).data
        val result = boxesRepository.delete(listOf(
            box!!.id,
        ))
        assertTrue(result is Result.Success)

        val dbItems = itemsRepository.observeAll().first()
        assertEquals((items.size - box.itemCount), (dbItems as Result.Success).data.size)
    }

    @Test
    fun offlineItemsRepository_DeleteCollection_AssociatedBoxesAndItems_AreDeleted() = runTest {
        val collectionResult = collectionsRepository.observe(collections.first().id).first()
        val currItems = itemsRepository.observeAll().first()
        assertEquals(items.size, (currItems as Result.Success).data.size)

        val collection = (collectionResult as Result.Success).data
        val result = collectionsRepository.delete(listOf(
            collection!!.id,
        ))

        assertTrue(result is Result.Success)
        val dbItems = itemsRepository.observeAll().first()
        assertEquals((items.size - collection.itemCount), (dbItems as Result.Success).data.size)
    }

    @Test
    fun offlineItemsRepository_ListOfBoxIdsAndNames_ReturnsAllBoxesWithIdsAndNames() = runTest {
        val result = itemsRepository.listOfBoxIdsAndNames().first()

        assertTrue(result is Result.Success)
        val expected = boxes.map { BoxIdAndName(it.id, it.name) }
        assertEquals(expected, (result as Result.Success).data)
    }
}
