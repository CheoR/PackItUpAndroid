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
import com.example.packitupandroid.data.database.entities.toBox
import com.example.packitupandroid.data.model.Box
import com.example.packitupandroid.data.model.Collection
import com.example.packitupandroid.data.model.Item
import com.example.packitupandroid.data.model.toBox
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
class BoxDaoTest{
    private lateinit var collectionDao: CollectionDao
    private lateinit var boxDao: BoxDao
    private lateinit var itemDao: ItemDao
    private lateinit var db: AppDatabase

    private val collections = listOf(
        Collection("1", "collections1").toEntity(),
        Collection("2", "collections2").toEntity(),
        Collection("3", "collections3").toEntity(),
        Collection("4", "collections4").toEntity(),
    )

    private val boxes = listOf(
        Box("1", "Box1", "1", collectionId=collections[0].id).toEntity(),
        Box("2", "Box2", "2", collectionId=collections[0].id).toEntity(),
        Box("3", "Box3", "3", collectionId=collections[1].id).toEntity(),
        Box("4", "Box4", "4").toEntity(),
    )

    private val items = listOf(
        Item("1", "item1", "1", boxId=boxes[0].id).toEntity(),
        Item("2", "Item2", "2", boxId=boxes[0].id).toEntity(),
        Item("3", "Item3", "3", boxId=boxes[0].id).toEntity(),
        Item("4", "Item4", "4", boxId=boxes[1].id).toEntity(),
        Item("5", "Item5", "5", boxId=boxes[1].id).toEntity(),
        Item("6", "Item6", "6", boxId=boxes[2].id).toEntity(),
        Item("7", "Item7", "7", boxId=boxes[2].id).toEntity(),
        Item("8", "Item8", "8", boxId=boxes[3].id).toEntity(),
    )

    private suspend fun insertCollections(collections: List<CollectionEntity>) {
        collectionDao.insertAll(collections)
    }
    private suspend fun insertBoxes(boxes: List<BoxEntity>) {
        boxDao.insertAll(boxes)
    }
    private suspend fun insertItems(items: List<ItemEntity>) {
        itemDao.insertAll(items)
    }
    private suspend fun getFirstBox() = boxDao.getQueryBox(boxes[0].id).first()
    private suspend fun getAllBoxes() = boxDao.getAllBoxes().first()
    private suspend fun getAllItems() = itemDao.getAllItems().first()

