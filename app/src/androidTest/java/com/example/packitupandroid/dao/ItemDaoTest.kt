package com.example.packitupandroid.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.packitupandroid.data.database.AppDatabase
import com.example.packitupandroid.data.database.dao.BoxDao
import com.example.packitupandroid.data.database.dao.CollectionDao
import com.example.packitupandroid.data.database.dao.ItemDao
import com.example.packitupandroid.data.database.entities.BoxEntity
import com.example.packitupandroid.data.database.entities.CollectionEntity
import com.example.packitupandroid.data.database.entities.ItemEntity
import com.example.packitupandroid.data.model.Item
import com.example.packitupandroid.data.repository.toEntity
import com.example.packitupandroid.data.repository.toItem
import com.example.packitupandroid.fake.data.boxes
import com.example.packitupandroid.fake.data.collections
import com.example.packitupandroid.fake.data.items
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotSame
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException


@RunWith(AndroidJUnit4::class)
class ItemDaoTest {
    private lateinit var collectionDao: CollectionDao
    private lateinit var boxDao: BoxDao
    private lateinit var itemDao: ItemDao
    private lateinit var db: AppDatabase

    private suspend fun insertCollections(collections: List<CollectionEntity>) {
        collectionDao.insert(collections)
    }

    private suspend fun insertBoxes(boxes: List<BoxEntity>) {
        boxDao.insert(boxes)
    }
    private suspend fun insertItems(items: List<ItemEntity>) {
        itemDao.insert(items)
    }
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
    fun createDb() {
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
            insertCollections(collections)
            insertBoxes(boxes)
            insertItems(items)
        }
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun itemDao_insertItems_AllItemsInsertedIntoDB() = runBlocking {
        val itemEntities = listOf(
            Item("10", "item1", "1", boxId=boxes[0].id).toEntity(),
            Item("20", "Item2", "1", boxId=boxes[0].id).toEntity(),
            Item("30", "Item3", "2", boxId=boxes[0].id).toEntity(),
            Item("40", "Item4", "2", boxId=boxes[1].id).toEntity(),
        )
        itemDao.insert(itemEntities)

        val allItems = observeAll()
        assertEquals(allItems.size, items.size + itemEntities.size)
    }

    @Test
    @Throws(Exception::class)
    fun itemDao_GetAllItems_AllItemsReturnedFromDB() = runBlocking {
        // without .first()
        // type is val allItems: Flow<List<Item>>
        // .first() turns flow into list.
        val dbItems = observeAll()

        assertSameProperties(dbItems.filterNotNull(), items.map { it.toItem() })
    }

    @Test
    @Throws(Exception::class)
    fun itemDao_getById_ItemReturnedFromDB() = runBlocking {
        val item = itemDao.get("4")

        assertSameProperties(listOf(item!!), listOf(items[3].toItem()))
    }

    @Test
    @Throws(Exception::class)
    fun itemDao_getWithNonExistingId_NullReturned() = runBlocking {
        val item = itemDao.get("doesNotExist")
        assertNull(item)
    }

    @Test
    @Throws(Exception::class)
    fun itemDao_UpdateItems_ItemsUpdatedInDB() = runBlocking {
        val updatedItem = items[0].copy(name = "tacos", description = "updated item 1", value = 25.0)

        itemDao.update(updatedItem)

        val dbItems = observeAll()

        assertSameProperties(listOf(dbItems[0]!!), listOf(updatedItem.toItem()))
    }

    @Test
    @Throws(Exception::class)
    fun itemDao_UpdateItemWithNonExistingId_NoChangesInDB() = runBlocking {
        val nonExistingItem = Item(
            id = "doesNotExist",
            name = "Non-Existing Item",
            value = 10.0,
        ).toEntity()

        itemDao.update(nonExistingItem)

        val allItems = observeAll()
        assertSameProperties(allItems.filterNotNull(), items.map { it.toItem() })
    }

    @Test
    @Throws(Exception::class)
    fun itemDao_DeleteItems_AllItemsDeletedFromDB() = runBlocking {
        itemDao.clear()

        val allItems = observeAll()

        assertTrue(allItems.isEmpty())
    }

    @Test
    @Throws(Exception::class)
    fun itemDao_DeleteSelectedItems_SelectedItemsDeletedFromDB() = runBlocking {
        val itemEntityIds = listOf(items[0].id, items[2].id, items[3].id)
        itemDao.delete(itemEntityIds)

        val allItems = observeAll()

        assertEquals(allItems.size, items.size - itemEntityIds.size)
        assertSameProperties(listOf(items[1].toItem()), listOf(allItems[0]!!))
    }

    @Test
    fun itemDao_DeleteBox_AssociatedItemsDeletedFromDB() = runBlocking {
        val numberOfItemsBeforeDeleteBox = observeAll().size

        val itemsToDelete = items.filter { it.boxId == boxes[0].id }
        boxDao.delete(listOf(boxes[0].id))

        val numberOfItemsAfterDeleteBox = observeAll().size

        assertNotSame(numberOfItemsBeforeDeleteBox, numberOfItemsAfterDeleteBox)
        assertEquals(numberOfItemsAfterDeleteBox, numberOfItemsBeforeDeleteBox - itemsToDelete.size)

        val item = itemDao.get("1")

        assertNull(item)
    }
}
