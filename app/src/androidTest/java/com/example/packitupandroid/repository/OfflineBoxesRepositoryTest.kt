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
import com.example.packitupandroid.source.local.boxes
import com.example.packitupandroid.source.local.collections
import com.example.packitupandroid.source.local.items
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
class OfflineBoxesRepositoryTest {
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
    fun offlineBoxesRepository_InsertBoxes_ObserveAll_ReturnsAllBoxes() = runTest {
        val result = boxesRepository.observeAll().first()

        assertTrue(result is Result.Success)
        assertEquals(boxes.size, (result as Result.Success).data.size)
    }

    @Test
    fun offlineBoxesRepository_GetBoxById_ReturnsCorrectBox() = runTest {
        val result = boxesRepository.get(boxes.first().id)

        assertTrue(result is Result.Success)
        assertEquals(boxes.first().id, (result as Result.Success).data?.id)
        assertEquals(boxes.first().name, result.data?.name)
    }
    
    @Test
    fun offlineBoxesRepository_getWithNonExistingId_NullReturned() = runTest {
        val result = boxesRepository.get("doesNotExist")
        val expected = (result as? Result.Error)

        assertNull(expected)
    }

    @Test
    fun offlineBoxesRepository_ObserveBoxById_EmitsCorrectBox() = runTest {
        val result = boxesRepository.observe(boxes.first().id).first()

        assertTrue(result is Result.Success)
        assertEquals(boxes.first().id, (result as Result.Success).data?.id)
    }

    @Test
    fun offlineBoxesRepository_UpdateBox_UpdatesInDb() = runTest {
        val updatedBox = boxes.first().copy(
            name = "Updated Box 1",
            description = "Updated description",
        )
        boxesRepository.update(updatedBox)

        val result = boxesRepository.observe(boxes.first().id).first()

        assertTrue(result is Result.Success)

        assertEquals(updatedBox.name, (result as Result.Success).data?.name)
        assertEquals(boxes.first().description, null)

        assertNotEquals(boxes.first().name, result.data?.name)
        assertNotEquals(boxes.first().description, result.data?.description)
    }

    @Test
    fun offlineBoxesRepository_DeleteBoxes_BoxesDeletedInDb() = runTest {
        val boxesToDelete = listOf(
            boxes.first().id,
            boxes.last().id,
        )

        val result = boxesRepository.delete(boxesToDelete)

        assertTrue(result is Result.Success)

        val dbBoxes = boxesRepository.observeAll().first()
        assertEquals(boxes.size - boxesToDelete.size, (dbBoxes as Result.Success).data.size)
    }

    @Test
    fun offlineBoxesRepository_ClearBoxes_ReturnsEmptyList() = runTest {
        val currentBoxes = boxesRepository.observeAll().first()
        assertEquals(boxes.size, (currentBoxes as Result.Success).data.size)

        val result = boxesRepository.clear()
        assertTrue(result is Result.Success)

        val dbBoxes = boxesRepository.observeAll().first()
        assertTrue((dbBoxes as Result.Success).data.isEmpty())
    }

    @Test
    fun offlineBoxesRepository_deleteCollection_AssociatedBoxes_AreDeleted() = runTest {
        val collectionResult = collectionsRepository.observe(collections.first().id).first()
        val currBoxes = boxesRepository.observeAll().first()
        assertEquals(boxes.size, (currBoxes as Result.Success).data.size)

        val collection = (collectionResult as Result.Success).data
        val result = collectionsRepository.delete(listOf(
            collection!!.id,
        ))
        assertTrue(result is Result.Success)

        val dbBoxes = boxesRepository.observeAll().first()
        assertEquals((boxes.size - collection.boxCount), (dbBoxes as Result.Success).data.size)
    }