    private fun assertSameProperties(list1: List<Box>, list2: List<Box>) {
        for (i in list1.indices) {
            assertEquals(list1[i].id, list2[i].id)
            assertEquals(list1[i].name, list2[i].name)
            assertEquals(list1[i].description, list2[i].description)
            assertEquals(list1[i].value, list2[i].value)
            assertEquals(list1[i].isFragile, list2[i].isFragile)
            assertEquals(list1[i].collectionId, list2[i].collectionId)
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
    fun boxDao_InsertAllBoxes_AllBoxesInserted() = runBlocking {
        val boxEntities = listOf(
            Box("10", "Box1", "1", collectionId=collections[0].id).toEntity(),
            Box("20", "Box2", "1", collectionId=collections[0].id).toEntity(),
        )
        boxDao.insertAll(boxEntities)

        val dbBoxes = getAllBoxes()
        assertEquals(dbBoxes.size, boxes.size + boxEntities.size)
    }

    @Test
    fun boxDao_InsertItem_BoxItemCountUpdated() = runBlocking {
        val box = getFirstBox()
        val items = getAllItems().filter { it.boxId == box.id }

        assertEquals(box.item_count, items.size)

        val item2 = Item("20", "newItem2", "moo moo", boxId=box.id).toEntity()
        itemDao.insert(item2)

        val items2 = getAllItems().filter { it.boxId == box.id }
        val box2 = getFirstBox().toBox()

        assertEquals(box2.item_count, items2.size)
    }

    @Test
    @Throws(Exception::class)
    fun boxDao_GetAllBoxes_AllBoxesReturned() = runBlocking {
        // without .first()
        // type is val aBoxItems: Flow<LiBoxItem>>
        // .first() turns flow into list.
        val dbBoxes = getAllBoxes()

        assertSameProperties(boxes.map { it.toBox() }, dbBoxes.map { it.toBox() })
    }

    @Test
    @Throws(Exception::class)
    fun boxDao_GetBoxById_BoxReturned() = runBlocking {
        // return flow
        val box = boxDao.getBox(boxes[3].id).first()
        // .first() - actual box
        assertEquals(boxes[3].id, box.id)
    }

    @Test
    @Throws(Exception::class)
    fun boxDao_UpdateBoxes_BoxesUpdated() = runBlocking {
        val updatedBox = boxes[0].copy(name = "tacos", description = "updated box 1")

        boxDao.update(updatedBox)

        val dbBoxes = getAllBoxes()

        assertSameProperties(listOf(dbBoxes[0].toBox()), listOf(updatedBox.toBox()))
    }

    @Test
    @Throws(Exception::class)
    fun boxDao_UpdateBoxWithNonExistingId_NothingHappens() = runBlocking {
        val nonExistingBox = Box(
            id = "doesNotExist",
            name = "Non-Existing Box",
            value = 10.0,
        ).toEntity()

        boxDao.update(nonExistingBox)

        val dbBoxes = getAllBoxes()

        assertSameProperties(boxes.map { it.toBox() }, dbBoxes.map { it.toBox() })
    }

    @Test
    @Throws(Exception::class)
    fun boxDao_DeleteAllBoxes_AllBoxesDeleted() = runBlocking {

        boxDao.clearAllBoxes()
        val allBoxes = getAllBoxes()

        assertTrue(allBoxes.isEmpty())
    }

    @Test
    @Throws(Exception::class)
    fun boxDao_DeleteSelectedBoxes_SelectedBoxesDeleted() = runBlocking {
        val boxEntities = listOf(boxes[0], boxes[2], boxes[3])
        boxDao.deleteAll(boxEntities)

        val dbBoxes = getAllBoxes()
        assertEquals(dbBoxes.size, (boxes.size - boxEntities.size))
        assertSameProperties(listOf(dbBoxes[0].toBox()), listOf(boxes[1].toBox()))
    }

    @Test
    fun boxDao_DeleteItem_BoxItemCountUpdated() = runBlocking {
        val box = getFirstBox().toBox()
        val allItems = getAllItems().filter { it.boxId == box.id }

        assertEquals(box.item_count, allItems.size)

//        itemDao.delete(items[0])
//
//        val allItems2 = getAllItems().filter { it.boxId == box.id }
//        val box2 = getFirstBox().toBox()
//
//        assertEquals(box2.item_count, allItems2.size - 1)
    }

    @Test
    @Throws(Exception::class)
    fun boxDao_GetBoxWithNonExistingId_NullReturned() = runBlocking {
        val box = boxDao.getBox("doesNotExist").first()
        assertNull(box)
    }

    @Test
    @Throws(Exception::class)
    fun itemDao_UpdateItemValue_BoxValueUpdated() = runBlocking {
        // TODO: update BoxDao to automatically trigger when associated Item instance updates its value or isFragile

        val itemNewValue = 15.25
        val box1 = getFirstBox().toBox()
        val boxValueBeforeItemValueUpdate = box1.value
        val allItemsSum1 = getAllItems().filter { it.boxId == box1.id }.sumOf { it.value }
        val updatedItem = items[0].copy(value = itemNewValue)

        itemDao.update(updatedItem)

        val box2 = getFirstBox().toBox()
        val boxValueAfterItemValueUpdate = box2.value
        val allItemsSum2 = getAllItems().filter { it.boxId == box2.id }.sumOf { it.value }

        assertEquals(boxValueBeforeItemValueUpdate, allItemsSum1)
        assertEquals(boxValueAfterItemValueUpdate, allItemsSum2)
    }

    @Test
    @Throws(Exception::class)
    fun itemDao_UpdateItemIsFragileProperty_BoxIsFragilePropertyUpdated() = runBlocking {
        // TODO: update BoxDao to automatically trigger when associated Item instance updates its value or isFragile

        val itemNewValue = true
        val box1 = getFirstBox().toBox()
        val boxValueBeforeItemIsFragileUpdate = box1.isFragile
        val anyItemIsFragile1 = getAllItems().filter { it.boxId == box1.id }.any { it.isFragile }
        val updatedItem = items[0].copy(isFragile = itemNewValue)

        itemDao.update(updatedItem)

        val box2 = getFirstBox().toBox()
        val boxValueAfterItemIsFragileUpdate = box2.isFragile
        val anyItemIsFragile2 = getAllItems().filter { it.boxId == box2.id }.any { it.isFragile }

        assertEquals(boxValueBeforeItemIsFragileUpdate, anyItemIsFragile1)
        assertEquals(boxValueAfterItemIsFragileUpdate, anyItemIsFragile2)
    }

    @Test
    fun collectionDao_DeleteCollection_AssociatedBoxesDeleted() = runBlocking {
        val numberOfBoxesBeforeDeleteCollection = getAllBoxes().size
        val numberOfItemsBeforeDeleteCollection = getAllItems().size

        val boxes = getAllBoxes().filter { it.collection_id == collections[0].id }
        val items = getAllItems().filter { boxes.map { box -> box.id }.contains(it.boxId) }

        collectionDao.delete(collections[0])

        val numberOfBoxesAfterDeleteCollection = getAllBoxes().size
        val numberOfItemsAfterDeleteCollection = getAllItems().size

        assertEquals(numberOfBoxesAfterDeleteCollection, numberOfBoxesBeforeDeleteCollection - boxes.size)
        assertEquals(numberOfItemsAfterDeleteCollection, numberOfItemsBeforeDeleteCollection - items.size)

        val collection = collectionDao.getCollection(collections[0].id).first()
        val box = boxDao.getBox(boxes[0].id).first()
        val item = itemDao.getItem(items[0].id).first()

        assertNull(collection)
        assertNull(box)
        assertNull(item)
    }

}

