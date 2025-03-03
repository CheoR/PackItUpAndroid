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
import com.example.packitupandroid.data.model.Box
import com.example.packitupandroid.data.model.Item
import com.example.packitupandroid.data.repository.toBox
import com.example.packitupandroid.data.repository.toEntity
import com.example.packitupandroid.fake.data.boxes
import com.example.packitupandroid.fake.data.collections
import com.example.packitupandroid.fake.data.items
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

    private suspend fun insertCollections(collections: List<CollectionEntity>) {
        collectionDao.insert(collections)
    }
    private suspend fun insertBoxes(boxes: List<BoxEntity>) {
        boxDao.insert(boxes)
    }
    private suspend fun insertItems(items: List<ItemEntity>) {
        itemDao.insert(items)
    }

    private suspend fun getFirstBox() = boxDao.observeAll().first()[0] // ?.id // (boxes[0].id).first()
    private suspend fun getAllBoxes() = boxDao.observeAll().first()
    private suspend fun getAllItems() = itemDao.observeAll().first()

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
    fun boxDao_insertBoxes_AllBoxesInserted() = runBlocking {
        val boxEntities = listOf(
            Box("10", "Box1", "1", collectionId=collections[0].id).toEntity(),
            Box("20", "Box2", "1", collectionId=collections[0].id).toEntity(),
        )
        boxDao.insert(boxEntities)

        val dbBoxes = getAllBoxes()
        assertEquals(dbBoxes.size, boxes.size + boxEntities.size)
    }

    @Test
    fun boxDao_InsertItem_BoxItemCountUpdated() = runBlocking {
        val box = getFirstBox()
        val items = getAllItems().filter { it?.boxId == box?.id }

        assertEquals(box?.itemCount, items.size)

        val item2 = Item("20", "newItem2", "moo moo", boxId=box?.id).toEntity()
        itemDao.insert(listOf(item2))

        val items2 = getAllItems().filter { it?.boxId == box?.id }
        val box2 = getFirstBox()

        assertEquals(box2?.itemCount, items2.size)
    }

    @Test
    @Throws(Exception::class)
    fun boxDao_GetAllBoxes_AllBoxesReturned() = runBlocking {
        // without .first()
        // type is val aBoxItems: Flow<LiBoxItem>>
        // .first() turns flow into list.
        val dbBoxes = getAllBoxes()

        assertSameProperties(boxes.map { it.toBox() }, dbBoxes.filterNotNull())
    }

    @Test
    @Throws(Exception::class)
    fun boxDao_observeById_BoxReturned() = runBlocking {
        // return flow
        val box = boxDao.observe(boxes[3].id).first()
        // .first() - actual box
        assertEquals(boxes[3].id, box?.id)
    }

    @Test
    @Throws(Exception::class)
    fun boxDao_UpdateBoxes_BoxesUpdated() = runBlocking {
        val updatedBox = boxes[0].copy(name = "tacos", description = "updated box 1")

        boxDao.update(updatedBox)

        val dbBoxes = getAllBoxes()

        assertSameProperties(listOf(dbBoxes[0]).filterNotNull(), listOf(updatedBox.toBox()))
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

        assertSameProperties(boxes.map { it.toBox() }, dbBoxes.filterNotNull())
    }

    @Test
    @Throws(Exception::class)
    fun boxDao_clearBoxes_AllBoxesDeleted() = runBlocking {

        boxDao.clear()
        val allBoxes = getAllBoxes()

        assertTrue(allBoxes.isEmpty())
    }

    @Test
    @Throws(Exception::class)
    fun boxDao_DeleteSelectedBoxes_SelectedBoxesDeleted() = runBlocking {
        val boxEntities = listOf(boxes[0], boxes[2], boxes[3])
        val boxEntityIds = boxEntities.map { it.id }
        boxDao.delete(boxEntityIds)

        val dbBoxes = getAllBoxes()
        assertEquals(dbBoxes.size, (boxes.size - boxEntities.size))
        assertSameProperties(listOf(dbBoxes[0]).filterNotNull(), listOf(boxes[1].toBox()))
    }

    @Test
    fun boxDao_DeleteItem_BoxItemCountUpdated() = runBlocking {
        val box = getFirstBox()
        val allItems = getAllItems().filter { it?.boxId == box?.id }

        assertEquals(box?.itemCount, allItems.size)
    }

    @Test
    @Throws(Exception::class)
    fun boxDao_observeWithNonExistingId_NullReturned() = runBlocking {
        val box = boxDao.observe("doesNotExist").first()
        assertNull(box)
    }

    @Test
    @Throws(Exception::class)
    fun itemDao_UpdateItemValue_BoxValueUpdated() = runBlocking {
        // TODO: update BoxDao to automatically trigger when associated Item instance updates its value or isFragile

        val itemNewValue = 15.25
        val box1 = getFirstBox()
        val boxValueBeforeItemValueUpdate = box1?.value
        val allItemsSum1 = getAllItems().filter { it?.boxId == box1?.id }.filterNotNull().sumOf { it.value }
        val updatedItem = items[0].copy(value = itemNewValue)

        itemDao.update(updatedItem)

        val box2 = getFirstBox()
        val boxValueAfterItemValueUpdate = box2?.value
        val allItemsSum2 = getAllItems().filter { it?.boxId == box2?.id }.filterNotNull().sumOf { it.value }

        assertEquals(boxValueBeforeItemValueUpdate, allItemsSum1)
        assertEquals(boxValueAfterItemValueUpdate, allItemsSum2)
    }

    @Test
    @Throws(Exception::class)
    fun itemDao_UpdateItemIsFragileProperty_BoxIsFragilePropertyUpdated() = runBlocking {
        // TODO: update BoxDao to automatically trigger when associated Item instance updates its value or isFragile

        val itemNewValue = true
        val box1 = getFirstBox()
        val boxValueBeforeItemIsFragileUpdate = box1?.isFragile
        val anyItemIsFragile1 = getAllItems().filter { it?.boxId == box1?.id }.any { it!!.isFragile }
        val updatedItem = items[0].copy(isFragile = itemNewValue)

        itemDao.update(updatedItem)

        val box2 = getFirstBox()
        val boxValueAfterItemIsFragileUpdate = box2?.isFragile
        val anyItemIsFragile2 = getAllItems().filter { it?.boxId == box2?.id }.any { it!!.isFragile }

        assertEquals(boxValueBeforeItemIsFragileUpdate, anyItemIsFragile1)
        assertEquals(boxValueAfterItemIsFragileUpdate, anyItemIsFragile2)
    }

    @Test
    fun collectionDao_DeleteCollection_AssociatedBoxesDeleted() = runBlocking {
        val numberOfBoxesBeforeDeleteCollection = getAllBoxes().size
        val numberOfItemsBeforeDeleteCollection = getAllItems().size

        val boxes = getAllBoxes().filter { it?.collectionId == collections[0].id }
        val items = getAllItems().filter { boxes.map { box -> box?.id }.contains(it?.boxId) }

        collectionDao.delete(listOf(collections[0].id))

        val numberOfBoxesAfterDeleteCollection = getAllBoxes().size
        val numberOfItemsAfterDeleteCollection = getAllItems().size

        assertEquals(numberOfBoxesAfterDeleteCollection, numberOfBoxesBeforeDeleteCollection - boxes.size)
        assertEquals(numberOfItemsAfterDeleteCollection, numberOfItemsBeforeDeleteCollection - items.size)

        val collection = collectionDao.observe(collections[0].id).first()
        val box = boxes[0]?.let { boxDao.observe(it.id).first() }
        val item = items[0]?.id?.let { itemDao.observe(it).first() }

        assertNull(collection)
        assertNull(box)
        assertNull(item)
    }

}

