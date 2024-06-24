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
import com.example.packitupandroid.data.database.entities.toCollection
import com.example.packitupandroid.data.model.Box
import com.example.packitupandroid.data.model.Collection
import com.example.packitupandroid.data.model.Item
import com.example.packitupandroid.data.model.toCollection
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
class CollectionDaoTest {

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
        Box("2", "Box2", "1", collectionId=collections[0].id).toEntity(),
        Box("3", "Box3", "2", collectionId=collections[1].id).toEntity(),
        Box("4", "Box4", "2").toEntity(),
    )

    private val items = listOf(
        Item("1", "items1", "1", boxId=boxes[0].id).toEntity(),
        Item("2", "Item2", "1", boxId=boxes[0].id).toEntity(),
        Item("3", "Item3", "2", boxId=boxes[0].id).toEntity(),
        Item("4", "Item4", "2", boxId=boxes[1].id).toEntity(),
        Item("5", "Item5", "3", boxId=boxes[1].id).toEntity(),
        Item("6", "Item6", "3", boxId=boxes[2].id).toEntity(),
        Item("7", "Item7", "4", boxId=boxes[2].id).toEntity(),
        Item("8", "Item8", "4", boxId=boxes[3].id).toEntity(),
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
    private suspend fun getFirstCollection() =
        collectionDao.getQueryCollection(collections[0].id).first()
    private suspend fun getAllCollections() = collectionDao.getAllCollections().first()
    private suspend fun getAllBoxes() = boxDao.getAllBoxes().first()
    private suspend fun getAllItems() = itemDao.getAllItems().first()
    private suspend fun getAllFirstCollectionBoxes() =
        getAllBoxes().filter { it.collection_id == collections[0].id }

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
    fun collectionDao_InsertAllCollections_AllCollectionsInserted() = runBlocking {
        val dbCollections = getAllCollections()
        assertEquals(collections.size, dbCollections.size)
    }

    @Test
    @Throws(Exception::class)
    fun collectionDao_DisplayItemCountAndBoxCount_CorrectValues() = runBlocking {
        val boxes = getAllFirstCollectionBoxes()
        val boxIds = boxes.map { it.id }
        val items = getAllItems().filter { boxIds.contains(it.boxId) }
        val collection = getFirstCollection()

        assertEquals(collection.box_count, boxes.size)
        assertEquals(collection.item_count, items.size)
    }

    @Test
    fun collectionDao_InsertBox_BoxCountUpdated() = runBlocking {
        val boxes = getAllFirstCollectionBoxes()
        val collection = getFirstCollection()

        assertEquals(collection.box_count, boxes.size)

        val box2 = Box("2", "newBox2", "moo moo", collectionId =collections[0].id).toEntity()
        boxDao.insert(box2)

        val boxes2 = getAllFirstCollectionBoxes()
        val collection2 = getFirstCollection()

        assertEquals(collection2.box_count, boxes2.size)
    }

    @Test
    @Throws(Exception::class)
    fun collectionDao_GetAllCollections_AllCollectionsReturned() = runBlocking {
        // without .first()
        // type is val aBoxItems: Flow<LiBoxItem>>
        // .first() turns flow into list.
        val allCollections = getAllCollections()
        assertSameProperties(collections.map { it.toCollection() }, allCollections.map { it.toCollection() })
    }

    @Test
    @Throws(Exception::class)
    fun collectionDao_GetCollectionById_CollectionReturned() = runBlocking {
        // return flow
        val collection = collectionDao.getCollection(collections[3].id).first()
        // .first() - actual collection
        assertSameProperties(listOf(collections[3].toCollection()), listOf((collection.toCollection())))
    }

    @Test
    @Throws(Exception::class)
    fun collectionDao_UpdateCollection_CollectionUpdated() = runBlocking {
        val collection = collections[0].copy(name = "tacos", description = "updated collection 1")

        collectionDao.update(collection)

        val allCollections = getAllCollections()
        assertSameProperties(listOf(allCollections[0].toCollection()), listOf((collection.toCollection())))
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

        assertSameProperties(collections.map { it.toCollection() }, collectionsAfterUpdate.map { it.toCollection() })
    }

    @Test
    @Throws(Exception::class)
    fun collectionDao_DeleteAllCollections_AllCollectionsDeleted() = runBlocking {
        collectionDao.clearAllCollections()
        val allCollections = getAllCollections()

        assertTrue(allCollections.isEmpty())
    }

    @Test
    @Throws(Exception::class)
    fun collectionDao_DeleteSelectedCollections_SelectedCollectionsDeleted() = runBlocking {
        val collectionEntities = listOf(collections[0], collections[2], collections[3])
        collectionDao.deleteAll(collectionEntities)

        val collectionsAfterDelete = getAllCollections()

        assertEquals(collectionsAfterDelete.size, collections.size - collectionEntities.size)
        assertSameProperties(collectionsAfterDelete.map { it.toCollection()}, collectionsAfterDelete.map { it.toCollection() })
    }

    @Test
    fun collectionDao_DeleteItem_CollectionItemCountUpdated() = runBlocking {
        val boxIds = getAllFirstCollectionBoxes().map { it.id }
        val allItems = getAllItems().filter { boxIds.contains(it.boxId) }
        val collection = getFirstCollection()

        assertEquals(collection.item_count, allItems.size)

        itemDao.delete(items[0])

        val boxIds2 = getAllFirstCollectionBoxes().map { it.id }
        val allItems2 = getAllItems().filter { boxIds2.contains(it.boxId) }
        val collection2 = getFirstCollection()

        assertEquals(collection2.item_count, allItems2.size)
    }

    @Test
    @Throws(Exception::class)
    fun collectionDao_GetCollectionWithNonExistingId_NullReturned() = runBlocking {
        val collection = collectionDao.getQueryCollection("doesNotExist").first()
        assertNull(collection)
    }

    @Test
    @Throws(Exception::class)
    fun itemDao_UpdateItemValue_CollectionValueUpdated() = runBlocking {
        // TODO: update CollectionDao to automatically trigger when associated Item instance updates its value or isFragile

        val itemNewValue = 500.0

        val allBoxes = getAllFirstCollectionBoxes()
        val allItemsSum1 = getAllItems().filter { allBoxes.map { box -> box.id }.contains(it.boxId) }.sumOf { it.value }
        val collection = getFirstCollection()

        assertEquals(allItemsSum1, collection.value)

        val updatedItem = items[0].copy(value = itemNewValue)
        itemDao.update(updatedItem)

        val allBoxes2 = getAllFirstCollectionBoxes()
        val allItemsSum2 = getAllItems().filter { allBoxes2.map { box -> box.id }.contains(it.boxId) }.sumOf { it.value }
        val collection2 = getFirstCollection()

        assertEquals(allItemsSum2, collection2.value)
    }

    @Test
    @Throws(Exception::class)
    fun itemDao_UpdateItemIsFragileProperty_CollectionIsFragilePropertyUpdated() = runBlocking {
        // TODO: update BoxDao to automatically trigger when associated Item instance updates its value or isFragile

        val collection = getFirstCollection()
        assertEquals(collection.is_fragile, false)

        val updatedItem = items[0].copy(isFragile = true)
        itemDao.update(updatedItem)

        val collection2 = getFirstCollection()
        assertEquals(collection2.is_fragile, true)
    }

    @Test
    fun collectionDao_DeleteCollection_AssociatedBoxesDeleted() = runBlocking {
        val numberOfBoxesBeforeDeleteCollection = getAllBoxes().size
        val numberOfItemsBeforeDeleteCollection = getAllItems().size

        val boxes = getAllFirstCollectionBoxes()
        val items = getAllItems().filter { boxes.map { box -> box.id }.contains(it.boxId) }

        collectionDao.delete(collections[0])

        val numberOfBoxesAfterDeleteCollection = getAllBoxes().size
        val numberOfItemsAfterDeleteCollection = getAllItems().size

        assertEquals(numberOfBoxesAfterDeleteCollection, numberOfBoxesBeforeDeleteCollection - boxes.size)
        assertEquals(numberOfItemsAfterDeleteCollection, numberOfItemsBeforeDeleteCollection - items.size)
    }
}
