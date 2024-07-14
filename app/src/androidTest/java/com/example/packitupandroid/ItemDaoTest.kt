package com.example.packitupandroid

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
import com.example.packitupandroid.data.database.entities.toItem
import com.example.packitupandroid.data.model.Item
import com.example.packitupandroid.data.model.toEntity
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
        collectionDao.insertAll(collections)
    }

    private suspend fun insertBoxes(boxes: List<BoxEntity>) {
        boxDao.insertAll(boxes)
    }
    private suspend fun insertItems(items: List<ItemEntity>) {
        itemDao.insertAll(items)
    }
    private suspend fun getAllItems() = itemDao.getAllItems().first()

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
    fun itemDao_InsertAllItems_AllItemsInsertedIntoDB() = runBlocking {
        val itemEntities = listOf(
            Item("10", "item1", "1", boxId=boxes[0].id).toEntity(),
            Item("20", "Item2", "1", boxId=boxes[0].id).toEntity(),
            Item("30", "Item3", "2", boxId=boxes[0].id).toEntity(),
            Item("40", "Item4", "2", boxId=boxes[1].id).toEntity(),
        )
        itemDao.insertAll(itemEntities)

        val allItems = getAllItems()
        assertEquals(allItems.size, items.size + itemEntities.size)
    }

    @Test
    @Throws(Exception::class)
    fun itemDao_GetAllItems_AllItemsReturnedFromDB() = runBlocking {
        // without .first()
        // type is val allItems: Flow<List<Item>>
        // .first() turns flow into list.
        val dbItems = getAllItems()

        assertSameProperties(dbItems.map { it.toItem() }, items.map { it.toItem() })
    }

    @Test
    @Throws(Exception::class)
    fun itemDao_GetItemById_ItemReturnedFromDB() = runBlocking {
        val item = itemDao.getItem("4").first()

        assertSameProperties(listOf(item.toItem()), listOf(items[3].toItem()))
    }

    @Test
    @Throws(Exception::class)
    fun itemDao_GetItemWithNonExistingId_NullReturned() = runBlocking {
        val item = itemDao.getItem("doesNotExist").first()
        assertNull(item)
    }

    @Test
    @Throws(Exception::class)
    fun itemDao_UpdateItems_ItemsUpdatedInDB() = runBlocking {
        val updatedItem = items[0].copy(name = "tacos", description = "updated item 1", value = 25.0)

        itemDao.update(updatedItem)

        val dbItems = getAllItems()

        assertSameProperties(listOf(dbItems[0].toItem()), listOf(updatedItem.toItem()))
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

        val allItems = getAllItems()
        assertSameProperties(allItems.map { it.toItem() }, items.map { it.toItem() })
    }

    @Test
    @Throws(Exception::class)
    fun itemDao_DeleteItems_AllItemsDeletedFromDB() = runBlocking {
        itemDao.clearAllItems()

        val allItems = getAllItems()

        assertTrue(allItems.isEmpty())
    }

    @Test
    @Throws(Exception::class)
    fun itemDao_DeleteSelectedItems_SelectedItemsDeletedFromDB() = runBlocking {
        val itemEntities = listOf(items[0], items[2], items[3])
        itemDao.deleteAll(itemEntities)

        val allItems = getAllItems()

        assertEquals(allItems.size, items.size - itemEntities.size)
        assertSameProperties(listOf(items[1].toItem()), listOf(allItems[0].toItem()))
    }

    @Test
    fun itemDao_DeleteBox_AssociatedItemsDeletedFromDB() = runBlocking {
        val numberOfItemsBeforeDeleteBox = getAllItems().size

        val itemsToDelete = items.filter { it.boxId == boxes[0].id }
        boxDao.delete(boxes[0])

        val numberOfItemsAfterDeleteBox = getAllItems().size

        assertNotSame(numberOfItemsBeforeDeleteBox, numberOfItemsAfterDeleteBox)
        assertEquals(numberOfItemsAfterDeleteBox, numberOfItemsBeforeDeleteBox - itemsToDelete.size)

        val item = itemDao.getItem("1").first()

        assertNull(item)
    }
}
