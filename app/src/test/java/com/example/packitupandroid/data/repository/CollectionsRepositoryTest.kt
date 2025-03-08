package com.example.packitupandroid.data.repository

import com.example.packitupandroid.MainCoroutineRule
import com.example.packitupandroid.data.database.dao.FakeBoxDao
import com.example.packitupandroid.data.database.dao.FakeCollectionDao
import com.example.packitupandroid.data.database.dao.FakeItemDao
import com.example.packitupandroid.data.model.Box
import com.example.packitupandroid.data.model.Collection
import com.example.packitupandroid.source.local.TestDataSource
import com.example.packitupandroid.utils.Result
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue


@OptIn(ExperimentalCoroutinesApi::class)
class CollectionsRepositoryTest {
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
        val collectionResult = collectionsRepository.observeAll().first()
        val boxResult = boxesRepository.observeAll().first()
        val itemResult = itemsRepository.observeAll().first()

        val items = (itemResult as Result.Success).data
        val boxes = (boxResult as Result.Success).data.filterNotNull().map {
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

        val collections = (collectionResult as Result.Success).data.filterNotNull().map { collection ->
            val (totalValue, anyIsFragile, counts) = boxes
                .filter { box -> box.collectionId == collection.id }
                .let { filteredBoxes ->
                    Triple(
                        filteredBoxes.sumOf { box -> box.value },
                        filteredBoxes.any { box -> box.isFragile },
                        Pair<Int, Int>(
                            filteredBoxes.sumOf{box -> box.itemCount},
                            filteredBoxes.size,
                        )
                    )
                }

            collection.copy(
                value = totalValue,
                boxCount = counts.second,
                itemCount = counts.first,
                isFragile = anyIsFragile,
            )
        }

        Result.Success(collections)
    }

