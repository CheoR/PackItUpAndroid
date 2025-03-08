package com.example.packitupandroid.data.database.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.packitupandroid.data.database.AppDatabase
import com.example.packitupandroid.data.model.Box
import com.example.packitupandroid.data.model.Item
import com.example.packitupandroid.data.repository.toEntity
import com.example.packitupandroid.source.local.boxes
import com.example.packitupandroid.source.local.collections
import com.example.packitupandroid.source.local.items
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
class BoxDaoTest{
    private lateinit var collectionDao: CollectionDao
    private lateinit var boxDao: BoxDao
    private lateinit var itemDao: ItemDao
    private lateinit var db: AppDatabase

    private suspend fun getFirstBox() = boxDao.observeAll().first().first()
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
            val collectionEntities = collections.map { it.toEntity() }
            val boxEntities = boxes.map { it.toEntity() }
            val itemEntities = items.map { it.toEntity() }

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
    fun boxDao_GetAllBoxes_AllBoxesReturned() = runTest {
        // without .first()
        // type is val aBoxItems: Flow<LiBoxItem>>
        // .first() turns flow into list.
        val dbBoxes = getAllBoxes()

        assertSameProperties(boxes, dbBoxes.filterNotNull())
    }

    @Test
    @Throws(Exception::class)
    fun boxDao_observeById_BoxReturned() = runTest {
        // return flow
        val box = boxDao.observe(boxes[3].id).first()
        // .first() - actual box
        assertEquals(boxes[3].id, box?.id)
    }

    @Test
    @Throws(Exception::class)
    fun boxDao_getById_BoxReturned() = runTest {
        val box = boxDao.get(boxes[3].id)

        assertSameProperties(listOf(boxes[3]), listOfNotNull(box))
    }

    @Test
    @Throws(Exception::class)
    fun boxDao_observeWithNonExistingId_NullReturned() = runTest {
        val box = boxDao.observe("doesNotExist").first()
        assertNull(box)
    }

    @Test
    @Throws(Exception::class)
    fun boxDao_insertBoxes_AllBoxesInserted() = runTest {
        val dbBoxes = getAllBoxes()

        assertEquals(dbBoxes.size, boxes.size)
    }

    @Test
    @Throws(Exception::class)
    fun boxDao_UpdateBoxes_BoxesUpdated() = runTest {
        val updatedBox = boxes.first().copy(name = "tacos", description = "updated box 1")

        boxDao.update(updatedBox.toEntity())

        val dbBoxes = getAllBoxes()

        assertSameProperties(listOfNotNull(dbBoxes.first()), listOf(updatedBox))
    }

    @Test
    @Throws(Exception::class)
    fun boxDao_UpdateBoxWithNonExistingId_NothingHappens() = runTest {
        val nonExistingBox = Box(
            id = "doesNotExist",
            name = "Non-Existing Box",
            value = 10.0,
        ).toEntity()

        boxDao.update(nonExistingBox)

        val dbBoxes = getAllBoxes()

        assertSameProperties(boxes, dbBoxes.filterNotNull())
    }

    @Test
    @Throws(Exception::class)
    fun boxDao_clearBoxes_AllBoxesDeleted() = runTest {

        boxDao.clear()
        val allBoxes = getAllBoxes()

        assertTrue(allBoxes.isEmpty())
    }

    @Test
    @Throws(Exception::class)
    fun boxDao_DeleteSelectedBoxes_SelectedBoxesDeleted() = runTest {
        val boxEntities = boxes.take(3)
        val boxEntityIds = boxEntities.map { it.id }

        boxDao.delete(boxEntityIds)

        val dbBoxes = getAllBoxes()

        assertEquals(dbBoxes.size, (boxes.size - boxEntities.size))
        assertSameProperties(listOfNotNull(dbBoxes.first()), listOf(boxes[boxEntities.size]))
    }

    @Test
    fun boxDao_InsertItem_BoxItemCountUpdated() = runTest {
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
    fun boxDao_DeleteItem_BoxItemCountUpdated() = runTest {
        val box = getFirstBox()
        val allItems = getAllItems().filter { it?.boxId == box?.id }

        assertEquals(box?.itemCount, allItems.size)
    }

    @Test
    @Throws(Exception::class)
    fun itemDao_UpdateItemValue_BoxValueUpdated() = runTest {
        // TODO: update BoxDao to automatically trigger when associated Item instance updates its value or isFragile

        val itemNewValue = 15.25
        val box1 = getFirstBox()
        val boxValueBeforeItemValueUpdate = box1?.value
        val allItemsSum1 = getAllItems().filter { it?.boxId == box1?.id }.filterNotNull().sumOf { it.value }
        val updatedItem = items.first().copy(value = itemNewValue)
        val updatedItemEntity = updatedItem.toEntity()

        itemDao.update(updatedItemEntity)

        val box2 = getFirstBox()
        val boxValueAfterItemValueUpdate = box2?.value
        val allItemsSum2 = getAllItems().filter { it?.boxId == box2?.id }.filterNotNull().sumOf { it.value }

        assertEquals(boxValueBeforeItemValueUpdate, allItemsSum1)
        assertEquals(boxValueAfterItemValueUpdate, allItemsSum2)
    }

    @Test
    @Throws(Exception::class)
    fun itemDao_UpdateItemIsFragileProperty_BoxIsFragilePropertyUpdated() = runTest {
        // TODO: update BoxDao to automatically trigger when associated Item instance updates its value or isFragile

        val itemNewValue = true
        val box1 = getFirstBox()
        val boxValueBeforeItemIsFragileUpdate = box1?.isFragile
        val anyItemIsFragile1 = getAllItems().filter { it?.boxId == box1?.id }.any { it!!.isFragile }
        val updatedItem = items.first().copy(isFragile = itemNewValue)
        val updatedItemEntity = updatedItem.toEntity()

        itemDao.update(updatedItemEntity)

        val box2 = getFirstBox()
        val boxValueAfterItemIsFragileUpdate = box2?.isFragile
        val anyItemIsFragile2 = getAllItems().filter { it?.boxId == box2?.id }.any { it!!.isFragile }

        assertEquals(boxValueBeforeItemIsFragileUpdate, anyItemIsFragile1)
        assertEquals(boxValueAfterItemIsFragileUpdate, anyItemIsFragile2)
    }

    @Test
    fun collectionDao_DeleteCollection_AssociatedBoxesDeleted() = runTest {
        val numberOfBoxesBeforeDeleteCollection = getAllBoxes().size
        val numberOfItemsBeforeDeleteCollection = getAllItems().size

        val boxes = getAllBoxes().filter { it?.collectionId == collections.first().id }
        val items = getAllItems().filter { boxes.map { box -> box?.id }.contains(it?.boxId) }

        collectionDao.delete(listOf(collections.first().id))

        val numberOfBoxesAfterDeleteCollection = getAllBoxes().size
        val numberOfItemsAfterDeleteCollection = getAllItems().size

        assertEquals(numberOfBoxesAfterDeleteCollection, numberOfBoxesBeforeDeleteCollection - boxes.size)
        assertEquals(numberOfItemsAfterDeleteCollection, numberOfItemsBeforeDeleteCollection - items.size)

        val collection = collectionDao.observe(collections.first().id).first()
        val box = boxes.first()?.let { boxDao.observe(it.id).first() }
        val item = items.first()?.id?.let { itemDao.observe(it).first() }

        assertNull(collection)
        assertNull(box)
        assertNull(item)
    }
}
