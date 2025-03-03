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
import com.example.packitupandroid.data.model.Collection
import com.example.packitupandroid.data.repository.toCollection
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
class CollectionDaoTest {

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
    private suspend fun getAllCollections() = collectionDao.observeAll().first()
    private suspend fun getAllBoxes() = boxDao.observeAll().first()
    private suspend fun getAllItems() = itemDao.observeAll().first()
    private suspend fun getFirstCollection() = collectionDao.observeAll().first()[0]
    private suspend fun getAllFirstCollectionBoxes() = boxDao.observeAll().first().filter { it?.collectionId == collections[0].id }

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
    fun collectionDao_insertCollections_AllCollectionsInserted() = runBlocking {
        val dbCollections = getAllCollections()
        assertEquals(collections.size, dbCollections.size)
    }

    @Test
    @Throws(Exception::class)
    fun collectionDao_DisplayItemCountAndBoxCount_CorrectValues() = runBlocking {
        val boxes = getAllFirstCollectionBoxes()
        val boxIds = boxes.map { it?.id }
        val items = getAllItems().filter { boxIds.contains(it?.boxId) }
        val collection = getFirstCollection()

        assertEquals(collection?.boxCount, boxes.size)
        assertEquals(collection?.itemCount, items.size)
    }

    @Test
    fun collectionDao_InsertBox_BoxCountUpdated() = runBlocking {
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
    @Throws(Exception::class)
    fun collectionDao_GetAllCollections_AllCollectionsReturned() = runBlocking {
        // without .first()
        // type is val aBoxItems: Flow<LiBoxItem>>
        // .first() turns flow into list.
        val allCollections = getAllCollections()
        assertSameProperties(collections.map { it.toCollection() }, allCollections.filterNotNull() )
    }

    @Test
    @Throws(Exception::class)
    fun collectionDao_GetCollectionById_CollectionReturned() = runBlocking {
        // return flow
        val collection = collectionDao.get(collections[3].id)
        // .first() - actual collection
        assertSameProperties(listOf(collections[3].toCollection()), listOf((collection!!)))
    }

    @Test
    @Throws(Exception::class)
    fun collectionDao_UpdateCollection_CollectionUpdated() = runBlocking {
        val collection = collections[0].copy(name = "tacos", description = "updated collection 1")

        collectionDao.update(collection)

        val allCollections = getAllCollections()
        assertSameProperties(listOf(allCollections[0]!!), listOf((collection.toCollection())))
    }

    @Test
    @Throws(Exception::class)
    fun collectionDao_UpdateCollectionWithNonExistingId_NothingHappens() = runBlocking {
        val nonExistingCollection = Collection(
            id = "doesNotExist",
            name = "Non-Existing Collection",
            value = 10.0,
        ).toEntity()

        collectionDao.update(nonExistingCollection)

        val collectionsAfterUpdate = getAllCollections()

        assertSameProperties(collections.map { it.toCollection() }, collectionsAfterUpdate.filterNotNull())
    }

    @Test
    @Throws(Exception::class)
    fun collectionDao_deleteCollections_AllCollectionsDeleted() = runBlocking {
        collectionDao.clear()
        val allCollections = getAllCollections()

        assertTrue(allCollections.isEmpty())
    }

    @Test
    @Throws(Exception::class)
    fun collectionDao_DeleteSelectedCollections_SelectedCollectionsDeleted() = runBlocking {
        val collectionEntityIds = listOf(collections[0].id, collections[2].id, collections[3].id)
        collectionDao.delete(collectionEntityIds)

        val collectionsAfterDelete = getAllCollections()

        assertEquals(collectionsAfterDelete.size, collections.size - collectionEntityIds.size)
        assertSameProperties(collectionsAfterDelete.filterNotNull(), collectionsAfterDelete.filterNotNull())
    }

    @Test
    fun collectionDao_DeleteItem_CollectionItemCountUpdated() = runBlocking {
        val boxIds = getAllFirstCollectionBoxes().map { it?.id }
        val allItems = getAllItems().filter { boxIds.contains(it?.boxId) }
        val collection = getFirstCollection()

        assertEquals(collection?.itemCount, allItems.size)

        itemDao.delete(listOf(items[0].id))

        val boxIds2 = getAllFirstCollectionBoxes().map { it?.id }
        val allItems2 = getAllItems().filter { boxIds2.contains(it?.boxId) }
        val collection2 = getFirstCollection()

        assertEquals(collection2?.itemCount, allItems2.size)
    }

    @Test
    @Throws(Exception::class)
    fun collectionDao_GetCollectionWithNonExistingId_NullReturned() = runBlocking {
        val collection = collectionDao.get("doesNotExist")
        assertNull(collection)
    }

    @Test
    @Throws(Exception::class)
    fun itemDao_UpdateItemValue_CollectionValueUpdated() = runBlocking {
        // TODO: update CollectionDao to automatically trigger when associated Item instance updates its value or isFragile

        val itemNewValue = 500.0

        val allBoxes = getAllFirstCollectionBoxes()
        val allItemsSum1 = getAllItems().filter { allBoxes.map { box -> box?.id }.contains(it?.boxId) }.filterNotNull().sumOf { it.value }
        val collection = getFirstCollection()

        assertEquals(allItemsSum1, collection?.value)

        val updatedItem = items[0].copy(value = itemNewValue)
        itemDao.update(updatedItem)

        val allBoxes2 = getAllFirstCollectionBoxes()
        val allItemsSum2 = getAllItems().filter { allBoxes2.map { box -> box?.id }.contains(it?.boxId) }.filterNotNull().sumOf { it.value }
        val collection2 = getFirstCollection()

        assertEquals(allItemsSum2, collection2?.value)
    }

    @Test
    @Throws(Exception::class)
    fun itemDao_UpdateItemIsFragileProperty_CollectionIsFragilePropertyUpdated() = runBlocking {
        // TODO: update BoxDao to automatically trigger when associated Item instance updates its value or isFragile

        val collection = getFirstCollection()
        assertEquals(collection?.isFragile, false)

        val updatedItem = items[0].copy(isFragile = true)
        itemDao.update(updatedItem)

        val collection2 = getFirstCollection()
        assertEquals(collection2?.isFragile, true)
    }

    @Test
    fun collectionDao_DeleteCollection_AssociatedBoxesDeleted() = runBlocking {

        val allItemsBeforeCollectionDeletion = getAllItems()
        val allBoxesBeforeCollectionDeletion = getAllBoxes()

        val boxesAssociatedWithCollection = getAllFirstCollectionBoxes()
        val boxesAssociatedWithCollectionIds = boxesAssociatedWithCollection.map { it?.id }
        val itemsAssociatedWithCollection = allItemsBeforeCollectionDeletion.filter { boxesAssociatedWithCollectionIds.contains(it?.boxId) }

        collectionDao.delete(listOf(collections[0].id))

        val allItemsAfterCollectionDeletion = getAllItems()
        val allBoxesAfterCollectionDeletion = getAllBoxes()

        val boxesAssociatedWithCollectionAfterCollectionDeletion = getAllFirstCollectionBoxes()
        val itemsAssociatedWithCollectionAfterCollectionDeletion = allItemsAfterCollectionDeletion.filter { allItemsAfterCollectionDeletion.map { box -> box?.id }.contains(it?.boxId) }

        assertEquals(allBoxesAfterCollectionDeletion.size, allBoxesBeforeCollectionDeletion.size - boxesAssociatedWithCollection.size)
        assertEquals(allItemsAfterCollectionDeletion.size, allItemsBeforeCollectionDeletion.size - itemsAssociatedWithCollection.size)
        assertEquals(boxesAssociatedWithCollectionAfterCollectionDeletion.size, 0)
        assertEquals(itemsAssociatedWithCollectionAfterCollectionDeletion.size, 0)
    }
}
