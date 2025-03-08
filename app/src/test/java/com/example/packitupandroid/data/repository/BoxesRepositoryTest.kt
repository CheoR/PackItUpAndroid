package com.example.packitupandroid.data.repository

import com.example.packitupandroid.MainCoroutineRule
import com.example.packitupandroid.data.database.dao.FakeBoxDao
import com.example.packitupandroid.data.database.dao.FakeCollectionDao
import com.example.packitupandroid.data.database.dao.FakeItemDao
import com.example.packitupandroid.data.model.Box
import com.example.packitupandroid.data.model.Item
import com.example.packitupandroid.source.local.TestDataSource
import com.example.packitupandroid.utils.Result
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.collections.first


@OptIn(ExperimentalCoroutinesApi::class)
class BoxesRepositoryTest {
    val dataSource = TestDataSource()
    val collections = dataSource.collections
    val boxes = dataSource.boxes
    val items = dataSource.items

    private lateinit var fakeCollectionDao: FakeCollectionDao
    private lateinit var fakeItemDao: FakeItemDao
    private lateinit var fakeBoxDao: FakeBoxDao

    private lateinit var collectionsRepository: OfflineCollectionsRepository
    private lateinit var boxesRepository: BoxesRepository
    private lateinit var itemsRepository: OfflineItemsRepository

    private fun observeAll() = runBlocking {
        val result = boxesRepository.observeAll().first()
        val data = (result as Result.Success).data
        val result2 = itemsRepository.observeAll().first()
        val items = (result2 as Result.Success).data
        val boxes = data.filterNotNull().map {
            val (updatedValue, updatedItemCount, updatedIsFragile) = items
                .filter { item -> item?.boxId == it.id }
                .let { filteredItems ->
                    Triple(
                        filteredItems.sumOf { item -> item?.value ?: 0.0 },
                        filteredItems.size,
                        filteredItems.any { item -> item?.isFragile == true }
                    )
                }

            it.copy(
                value = updatedValue,
                itemCount = updatedItemCount,
                isFragile = updatedIsFragile,
            )
        }

        Result.Success(boxes)
    }

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

    @get:Rule
    val coroutineRule = MainCoroutineRule()

    @Before
    fun setup() {
        fakeItemDao = FakeItemDao()
        fakeBoxDao = FakeBoxDao(fakeItemDao)
        fakeCollectionDao = FakeCollectionDao(fakeBoxDao, fakeItemDao)

        collectionsRepository = OfflineCollectionsRepository(fakeCollectionDao)
        boxesRepository = OfflineBoxesRepository(fakeBoxDao)
        itemsRepository = OfflineItemsRepository(fakeItemDao)

        runBlocking {
            val collectionEntities = collections.map { it.toEntity() }
            val boxEntities = boxes.map { it.toEntity() }
            val itemEntities = items.map { it.toEntity() }

            fakeCollectionDao.insert(collectionEntities)
            fakeBoxDao.insert(boxEntities)
            fakeItemDao.insert(itemEntities)
        }
    }

    @After
    fun tearDown() {
        runBlocking {
            fakeItemDao.clear()
            fakeBoxDao.clear()
            fakeCollectionDao.clear()
        }
    }

    @Test
    @Throws(Exception::class)
    fun boxDao_GetAllBoxes_AllBoxesReturned() = runTest {
        val boxes = observeAll().data
        // without .first()
        // type is val aBoxItems: Flow<LiBoxItem>>
        // .first() turns flow into list.
        val result = observeAll().data

        assertEquals(boxes.size, result.size)
        assertSameProperties(boxes, result)
    }

    @Test
    @Throws(Exception::class)
    fun boxDao_observeById_BoxReturned() = runTest {
        val box = fakeBoxDao.observe(boxes[3].id).first()

        assertEquals(boxes[3].id, box?.id)
    }

    @Test
    @Throws(Exception::class)
    fun boxDao_getById_BoxReturned() = runTest {
        val box = fakeBoxDao.get(boxes[3].id)
        assertSameProperties(listOf(boxes[3]), listOfNotNull(box))
    }

    @Test
    @Throws(Exception::class)
    fun boxDao_observeWithNonExistingId_NullReturned() = runTest {
        val box = fakeBoxDao.observe("doesNotExist").first()

        assertNull(box)
    }

    @Test
    @Throws(Exception::class)
    fun boxDao_insertBoxes_AllBoxesInserted() = runTest {
        val result = observeAll()

        assertEquals(result.data.size, boxes.size)
    }

    @Test
    @Throws(Exception::class)
    fun boxDao_UpdateBoxes_BoxesUpdated() = runTest {
        val boxes = observeAll().data
        val updatedBox = boxes.first().copy(name = "tacos", description = "updated box 1")

        fakeBoxDao.update(updatedBox.toEntity())

        val result = observeAll()
        val box = result.data.first()

        assertSameProperties(listOfNotNull(box), listOf(updatedBox))
    }

    @Test
    @Throws(Exception::class)
    fun boxDao_UpdateBoxWithNonExistingId_NothingHappens() = runTest {
        val boxesBeforeUpdate = observeAll().data
        val nonExistingBox = Box(
            id = "doesNotExist",
            name = "Non-Existing Box",
        ).toEntity()
        fakeBoxDao.update(nonExistingBox)

        val result = observeAll()

        assertSameProperties(boxesBeforeUpdate, result.data)
    }