    private fun assertSameProperties(list1: List<Collection>, list2: List<Collection>) {
        for (i in list1.indices) {
            assertEquals(list1[i].id, list2[i].id)
            assertEquals(list1[i].name, list2[i].name)
            assertEquals(list1[i].description, list2[i].description)
            assertEquals(list1[i].value, list2[i].value)
            assertEquals(list1[i].isFragile, list2[i].isFragile)
            assertEquals(list1[i].itemCount, list2[i].itemCount)
            assertEquals(list1[i].boxCount, list2[i].boxCount)
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
    fun collectionsRepositoryTest_GetAllCollections_AllCollectionsReturned() = runTest {
        // without .first()
        // type is val aBoxItems: Flow<LiBoxItem>>
        // .first() turns flow into list.
        val result = observeAll()
        assertEquals(collections.size, result.data.size)
    }

    @Test
    @Throws(Exception::class)
    fun collectionsRepositoryTest_GetCollectionById_CollectionReturned() = runTest {
        // return flow
        val collection = fakeCollectionDao.get(collections[3].id)

        // .first() - actual collection
        assertEquals(collections[3].id, collection?.id)
    }

    @Test
    @Throws(Exception::class)
    fun collectionsRepositoryTest_GetCollectionWithNonExistingId_NullReturned() = runTest {
        val collection = fakeCollectionDao.get("doesNotExist")

        assertNull(collection)
    }

    @Test
    @Throws(Exception::class)
    fun collectionsRepositoryTest_insertCollections_AllCollectionsInserted() = runTest {
        val result = observeAll()

        assertEquals(collections.size, result.data.size)
    }

    @Test
    @Throws(Exception::class)
    fun collectionsRepositoryTest_UpdateCollection_CollectionUpdated() = runTest {
        val collection = observeAll().data.first().copy(name = "tacos", description = "updated collection 1")
        val collectionEntity = collection.toEntity()

        fakeCollectionDao.update(collectionEntity)

        val result = observeAll()

        assertSameProperties(listOf(result.data.first()), listOf((collection)))
    }

    @Test
    @Throws(Exception::class)
    fun collectionsRepositoryTest_UpdateCollectionWithNonExistingId_NothingHappens() = runTest {
        val collections = observeAll().data
        val nonExistingCollection = Collection(
            id = "doesNotExist",
            name = "Non-Existing Collection",
            value = 10.0,
        ).toEntity()

        fakeCollectionDao.update(nonExistingCollection)

        val result = observeAll()

        assertSameProperties(collections, result.data)
    }

    @Test
    @Throws(Exception::class)
    fun collectionsRepositoryTest_deleteCollections_AllCollectionsDeleted() = runTest {
        fakeCollectionDao.clear()

        val result = observeAll()

        assertTrue(result.data.isEmpty())
    }

    @Test
    @Throws(Exception::class)
    fun collectionsRepositoryTest_DeleteSelectedCollections_SelectedCollectionsDeleted() = runTest {
        val collections = observeAll().data
        val collectionEntityIds = collections.take(3).map { it.id }

        fakeCollectionDao.delete(collectionEntityIds)

        val result = observeAll()

        assertEquals(result.data.size, collections.size - collectionEntityIds.size)
        assertSameProperties(collections.drop(collectionEntityIds.size), result.data)
    }

    @Test
    @Throws(Exception::class)
    fun collectionsRepositoryTest_DisplayItemCountAndBoxCount_CorrectValues() = runTest {
        val collection = observeAll().data.first()
        val boxResult = boxesRepository.observeAll().first()
        val associatedBoxesIds = (boxResult as Result.Success).data.filter { it?.collectionId == collection.id }.map { it?.id }
        val itemResult = itemsRepository.observeAll().first()
        val associatedItems = (itemResult as Result.Success).data.filter { associatedBoxesIds.contains(it?.boxId) }

        assertEquals(collection.boxCount, associatedBoxesIds.size)
        assertEquals(collection.itemCount, associatedItems.size)
    }

    @Test
    fun collectionsRepositoryTest_InsertBox_BoxCountUpdated() = runTest {
        val collectionBeforeBoxInsert = observeAll().data.first()
        val box = Box("2", "newBox2", "moo moo", collectionId = collectionBeforeBoxInsert.id).toEntity()

        fakeBoxDao.insert(listOf(box))

        val collectionAfterBoxInsert = observeAll().data.first()

        assertEquals(collectionBeforeBoxInsert.boxCount + 1, collectionAfterBoxInsert.boxCount)
    }

    @Test
    fun collectionsRepositoryTest_DeleteItem_CollectionItemCountUpdated() = runTest {
        val collection = observeAll().data.first()
        val boxResult = boxesRepository.observeAll().first()
        val associatedBoxesIds = (boxResult as Result.Success).data.filter { it?.collectionId == collection.id }.map { it?.id }
        val itemResult = itemsRepository.observeAll().first()
        val associatedItemIds = (itemResult as Result.Success).data.filter { associatedBoxesIds.contains(it?.boxId) }.map { it?.id }

        assertEquals(collection.itemCount, associatedItemIds.size)

        fakeItemDao.delete(listOfNotNull(associatedItemIds.first()))

        val collection2 = observeAll().data.first()

        assertEquals(collection2.itemCount, associatedItemIds.size - 1)
    }

    @Test
    @Throws(Exception::class)
    fun collectionsRepositoryTest_UpdateItemValue_CollectionValueUpdated() = runTest {
        // TODO: update CollectionDao to automatically trigger when associated Item instance updates its value or isFragile
        val itemNewValue = 500.0

        val collection = observeAll().data.first()
        val boxResult = boxesRepository.observeAll().first()
        val associatedBoxesIds = (boxResult as Result.Success).data.filter { it?.collectionId == collection.id }.map { it?.id }
        val itemResult = itemsRepository.observeAll().first()
        val associatedItems = (itemResult as Result.Success).data.filter { associatedBoxesIds.contains(it?.boxId) }

        assertEquals(collection.value, associatedItems.sumOf { it?.value ?: 0.00 })

        val oldValue = associatedItems.first()?.value as Double
        val updatedItem = associatedItems.first()?.copy(value = itemNewValue)
        val updatedItemEntity = updatedItem?.toEntity()

        fakeItemDao.update(updatedItemEntity!!)

        val collection2 = observeAll().data.first()
        val itemResult2 = itemsRepository.observeAll().first()
        val associatedItems2 = (itemResult2 as Result.Success).data.filter { associatedBoxesIds.contains(it?.boxId) }

        assertEquals(collection.value - oldValue + itemNewValue, collection2.value)
        assertEquals(collection2.value, associatedItems2.sumOf { it?.value ?: 0.00 })
    }

    @Test
    @Throws(Exception::class)
    fun collectionsRepositoryTest_UpdateItemIsFragileProperty_CollectionIsFragilePropertyUpdated() = runTest {
        // TODO: update BoxDao to automatically trigger when associated Item instance updates its value or isFragile

        val itemNewIsFragile = true

        val collection = observeAll().data.first()
        val boxResult = boxesRepository.observeAll().first()
        val associatedBoxesIds = (boxResult as Result.Success).data.filter { it?.collectionId == collection.id }.map { it?.id }
        val itemResult = itemsRepository.observeAll().first()
        val associatedItems = (itemResult as Result.Success).data.filter { associatedBoxesIds.contains(it?.boxId) }

        assertEquals(collection.isFragile, associatedItems.filterNotNull().any { it.isFragile })

        val updatedItem = associatedItems.first()?.copy(isFragile = itemNewIsFragile)
        val updatedItemEntity = updatedItem?.toEntity()

        fakeItemDao.update(updatedItemEntity!!)

        val collection2 = observeAll().data.first()
        val itemResult2 = itemsRepository.observeAll().first()
        val associatedItems2 = (itemResult2 as Result.Success).data.filter { associatedBoxesIds.contains(it?.boxId) }

        assertEquals(collection2.isFragile, associatedItems2.filterNotNull().any { it.isFragile })
    }

    @Test
    fun collectionsRepositoryTest_DeleteCollection_AssociatedBoxesDeleted() = runTest {
        // TODO: write helper functions to get associated boxes and items to use in all tests

        val collection = observeAll().data.first()
        val boxResult = boxesRepository.observeAll().first()
        val totalInitialBoxCount = (boxResult as Result.Success).data.size
        val associatedBoxes = boxResult.data.filter { it?.collectionId == collection.id }

        assertEquals(collection.boxCount, associatedBoxes.size)

        fakeCollectionDao.delete(listOf(collection.id))

        val boxResult2 = boxesRepository.observeAll().first()

        assertEquals(totalInitialBoxCount - associatedBoxes.size, (boxResult2 as Result.Success ).data.size)
    }

    @Test
    fun collectionsRepositoryTest_DeleteCollection_AssociatedItemsDeleted() = runTest {
        val collection = observeAll().data.first()
        val itemsResult = itemsRepository.observeAll().first()
        val boxesResult = boxesRepository.observeAll().first()
        val totalInitialItemCount = (itemsResult as Result.Success).data.size
        val associatedBoxesIds = (boxesResult as Result.Success).data.filter { it?.collectionId == collection.id }.map { it?.id }
        val associatedItems = itemsResult.data.filter { associatedBoxesIds.contains(it?.boxId) }

        assertEquals(collection.itemCount, associatedItems.size)

        fakeCollectionDao.delete(listOf(collection.id))

        val itemsResult2 = itemsRepository.observeAll().first()

        assertEquals(totalInitialItemCount - associatedItems.size, (itemsResult2 as Result.Success).data.size)
    }
}