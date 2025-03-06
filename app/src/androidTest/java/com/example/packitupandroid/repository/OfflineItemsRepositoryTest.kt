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
import com.example.packitupandroid.source.local.boxes
import com.example.packitupandroid.source.local.collections
import com.example.packitupandroid.source.local.items
import com.example.packitupandroid.utils.Result
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException


@RunWith(AndroidJUnit4::class)
class OfflineItemsRepositoryTest {
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
        val result = itemsRepository.observe("1").first()

        assertTrue(result is Result.Success)
        assertEquals(items.first(), (result as Result.Success).data)
    }

    @Test
    fun offlineItemsRepository_UpdateItem_ItemUpdatesInDb() = runTest {
        val updatedItem = items.first().copy(
            name = "Updated Item 1",
            description = "Updated description",
            value = 100.0,
            isFragile = true,
            boxId = "2",
        )
        val result = itemsRepository.update(updatedItem)

        assertTrue(result is Result.Success)
        val dbItem = itemDao.get(items.first().id)

        assertTrue(dbItem != null)
        assertEquals(updatedItem, dbItem)
        assertEquals(updatedItem.name, dbItem?.name)
        assertEquals(updatedItem.description, dbItem?.description)
        assertEquals(updatedItem.value, dbItem?.value)
        assertEquals(updatedItem.isFragile, dbItem?.isFragile)
        assertEquals(updatedItem.boxId, dbItem?.boxId)
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