    @Test
    @Throws(Exception::class)
    fun boxDao_clearBoxes_AllBoxesDeleted() = runTest {
        fakeBoxDao.clear()

        val result = observeAll()

        assertTrue(result.data.isEmpty())
    }

    @Test
    @Throws(Exception::class)
    fun boxDao_DeleteSelectedBoxes_SelectedBoxesDeletedFromDb() = runTest {
        val boxesBeforeDelete = observeAll().data
        val boxEntities = boxes.take(3)
        val boxEntityIds = boxEntities.map { it.id }

        fakeBoxDao.delete(boxEntityIds)

        val result = observeAll()

        assertEquals(result.data.size, (boxes.size - boxEntities.size))
        assertSameProperties(
            listOfNotNull(result.data.first()),
            listOf(boxesBeforeDelete[boxEntities.size])
        )
    }

    @Test
    fun boxDao_InsertItem_BoxItemCountUpdated() = runTest {
        val result = observeAll()
        val box = result.data.first()
        val items = fakeItemDao.observeAll().first().filter { it?.boxId == box.id }

        assertEquals(box.itemCount, items.size)

        val item2 = Item("20", "newItem2", "moo moo", boxId = box.id).toEntity()
        fakeItemDao.insert(listOf(item2))

        val items2 = fakeItemDao.observeAll().first().filter { it?.boxId == box.id }

        val result2 = observeAll()
        val box2 = result2.data.first()

        assertEquals(box2.itemCount, items2.size)
    }

    @Test
    fun boxDao_DeleteItem_BoxItemCountUpdated() = runTest {
        val box = observeAll().data.first()
        val result = itemsRepository.observeAll().first()
        val items = (result as Result.Success).data.filter { it?.boxId == box.id }

        assertEquals(box.itemCount, items.size)
    }

    @Test
    @Throws(Exception::class)
    fun itemDao_UpdateItemValue_BoxValueUpdated() = runTest {
        // TODO: update BoxDao to automatically trigger when associated Item instance updates its value or isFragile

        val itemNewValue = 15.25
        val box1 = observeAll().data.first()
        val boxValueBeforeItemValueUpdate = box1.value
        val result1 = itemsRepository.observeAll().first()
        val allItemsSum1 =
            (result1 as Result.Success).data.filter { it?.boxId == box1.id }.filterNotNull()
                .sumOf { it.value }
        val updatedItem = items.first().copy(value = itemNewValue)
        val updatedItemEntity = updatedItem.toEntity()

        fakeItemDao.update(updatedItemEntity)

        val box2 = observeAll().data.first()
        val boxValueAfterItemValueUpdate = box2.value
        val result2 = itemsRepository.observeAll().first()
        val allItemsSum2 =
            (result2 as Result.Success).data.filter { it?.boxId == box2.id }.filterNotNull()
                .sumOf { it.value }

        assertEquals(boxValueBeforeItemValueUpdate, allItemsSum1)
        assertEquals(boxValueAfterItemValueUpdate, allItemsSum2)
    }

    @Test
    @Throws(Exception::class)
    fun itemDao_UpdateItemIsFragileProperty_BoxIsFragilePropertyUpdated() = runTest {
        // TODO: update BoxDao to automatically trigger when associated Item instance updates its value or isFragile

        val itemNewValue = true
        val box1 = observeAll().data.first()
        val boxValueBeforeItemIsFragileUpdate = box1.isFragile
        val result1 = itemsRepository.observeAll().first()
        val anyItemIsFragile1 =
            (result1 as Result.Success).data.filterNotNull().filter { it.boxId == box1.id }
                .any { it.isFragile }
        val updatedItem = items.first().copy(isFragile = itemNewValue)
        val updatedItemEntity = updatedItem.toEntity()

        fakeItemDao.update(updatedItemEntity)

        val box2 = observeAll().data.first()
        val boxValueAfterItemIsFragileUpdate = box2.isFragile
        val result2 = itemsRepository.observeAll().first()
        val anyItemIsFragile2 =
            (result2 as Result.Success).data.filterNotNull().filter { it.boxId == box2.id }
                .any { it.isFragile }

        assertEquals(boxValueBeforeItemIsFragileUpdate, anyItemIsFragile1)
        assertEquals(boxValueAfterItemIsFragileUpdate, anyItemIsFragile2)
    }

    @Test
    fun collectionDao_DeleteCollection_AssociatedBoxesDeletedFromDb() = runTest {
        val collection = fakeCollectionDao.observeAll().first().first()
        val numberOfBoxesBeforeDeleteCollection = observeAll().data.size
        val boxesToDelete = observeAll().data.filter { it.collectionId == collection?.id }

        fakeCollectionDao.delete(listOfNotNull(collection?.id))

        val numberOfBoxesAfterDeleteCollection = observeAll().data.size

        assertEquals(
            numberOfBoxesAfterDeleteCollection,
            numberOfBoxesBeforeDeleteCollection - boxesToDelete.size
        )
    }
}
