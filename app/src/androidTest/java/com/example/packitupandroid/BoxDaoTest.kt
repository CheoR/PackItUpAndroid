package com.example.packitupandroid

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.packitupandroid.data.database.AppDatabase
import com.example.packitupandroid.data.database.dao.BoxDao
import com.example.packitupandroid.data.database.dao.CollectionDao
import com.example.packitupandroid.data.database.dao.ItemDao
import com.example.packitupandroid.data.database.entities.toBox
import com.example.packitupandroid.data.model.Box
import com.example.packitupandroid.data.model.Collection
import com.example.packitupandroid.data.model.Item
import com.example.packitupandroid.data.model.toBox
import com.example.packitupandroid.data.model.toEntity
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
class BoxDaoTest{
    private lateinit var collectionDao: CollectionDao
    private lateinit var boxDao: BoxDao
    private lateinit var itemDao: ItemDao
    private lateinit var db: AppDatabase

    private var collection1 = Collection("1", "Collection1").toEntity()
    private var collection2 = Collection("2", "Collection2").toEntity()

    private var box1 = Box("1", "Box1", "1", collectionId=collection1.id).toEntity()
    private var box2 = Box("2", "Box2", "1", collectionId=collection1.id).toEntity()
    private var box3 = Box("3", "Box3", "2", collectionId=collection2.id).toEntity()
    private var box4 = Box("4", "Box4", "2").toEntity()

    private var item1 = Item("1", "Item1", "1", boxId=box1.id).toEntity()
    private var item2 = Item("2", "Item2", "1", boxId=box1.id).toEntity()
    private var item3 = Item("3", "Item3", "2", boxId=box1.id).toEntity()
    private var item4 = Item("4", "Item4", "2", boxId=box2.id).toEntity()
    private var item5 = Item("5", "Item5", "3", boxId=box2.id).toEntity()
    private var item6 = Item("6", "Item6", "3", boxId=box3.id).toEntity()
    private var item7 = Item("7", "Item7", "4", boxId=box3.id).toEntity()
    private var item8 = Item("8", "Item8", "4", boxId=box4.id).toEntity()

    private suspend fun addTwoCollectionToDb(){
        collectionDao.insert(collection1)
        collectionDao.insert(collection2)
    }

    private suspend fun addFourBoxToDb(){
        boxDao.insert(box1)
        boxDao.insert(box2)
        boxDao.insert(box3)
        boxDao.insert(box4)
    }

    private suspend fun addOneItemToDb(){
        itemDao.insert(item1)
    }

    private suspend fun addSomeItemToDb(){
        addOneItemToDb()
        itemDao.insert(item2)
        itemDao.insert(item3)
        itemDao.insert(item4)
        itemDao.insert(item5)
    }

