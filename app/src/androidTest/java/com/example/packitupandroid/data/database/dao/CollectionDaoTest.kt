package com.example.packitupandroid.data.database.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.packitupandroid.data.database.AppDatabase
import com.example.packitupandroid.data.model.Box
import com.example.packitupandroid.data.model.Collection
import com.example.packitupandroid.data.repository.toEntity
import com.example.packitupandroid.source.local.TestDataSource
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
class CollectionDaoTest {
    private val collections = TestDataSource().collections
    private val boxes = TestDataSource().boxes
    private val items = TestDataSource().items

    private lateinit var collectionDao: CollectionDao
    private lateinit var boxDao: BoxDao
    private lateinit var itemDao: ItemDao
    private lateinit var db: AppDatabase

    private suspend fun getAllCollections() = collectionDao.observeAll().first()
    private suspend fun getAllBoxes() = boxDao.observeAll().first()
    private suspend fun getAllItems() = itemDao.observeAll().first()
    private suspend fun getFirstCollection() = collectionDao.observeAll().first().first()
    private suspend fun getAllFirstCollectionBoxes() = getAllBoxes().filter { it?.collectionId == collections.first().id }

    private fun assertSameProperties(list1: List<Collection>, list2: List<Collection>) {
        for (i in list1.indices) {
            assertEquals(list1[i].id, list2[i].id)
            assertEquals(list1[i].name, list2[i].name)
            assertEquals(list1[i].description, list2[i].description)
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
            itemDao.clear()
            boxDao.clear()
            collectionDao.clear()

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
    fun collectionDao_GetAllCollections_AllCollectionsReturned() = runTest {
        // without .first()
        // type is val aBoxItems: Flow<LiBoxItem>>
        // .first() turns flow into list.
        val allCollections = getAllCollections()
        assertSameProperties(collections, allCollections.filterNotNull() )
    }

    @Test
    @Throws(Exception::class)
    fun collectionDao_GetCollectionById_CollectionReturned() = runTest {
        // return flow
        val collection = collectionDao.get(collections[3].id)

        // .first() - actual collection
        assertSameProperties(listOf(collections[3]), listOf((collection!!)))
    }

    @Test
    @Throws(Exception::class)
    fun collectionDao_GetCollectionWithNonExistingId_NullReturned() = runTest {
        val collection = collectionDao.get("doesNotExist")
        assertNull(collection)
    }

    @Test
    @Throws(Exception::class)
    fun collectionDao_insertCollections_AllCollectionsInserted() = runTest {
        val dbCollections = getAllCollections()

        assertEquals(collections.size, dbCollections.size)
    }

    @Test
    @Throws(Exception::class)
    fun collectionDao_UpdateCollection_CollectionUpdated() = runTest {
        val collection = collections.first().copy(name = "tacos", description = "updated collection 1")
        val collectionEntity = collection.toEntity()

        collectionDao.update(collectionEntity)

        val allCollections = getAllCollections()
        assertSameProperties(listOf(allCollections.first()!!), listOf((collection)))
    }

    @Test
    @Throws(Exception::class)
    fun collectionDao_UpdateCollectionWithNonExistingId_NothingHappens() = runTest {
        val nonExistingCollection = Collection(
            id = "doesNotExist",
            name = "Non-Existing Collection",
            value = 10.0,
        ).toEntity()

        collectionDao.update(nonExistingCollection)

        val collectionsAfterUpdate = getAllCollections()

        assertSameProperties(collections, collectionsAfterUpdate.filterNotNull())
    }

    @Test
    @Throws(Exception::class)
    fun collectionDao_deleteCollections_AllCollectionsDeleted() = runTest {
        collectionDao.clear()
        val allCollections = getAllCollections()

        assertTrue(allCollections.isEmpty())
    }

    @Test
    @Throws(Exception::class)
    fun collectionDao_DeleteSelectedCollections_SelectedCollectionsDeleted() = runTest {
        val collectionEntityIds = collections.take(3).map { it.id }
        collectionDao.delete(collectionEntityIds)

        val collectionsAfterDelete = getAllCollections()

        assertEquals(collectionsAfterDelete.size, collections.size - collectionEntityIds.size)
        assertSameProperties(collectionsAfterDelete.filterNotNull(), collectionsAfterDelete.filterNotNull())
    }

    @Test
    @Throws(Exception::class)
    fun collectionDao_DisplayItemCountAndBoxCount_CorrectValues() = runTest {
        val boxes = getAllFirstCollectionBoxes()
        val boxIds = boxes.map { it?.id }
        val items = getAllItems().filter { boxIds.contains(it?.boxId) }
        val collection = getFirstCollection()

        assertEquals(collection?.boxCount, boxes.size)
        assertEquals(collection?.itemCount, items.size)
    }

    @Test
    fun collectionDao_InsertBox_BoxCountUpdated() = runTest {
        val boxes = getAllFirstCollectionBoxes()
        val collection = getFirstCollection()

        assertEquals(collection?.boxCount, boxes.size)

        val box2 = Box("2", "newBox2", "moo moo", collectionId = collections[0].id).toEntity()
        boxDao.insert(listOf(box2))

        val boxes2 = getAllFirstCollectionBoxes()
        val collection2 = getFirstCollection()

        assertEquals(collection2?.boxCount, boxes2.size)
    }

    @Test
    fun collectionDao_DeleteItem_CollectionItemCountUpdated() = runTest {
        val boxIds = getAllFirstCollectionBoxes().map { it?.id }
        val allItems = getAllItems().filter { boxIds.contains(it?.boxId) }
        val collection = getFirstCollection()

        assertEquals(collection?.itemCount, allItems.size)

        itemDao.delete(listOf(items.first().id))

        val boxIds2 = getAllFirstCollectionBoxes().map { it?.id }
        val allItems2 = getAllItems().filter { boxIds2.contains(it?.boxId) }
        val collection2 = getFirstCollection()

        assertEquals(collection2?.itemCount, allItems2.size)
    }

    @Test
    @Throws(Exception::class)
    fun collectionDao_UpdateItemValue_CollectionValueUpdated() = runTest {
        // TODO: update CollectionDao to automatically trigger when associated Item instance updates its value or isFragile

        val itemNewValue = 500.0

        val allBoxes = getAllFirstCollectionBoxes()
        val allItemsSum1 = getAllItems().filter { allBoxes.map { box -> box?.id }.contains(it?.boxId) }.filterNotNull().sumOf { it.value }
        val collection = getFirstCollection()

        assertEquals(allItemsSum1, collection?.value)

        val updatedItem = items.first().copy(value = itemNewValue)
        val updatedItemEntity = updatedItem.toEntity()

        itemDao.update(updatedItemEntity)

        val allBoxes2 = getAllFirstCollectionBoxes()
        val allItemsSum2 = getAllItems().filter { allBoxes2.map { box -> box?.id }.contains(it?.boxId) }.filterNotNull().sumOf { it.value }
        val collection2 = getFirstCollection()

        assertEquals(allItemsSum2, collection2?.value)
    }

    @Test
    @Throws(Exception::class)
    fun collectionDao_UpdateItemIsFragileProperty_CollectionIsFragilePropertyUpdated() = runTest {
        // TODO: update BoxDao to automatically trigger when associated Item instance updates its value or isFragile

        val collection = getFirstCollection()
        val associatedBoxes = getAllFirstCollectionBoxes()
        val associatedBoxIds = associatedBoxes.map { it?.id }
        val items = getAllItems().filter { associatedBoxIds.contains(it?.boxId) }

        assertTrue(items.any { it?.isFragile == true })
        assertEquals(true, collection?.isFragile)

        val updatedItemEntities = items
            .mapNotNull { item -> item?.copy(isFragile = false) }
            .map { it.toEntity() }
        updatedItemEntities.forEach { itemDao.update(it) }

        val collection2 = getFirstCollection()
        val items2 = getAllItems().filter { associatedBoxIds.contains(it?.boxId) }

        assertTrue(items2.none { it?.isFragile == true })
        assertEquals(false, collection2?.isFragile)
    }

    @Test
    fun collectionDao_DeleteCollection_AssociatedBoxesDeleted() = runTest {
        // TODO: write helper functions to get associated boxes and items to use in all tests

        val allBoxesBeforeCollectionDeletion = getAllBoxes()
        val boxesAssociatedWithCollection = getAllFirstCollectionBoxes()

        collectionDao.delete(listOf(collections.first().id))

        val allBoxesAfterCollectionDeletion = getAllBoxes()
        val boxesAssociatedWithCollectionAfterCollectionDeletion = getAllFirstCollectionBoxes()

        assertEquals(allBoxesAfterCollectionDeletion.size, allBoxesBeforeCollectionDeletion.size - boxesAssociatedWithCollection.size)
        assertEquals(boxesAssociatedWithCollectionAfterCollectionDeletion.size, 0)
    }

    @Test
    fun collectionDao_DeleteCollection_AssociatedItemsDeleted() = runTest {
        val allItemsBeforeCollectionDeletion = getAllItems()
        val boxesAssociatedWithCollection = getAllFirstCollectionBoxes()
        val boxesAssociatedWithCollectionIds = boxesAssociatedWithCollection.map { it?.id }
        val itemsAssociatedWithCollection = allItemsBeforeCollectionDeletion.filter { boxesAssociatedWithCollectionIds.contains(it?.boxId) }

        collectionDao.delete(listOf(collections.first().id))

        val allItemsAfterCollectionDeletion = getAllItems()
        val itemsAssociatedWithCollectionAfterCollectionDeletion = allItemsAfterCollectionDeletion.filter { allItemsAfterCollectionDeletion.map { box -> box?.id }.contains(it?.boxId) }

        assertEquals(allItemsAfterCollectionDeletion.size, allItemsBeforeCollectionDeletion.size - itemsAssociatedWithCollection.size)
        assertEquals(itemsAssociatedWithCollectionAfterCollectionDeletion.size, 0)
    }
}