    @Test
    fun offlineBoxesRepository_ItemUpdatesValue_BoxValueUpdates() = runTest {
        val itemResult = itemsRepository.observe(items.first().id).first()
        val boxResult = boxesRepository.observe(boxes.first().id).first()
        val item = (itemResult as Result.Success).data
        val box = (boxResult as Result.Success).data
        val initialBoxValue = box?.value ?: 0.00

        val updatedItem = item!!.copy(
            value = 100.0,
        )
        itemsRepository.update(updatedItem)

        val updatedBoxResult = boxesRepository.observe(boxes.first().id).first()
        val updatedBox = (updatedBoxResult as Result.Success).data

        assertEquals(initialBoxValue + updatedItem.value, updatedBox?.value)
    }

    @Test
    fun offlineBoxesRepository_ItemUpdatesIsFragile_BoxIsFragileUpdates() = runTest {
        val itemResult = itemsRepository.observe(items.first().id).first()
        val boxResult = boxesRepository.observe(boxes.first().id).first()
        val item = (itemResult as Result.Success).data
        val box = (boxResult as Result.Success).data
        val initialBoxIsFragile = box?.isFragile

        assertFalse(initialBoxIsFragile!!)
        val updatedItem = item!!.copy(
            isFragile = true,
        )
        itemsRepository.update(updatedItem)

        val updatedBoxResult = boxesRepository.observe(boxes.first().id).first()
        val updatedBox = (updatedBoxResult as Result.Success).data

        assertTrue(updatedBox!!.isFragile)
    }

    @Test
    fun offlineBoxesRepository_DeleteItem_AssociatedBoxValueUpdates() = runTest {
        val boxResult = boxesRepository.get(boxes.first().id)
        val initialBoxValue = (boxResult as Result.Success).data?.value ?: 0.00

        itemsRepository.delete(listOf(items.first().id))

        val updatedBoxResult = boxesRepository.get(boxes.first().id)
        val updatedBoxValue = (updatedBoxResult as Result.Success).data?.value ?: 0.00

        assertEquals(initialBoxValue - items.first().value, updatedBoxValue)
    }

    @Test
    fun offlineBoxesRepository_DeleteBox_AssociatedBoxIsFragileUpdates() = runTest {
        val boxResult = boxesRepository.get(boxes.first().id)
        val initialBoxIsFragile = (boxResult as Result.Success).data?.isFragile

        assertFalse(initialBoxIsFragile!!)

        itemsRepository.update(items.first().copy(
            isFragile = true,
        ))

        val updatedBoxResult = boxesRepository.get(boxes.first().id)
        val updatedBoxIsFragile = (updatedBoxResult as Result.Success).data?.isFragile

        assertTrue(updatedBoxIsFragile!!)
    }

    @Test
    fun offlineBoxesRepository_DeleteItem_AssociatedBoxItemCountUpdates() = runTest {
        val boxResult = boxesRepository.get(boxes.first().id)
        val initialItemCount = (boxResult as Result.Success).data?.itemCount
        val itemsToDelete = listOf(
            items.first().id,
        )

        itemsRepository.delete(itemsToDelete)

        val updatedBoxResult = boxesRepository.get(boxes.first().id)
        val updatedItemCount = (updatedBoxResult as Result.Success).data?.itemCount

        assertEquals(initialItemCount!! - itemsToDelete.size, updatedItemCount)
    }

    @Test
    fun offlineBoxesRepository_DeleteCollection_AssociatedBoxAreDeleted() = runTest {
        val currBoxes = boxesRepository.observeAll().first()
        assertEquals(boxes.size, (currBoxes as Result.Success).data.size)

        val collectionResult = collectionsRepository.observe(collections.first().id).first()
        val associatedBoxesCount = (collectionResult as Result.Success).data?.boxCount ?: 0

        collectionsRepository.delete(listOf(collections.first().id))

        val dbBoxes = boxesRepository.observeAll().first()
        assertEquals(currBoxes.data.size - associatedBoxesCount, (dbBoxes as Result.Success).data.size)
    }
}