    private suspend fun addAllItemToDb(){
        addSomeItemToDb()
        itemDao.insert(item6)
        itemDao.insert(item7)
        itemDao.insert(item8)
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
        collectionDao = db.collectionDao()

        runBlocking {
            addTwoCollectionToDb()
            addFourBoxToDb()
        }
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun boxDao_InsertFourBoxes_Success() = runBlocking {
        addFourBoxToDb()
        val allBoxes = boxDao.getAllBoxes().first()
        assert(allBoxes.size == 4)
    }

    @Test
    @Throws(Exception::class)
    fun boxDao_InsertAllBoxes_AllBoxesInserted() = runBlocking {
        val boxEntities = listOf(box1, box2, box3, box4)
        boxDao.insertAll(boxEntities)

        val allBoxes = boxDao.getAllBoxes().first()
        assertEquals(boxEntities.size, allBoxes.size)
    }

    @Test
    fun boxDao_InsertItem_BoxItemCountUpdated() = runBlocking {
        addFourBoxToDb()
        addOneItemToDb()

        val allItems = itemDao.getAllItems().first().filter { it.boxId == box1.id }
        val box = boxDao.getBox(box1.id).first().toBox()

        assertEquals(box.item_count, allItems.size)

        val item2 = Item("2", "newItem2", "moo moo", boxId=box1.id).toEntity()
        itemDao.insert(item2)

        val allItems2 = itemDao.getAllItems().first().filter { it.boxId == box1.id }
        val box2 = boxDao.getBox(box1.id).first().toBox()

        assertEquals(box2.item_count, allItems2.size)
    }

    @Test
    @Throws(Exception::class)
    fun boxDao_GetAllBoxes_AllBoxesReturned() = runBlocking {
        addFourBoxToDb()
        // without .first()
        // type is val aBoxItems: Flow<LiBoxItem>>
        // .first() turns flow into list.
        val allBoxes = boxDao.getAllBoxes().first().map { it.toBox().toEntity() }

        assertEquals(allBoxes[0].id, box1.id)
        assertEquals(allBoxes[1].id, box2.id)
        assertEquals(allBoxes[2].id, box3.id)
        assertEquals(allBoxes[3].id, box4.id)
    }

    @Test
    @Throws(Exception::class)
    fun boxDao_GetBoxById_BoxReturned() = runBlocking {
        addFourBoxToDb()
        // return flow
        val box = boxDao.getBox(box4.id).first()
        // .first() - actual box
        assertEquals(box4.id, box.id)
    }

    @Test
    @Throws(Exception::class)
    fun boxDao_UpdateBoxes_BoxesUpdated() = runBlocking {
        addFourBoxToDb()
        val updatedBox1 = box1.copy(name = "tacos", description = "updated box 1")
        val updatedBox2 = box2.copy(name = "tacos", description = "updated box 2")

        boxDao.update(updatedBox1)
        boxDao.update(updatedBox2)

        val allBoxes = boxDao.getAllBoxes().first().map { it.toBox().toEntity() }

        assertEquals(allBoxes[0].id, updatedBox1.id)
        assertEquals(allBoxes[0].name, updatedBox1.name)
        assertEquals(allBoxes[0].description, updatedBox1.description)
        assertEquals(allBoxes[0].collectionId, updatedBox1.collectionId)

        assertEquals(allBoxes[1].id, updatedBox2.id)
        assertEquals(allBoxes[1].name, updatedBox2.name)
        assertEquals(allBoxes[1].description, updatedBox2.description)
        assertEquals(allBoxes[1].collectionId, updatedBox2.collectionId)
    }

    @Test
    @Throws(Exception::class)
    fun boxDao_UpdateBoxWithNonExistingId_NothingHappens() = runBlocking {
        addFourBoxToDb()
        val nonExistingBox = Box(
            id = "doesNotExist",
            name = "Non-Existing Box",
            value = 10.0,
        ).toEntity()

        boxDao.update(nonExistingBox)

        val allBoxes = boxDao.getAllBoxes().first().map { it.toBox().toEntity() }

        assertEquals(allBoxes[0].id, box1.id)
        assertEquals(allBoxes[0].name, box1.name)
        assertEquals(allBoxes[0].description, box1.description)
        assertEquals(allBoxes[0].collectionId, box1.collectionId)

        assertEquals(allBoxes[1].id, box2.id)
        assertEquals(allBoxes[1].name, box2.name)
        assertEquals(allBoxes[1].description, box2.description)
        assertEquals(allBoxes[1].collectionId, box2.collectionId)

        assertEquals(allBoxes[2].id, box3.id)
        assertEquals(allBoxes[2].name, box3.name)
        assertEquals(allBoxes[2].description, box3.description)
        assertEquals(allBoxes[2].collectionId, box3.collectionId)

        assertEquals(allBoxes[3].id, box4.id)
        assertEquals(allBoxes[3].name, box4.name)
        assertEquals(allBoxes[3].description, box4.description)
        assertEquals(allBoxes[3].collectionId, box4.collectionId)
    }

    @Test
    @Throws(Exception::class)
    fun boxDao_DeleteAllBoxes_AllBoxesDeleted() = runBlocking {
        addFourBoxToDb()

        boxDao.clearAllBoxes()
        val allBoxes = boxDao.getAllBoxes().first()

        assertTrue(allBoxes.isEmpty())
    }

    @Test
    @Throws(Exception::class)
    fun boxDao_DeleteSelectedBoxes_SelectedBoxesDeleted() = runBlocking {
        addFourBoxToDb()
        val boxEntities = listOf(box1, box3, box4)
        boxDao.deleteAll(boxEntities)

        val allBoxes = boxDao.getAllBoxes().first().map { it.toBox().toEntity() }

        assertTrue(allBoxes.size == 1)
        assertEquals(allBoxes[0].id, box2.id)
        assertEquals(allBoxes[0].name, box2.name)
        assertEquals(allBoxes[0].description, box2.description)
        assertEquals(allBoxes[0].collectionId, box2.collectionId)
    }

    @Test
    fun boxDao_DeleteItem_BoxItemCountUpdated() = runBlocking {
        
        val allItems = itemDao.getAllItems().first().filter { it.boxId == box1.id }
        val box = boxDao.getBox(box1.id).first().toBox()

        assertEquals(box.item_count, allItems.size)

        itemDao.delete(item1)

        val allItems2 = itemDao.getAllItems().first().filter { it.boxId == box1.id }
        val box2 = boxDao.getBox(box1.id).first().toBox()

        assertEquals(box2.item_count, allItems2.size)
    }

    @Test
    @Throws(Exception::class)
    fun boxDao_GetBoxWithNonExistingId_NullReturned() = runBlocking {
        addFourBoxToDb()
        val box = boxDao.getBox("doesNotExist").first()
        assertNull(box)
    }

    @Test
    @Throws(Exception::class)
    fun itemDao_UpdateItemValue_BoxValueUpdated() = runBlocking {
        addFourBoxToDb()
        addAllItemToDb()

        val itemNewValue = 15.25

        val boxValueBeforeItemValueUpdate = boxDao.getBox(box1.id).first().toBox()
        val updatedItem = item1.copy(value = itemNewValue)

        itemDao.update(updatedItem)

        val boxValueAfterItemValueUpdate = boxDao.getBox(box1.id).first().toBox()
        // TODO: update BoxDao to automatically trigger when associated Item instance updates its value or isFragile
//        assertNotEquals(boxValueBeforeItemValueUpdate.value, boxValueAfterItemValueUpdate.value)
//        assertEquals(boxValueBeforeItemValueUpdate.value, boxValueBeforeItemValueUpdate.value + itemNewValue)
    }
//
    @Test
    @Throws(Exception::class)
    fun itemDao_UpdateItemIsFragileProperty_BoxIsFragilePropertyUpdated() = runBlocking {
        addFourBoxToDb()
        addAllItemToDb()

        val isBoxFragile1 = boxDao.getBox(box1.id).first().toBox().isFragile

        assertEquals(isBoxFragile1, false)

        val updatedItem1 = item1.copy(isFragile = true)
        itemDao.update(updatedItem1)

        val isBoxFragile2 = boxDao.getBox(box1.id).first().toBox().isFragile
    // TODO: update BoxDao to automatically trigger when associated Item instance updates its value or isFragile
//        assertEquals(isBoxFragile2, true)
    }

    @Test
    fun collectionDao_DeleteCollection_AssociatedBoxesDeleted() = runBlocking {
        addFourBoxToDb()

        val numberOfBoxesBeforeDeleteCollection = boxDao.getAllBoxes().first().size

        collectionDao.delete(collection1)

        val numberOfBoxesAfterDeleteCollection = boxDao.getAllBoxes().first().size

        assertNotSame(numberOfBoxesAfterDeleteCollection, numberOfBoxesBeforeDeleteCollection)
        assertEquals(numberOfBoxesAfterDeleteCollection, numberOfBoxesBeforeDeleteCollection - 2)

        val item1 = boxDao.getBox("1").first()
        val item2 = boxDao.getBox("2").first()

        assertNull(item1)
        assertNull(item2)
    }

}

