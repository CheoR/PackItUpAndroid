package com.example.packitupandroid.data.repository

import com.example.packitupandroid.MainCoroutineRule
import com.example.packitupandroid.data.database.dao.FakeBoxDao
import com.example.packitupandroid.data.database.dao.FakeCollectionDao
import com.example.packitupandroid.data.database.dao.FakeItemDao
import com.example.packitupandroid.data.model.Item
import com.example.packitupandroid.source.local.TestDataSource
import com.example.packitupandroid.utils.Result
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
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


@OptIn(ExperimentalCoroutinesApi::class)
class ItemsRepositoryTest {
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

    private suspend fun observeAll() = itemsRepository.observeAll().first()

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
    fun testItemsRepository_observeAll_AllItemsReturnedFromDB() = runTest {
        val dbItems = observeAll()

        assert(dbItems is Result.Success)
        assertEquals((dbItems as Result.Success).data.size, items.size)
    }

    @Test
    fun testItemsRepository_getById_ItemReturnedFromDB() = runTest {
        val item = itemsRepository.get(items.last().id)

        assert(item is Result.Success)
        assertNotNull((item as Result.Success).data)

        if ((item).data != null) {
            assertSameProperties(listOf((item).data), listOf(items.last()))
        }
    }

    @Test
    fun testItemsRepository_getWithNonExistingId_NullReturned() = runTest {
        // TODO: make this throw error instead of null or keep it i don't know, look into it.
        // cheesecake sounds pretty good right now
        val item = itemsRepository.get("doesNotExist")
        assert(item is Result.Success)
        assertNull((item as Result.Success).data)
    }

    @Test
    fun testItemsRepository_insertItem() = runTest {
        val item = Item(name = "Test Item")
        val result = itemsRepository.insert(listOf(item))

        assert(result is Result.Success)

        val items = observeAll()

        assert((items as Result.Success).data.contains(item))
    }

    @Test
    fun testItemsRepository_UpdateItems_ItemsUpdatedInDB() = runTest {
        val updatedItem = items.first().copy(
            name = "tacos",
            description = "updated item 1",
            value = 25.0,
        )
        itemsRepository.update(updatedItem)

        val dbItems = observeAll()

        assert(dbItems is Result.Success)
        assertSameProperties(listOf((dbItems as Result.Success).data.first()!!), listOf(updatedItem))
    }

    @Test
    @Throws(Exception::class)
    fun testItemsRepository_UpdateItemWithNonExistingId_NoChangesInDB() = runTest {
        val nonExistingItem = Item(
            id = "doesNotExist",
            name = "Non-Existing Item",
            value = 10.0,
        )
        itemsRepository.update(nonExistingItem)

        val dbItems = observeAll()

        assert(dbItems is Result.Success)
        assertSameProperties((dbItems as Result.Success).data.filterNotNull(),items)
    }

    @Test
    @Throws(Exception::class)
    fun testItemsRepository_DeleteItems_AllItemsDeletedFromDB() = runTest {
        itemsRepository.clear()

        val dbItems = observeAll()

        assertTrue((dbItems as Result.Success).data.isEmpty())
    }

    @Test
    @Throws(Exception::class)
    fun testItemsRepository_DeleteSelectedItems_SelectedItemsDeletedFromDB() = runTest {
        val itemEntityIds = items.take(3).map { it.id }
        itemsRepository.delete(itemEntityIds)

        val dbItems = observeAll()

        assert(dbItems is Result.Success)
        val _items = (dbItems as Result.Success).data

        assertEquals(_items.size, items.size - itemEntityIds.size)
        assertSameProperties(listOf(items.getOrNull(itemEntityIds.size)!!), listOf(_items.first()!!))
    }

    @Test
    fun testItemsRepository_DeleteBox_AssociatedItemsDeletedFromDB() = runTest {
        val result = observeAll()
        assert(result is Result.Success)

        val itemsToDelete = items.filter { it.boxId == boxes.first().id }
        val numberOfItemsBeforeDeleteBox = (result as Result.Success).data.size

        fakeBoxDao.cascadeDelete(listOf(boxes.first().id))

        val result2 = observeAll()
        assert(result2 is Result.Success)

        val numberOfItemsAfterDeleteBox = (result2 as Result.Success).data.size

        assertEquals(numberOfItemsBeforeDeleteBox - itemsToDelete.size, numberOfItemsAfterDeleteBox)

        val item = itemsRepository.get(items.first().id)

        assert(item is Result.Success)
        assertNull((item as Result.Success).data)
    }

    @Test
    fun testItemsRepository_DeleteCollection_AssociatedItemsDeletedFromDB() = runTest {
        val result = observeAll()
        val collections = collectionsRepository.observeAll().first()
        val collectionToDelete = (collections as Result.Success).data.first()

        assert(result is Result.Success)

        val numberOfItemsBeforeDeleteCollection = (result as Result.Success).data.size
        val boxesToDeleteIds = boxes.filter { it.collectionId == collectionToDelete?.id }.map { it.id }
        val itemsToDeleteIds = items.filter { it.boxId in boxesToDeleteIds }.map { it.id }

        fakeCollectionDao.cascadeDelete(listOfNotNull(collectionToDelete?.id))

        val result2 = observeAll()
        assert(result2 is Result.Success)

        val numberOfItemsAfterDeleteCollection = (result2 as Result.Success).data.size

        assertEquals(numberOfItemsBeforeDeleteCollection - itemsToDeleteIds.size, numberOfItemsAfterDeleteCollection)
    }
}