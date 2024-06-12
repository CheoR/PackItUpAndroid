package com.example.packitupandroid

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.packitupandroid.data.database.AppDatabase
import com.example.packitupandroid.data.database.dao.BoxDao
import com.example.packitupandroid.data.database.dao.ItemDao
import com.example.packitupandroid.data.model.Box
import com.example.packitupandroid.data.model.Item
import com.example.packitupandroid.data.model.toEntity
import junit.framework.TestCase.assertEquals
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
    private lateinit var itemDao: ItemDao
    private lateinit var boxDao: BoxDao
    private lateinit var db: AppDatabase

    private var box1 = Box("box1", "box1").toEntity()
    private var box2 = Box("box2", "box2").toEntity()

    private var item1 = Item("1", "item1", "1", 5.0, true, boxId = box1.id).toEntity()
    private var item2 = Item("2", "item2", boxId = box1.id).toEntity()
    private var item3 = Item("3", "item3", value = 5.0, boxId = box2.id).toEntity()
    private var item4 = Item("4", "item3", value = 5.0).toEntity()

    // utility functions
    private suspend fun addTwoBoxToDb () {
        boxDao.insert(box1)
        boxDao.insert(box2)
    }
    private suspend fun addOneItemToDb() {
        itemDao.insert(item1)
    }

    private suspend fun addTwoItemToDb() {
        addOneItemToDb()
        itemDao.insert(item2)
    }
    private suspend fun addThreeItemToDb() {
        addTwoItemToDb()
        itemDao.insert(item3)
    }

    private suspend fun addFourItemToDb() {
        addThreeItemToDb()
        itemDao.insert(item4)
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
        itemDao = db.itemDao()
        boxDao = db.boxDao()

        runBlocking {
            addTwoBoxToDb()
        }
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun daoInsert_insertItemIntoDB() = runBlocking {
        addOneItemToDb()
        val allItems = itemDao.getAllItems().first()
        assertEquals(allItems[0], item1)
    }

    @Test
    @Throws(Exception::class)
    fun daoInsert_insertAllItemIntoDB() = runBlocking {
        val itemEntities = listOf(item1, item2, item3, item4)
        itemDao.insertAll(itemEntities)

        val allItems = itemDao.getAllItems().first()
        assertEquals(itemEntities.size, allItems.size)
    }

    @Test
    @Throws(Exception::class)
    fun daoGetAllItems_returnsAllItemsFromDB() = runBlocking {
        addTwoItemToDb()
        // without .first()
        // type is val allItems: Flow<List<Item>>
        // .first() turns flow into list.
        val allItems = itemDao.getAllItems().first()

        assertEquals(allItems[0], item1)
        assertEquals(allItems[1], item2)
    }

    @Test
    @Throws(Exception::class)
    fun daoGetItem_returnsItemFromDB() = runBlocking {
        addOneItemToDb()
        // return flow
        val item = itemDao.getItem("1")
        // .first() - actual item
        assertEquals(item.first(), item1)
    }

    @Test
    @Throws(Exception::class)
    fun daoGetItem_returnsItemByIdFromDB() = runBlocking {
        addFourItemToDb()
        val item = itemDao.getItem("4").first()
        assertEquals(item4.id, item.id)
    }

    @Test
    @Throws(Exception::class)
    fun daoGetItemWithNonExistingId_returnsNull() = runBlocking {
        addFourItemToDb()
        val item = itemDao.getItem("doesNotExist").first()
        assertNull(item)
    }

    @Test
    @Throws(Exception::class)
    fun daoUpdateItems_updatesItemsInDB() = runBlocking {
        addTwoItemToDb()
        val updatedItem1 = item1.copy(name = "tacos", description = "updated item 1", value = 25.0)
        val updatedItem2 = item2.copy(name = "tacos", description = "updated item 2", value = 10.0)
        addTwoItemToDb()

        itemDao.update(updatedItem1)
        itemDao.update(updatedItem2)

        val allItems = itemDao.getAllItems().first()

        assertEquals(allItems[0], updatedItem1)
        assertEquals(allItems[1], updatedItem2)
    }

    @Test
    @Throws(Exception::class)
    fun updateItemWithNonExistingId_doesNothing() = runBlocking {
        addFourItemToDb()
        val nonExistingItem = Item(
            id = "doesNotExist",
            name = "Non-Existing Item",
            value = 10.0,
        ).toEntity()

        itemDao.update(nonExistingItem)

        val allItems = itemDao.getAllItems().first()
        assertEquals(allItems[0], item1)
        assertEquals(allItems[1], item2)
        assertEquals(allItems[2], item3)
        assertEquals(allItems[3], item4)
    }

    @Test
    @Throws(Exception::class)
    fun daoDeleteItems_deletesAllItemsFromDB() = runBlocking {
        addFourItemToDb()

        itemDao.clearAllItems()
        val allItems = itemDao.getAllItems().first()

        assertTrue(allItems.isEmpty())
    }

    @Test
    @Throws(Exception::class)
    fun daoDeleteItems_deletesAllSelectedItemsFromDB() = runBlocking {
        addFourItemToDb()
        val itemEntities = listOf(item1, item3, item4)
        itemDao.deleteAll(itemEntities)

        val allItems = itemDao.getAllItems().first()

        assertTrue(allItems.size == 1)
        assertEquals(allItems[0], item2)
        assertEquals(allItems[0].id, item2.id)
    }
}
