package com.example.packitupandroid.data.database.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.packitupandroid.data.database.AppDatabase
import com.example.packitupandroid.data.database.entities.ItemEntity
import com.example.packitupandroid.data.model.Item
import com.example.packitupandroid.data.repository.toEntity
import com.example.packitupandroid.source.local.TestDataSource
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotSame
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
class ItemDaoTest {
    private val collections = TestDataSource().collections
    private val boxes = TestDataSource().boxes
    private val items = TestDataSource().items

    private lateinit var collectionDao: CollectionDao
    private lateinit var boxDao: BoxDao
    private lateinit var itemDao: ItemDao
    private lateinit var db: AppDatabase

    private suspend fun observeAll() = itemDao.observeAll().first()

    private fun assertSameProperties(list1: List<Item>, list2: List<Item>) {
        for (i in list1.indices) {
            assertEquals(list1[i].id, list2[i].id)
            assertEquals(list1[i].name, list2[i].name)
            assertEquals(list1[i].description, list2[i].description)
            assertEquals(list1[i].value, list2[i].value)
            assertEquals(list1[i].isFragile, list2[i].isFragile)
            assertEquals(list1[i].boxId, list2[i].boxId)
        }
    }

    @Before
    fun setup() {
        val context: Context = ApplicationProvider.getApplicationContext()
        // Use in-memory db. Information stored here disappears when process killed.
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            // Run DAO queries in main thread.
            // Allow main thread queries, just for testing.
            .allowMainThreadQueries()
            .build()
        collectionDao = db.collectionDao()
        boxDao = db.boxDao()
        itemDao = db.itemDao()

        runBlocking {
            val itemEntities = items.map { it.toEntity() }
            val boxEntities = boxes.map { it.toEntity() }
            val collectionEntities = collections.map { it.toEntity() }

            collectionDao.insert(collectionEntities)
            boxDao.insert(boxEntities)
            itemDao.insert(itemEntities)
        }
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun itemDao_GetAllItems_AllItemsReturnedFromDB() = runTest {
        // without .first()
        // type is val allItems: Flow<List<Item>>
        // .first() turns flow into list.
        val dbItems = observeAll()

        assertSameProperties(dbItems.filterNotNull(), items)
    }

    @Test
    @Throws(Exception::class)
    fun itemDao_getById_ItemReturnedFromDB() = runTest {
        val item = itemDao.get("ebaba247-330d-47be-8cd1-ec40db5c6ae7")

        assertSameProperties(listOf(item!!), listOf(items[3]))
    }

    @Test
    @Throws(Exception::class)
    fun itemDao_getWithNonExistingId_NullReturned() = runTest {
        val item = itemDao.get("doesNotExist")
        assertNull(item)
    }

    @Test
    @Throws(Exception::class)
    fun itemDao_insertItems_AllItemsInsertedIntoDB() = runTest {
        val dbItems = observeAll()

        assertEquals(items.size, dbItems.size)
    }

    @Test
    @Throws(Exception::class)
    fun itemDao_UpdateItems_ItemsUpdatedInDB() = runTest {
        val updatedItem = items.first().copy(
            name = "tacos",
            description = "updated item 1",
            value = 25.0,
        )
        itemDao.update(updatedItem.toEntity())

        val dbItems = observeAll()

        assertSameProperties(listOf(dbItems.first()!!), listOf(updatedItem))
    }

    @Test
    @Throws(Exception::class)
    fun itemDao_UpdateItemWithNonExistingId_NoChangesInDB() = runTest {
        val nonExistingItem = ItemEntity(
            id = "doesNotExist",
            name = "Non-Existing Item",
            value = 10.0,
        )
        itemDao.update(nonExistingItem)

        val allItems = observeAll()
        assertSameProperties(allItems.filterNotNull(), items)
    }

    @Test
    @Throws(Exception::class)
    fun itemDao_DeleteItems_AllItemsDeletedFromDB() = runTest {
        itemDao.clear()

        val allItems = observeAll()

        assertTrue(allItems.isEmpty())
    }

    @Test
    @Throws(Exception::class)
    fun itemDao_DeleteSelectedItems_SelectedItemsDeletedFromDB() = runTest {
        val itemEntityIds = items.take(3).map { it.id }
        itemDao.delete(itemEntityIds)

        val allItems = observeAll()

        assertEquals(allItems.size, items.size - itemEntityIds.size)
        assertSameProperties(listOf(items[3]), listOf(allItems[0]!!))
    }

    @Test
    fun itemDao_DeleteBox_AssociatedItemsDeletedFromDB() = runTest {
        val numberOfItemsBeforeDeleteBox = observeAll().size

        val itemsToDelete = items.filter { it.boxId == boxes.first().id }
        boxDao.delete(listOf(boxes.first().id))

        val numberOfItemsAfterDeleteBox = observeAll().size

        assertNotSame(numberOfItemsBeforeDeleteBox, numberOfItemsAfterDeleteBox)
        assertEquals(numberOfItemsAfterDeleteBox, numberOfItemsBeforeDeleteBox - itemsToDelete.size)

        val item = itemDao.get("1")

        assertNull(item)
    }
}
