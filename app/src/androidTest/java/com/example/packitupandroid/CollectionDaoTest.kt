package com.example.packitupandroid

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.packitupandroid.data.database.AppDatabase
import com.example.packitupandroid.data.database.dao.BoxDao
import com.example.packitupandroid.data.database.dao.CollectionDao
import com.example.packitupandroid.data.database.dao.ItemDao
import com.example.packitupandroid.data.model.Box
import com.example.packitupandroid.data.model.Collection
import com.example.packitupandroid.data.model.Item
import com.example.packitupandroid.data.model.toCollection
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
class CollectionDaoTest {

    private lateinit var collectionDao: CollectionDao
    private lateinit var boxDao: BoxDao
    private lateinit var itemDao: ItemDao
    private lateinit var db: AppDatabase

    private var collection1 = Collection("1", "Collection1").toEntity()
    private var collection2 = Collection("2", "Collection2").toEntity()
    private var collection3 = Collection("3", "Collection3").toEntity()
    private var collection4 = Collection("4", "Collection4").toEntity()

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

    private suspend fun addAllCollectionToDb(){
        addTwoCollectionToDb()
        collectionDao.insert(collection3)
        collectionDao.insert(collection4)
    }

    private suspend fun addTwoBoxToDb(){
        boxDao.insert(box1)
        boxDao.insert(box2)
    }

    private suspend fun addAllBoxToDb(){
        addTwoBoxToDb()
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
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun collectionDao_InsertTwoCollections_Success() = runBlocking {
        addTwoCollectionToDb()
        val allCollections = collectionDao.getAllCollections().first()
        assertEquals(allCollections.size, 2)
    }

    @Test
    @Throws(Exception::class)
    fun collectionDao_InsertAllCollections_AllCollectionsInserted() = runBlocking {
        val collectionEntities = listOf(collection1, collection2, collection3, collection4)
        collectionDao.insertAll(collectionEntities)

        val allCollections = collectionDao.getAllCollections().first()
        assertEquals(collectionEntities.size, allCollections.size)
    }

    @Test
    @Throws(Exception::class)
    fun collectionDao_DisplayItemCountAndBoxCount_CorrectValues() = runBlocking {
        addAllCollectionToDb()
        addAllBoxToDb()
        addAllItemToDb()

        val boxes = boxDao.getAllBoxes().first()
        val boxesForCollection = boxes.filter{ it.collection_id == collection1.id }
        val boxIds = boxesForCollection.map { it.id }
        val items = itemDao.getAllItems().first()
        val itemsForBoxes = items.filter { boxIds.contains(it.boxId) }
        val collection = collectionDao.getQueryCollection(collection1.id).first()

        assertEquals(collection.box_count, boxesForCollection.size)
        assertEquals(collection.item_count, itemsForBoxes.size)
    }

    @Test
    fun collectionDao_InsertBox_BoxCountUpdated() = runBlocking {
        addAllCollectionToDb()
        addAllBoxToDb()
        addAllItemToDb()

        val allBoxes = boxDao.getAllBoxes().first().filter { it.collection_id == collection1.id }
        val collection = collectionDao.getQueryCollection(collection1.id).first()

        assertEquals(collection.box_count, allBoxes.size)

        val box2 = Box("2", "newBox2", "moo moo", collectionId =collection1.id).toEntity()
        boxDao.insert(box2)

        val allBoxes2 = boxDao.getAllBoxes().first().filter { it.collection_id == collection1.id }
        val collection2 = collectionDao.getQueryCollection(collection1.id).first()

        assertEquals(collection2.box_count, allBoxes2.size)
    }

    @Test
    @Throws(Exception::class)
    fun collectionDao_GetAllCollections_AllCollectionsReturned() = runBlocking {
        addAllCollectionToDb()
        // without .first()
        // type is val aBoxItems: Flow<LiBoxItem>>
        // .first() turns flow into list.
        val allCollections = collectionDao.getAllCollections().first().map { it.toCollection().toEntity() }

        assertEquals(allCollections[0].id, collection1.id)
        assertEquals(allCollections[1].id, collection2.id)
        assertEquals(allCollections[2].id, collection3.id)
        assertEquals(allCollections[3].id, collection4.id)
    }

    @Test
    @Throws(Exception::class)
    fun collectionDao_GetCollectionById_CollectionReturned() = runBlocking {
        addAllCollectionToDb()
        // return flow
        val collection = collectionDao.getCollection(collection4.id).first()
        // .first() - actual collection
        assertEquals(collection4.id, collection.id)
    }

    @Test
    @Throws(Exception::class)
    fun collectionDao_UpdateBoxes_BoxesUpdated() = runBlocking {
        addAllCollectionToDb()
        addAllBoxToDb()
        addAllItemToDb()

        val updatedCollection1 = collection1.copy(name = "tacos", description = "updated collection 1")
        val updatedCollection2 = collection2.copy(name = "tacos", description = "updated collection 2")

        collectionDao.update(updatedCollection1)
        collectionDao.update(updatedCollection2)

        val allCollections = collectionDao.getAllCollections().first().map { it.toCollection().toEntity() }

        assertEquals(allCollections[0].id, updatedCollection1.id)
        assertEquals(allCollections[0].name, updatedCollection1.name)
        assertEquals(allCollections[0].description, updatedCollection1.description)

        assertEquals(allCollections[1].id, updatedCollection2.id)
        assertEquals(allCollections[1].name, updatedCollection2.name)
        assertEquals(allCollections[1].description, updatedCollection2.description)
    }

    @Test
    @Throws(Exception::class)
    fun collectionDao_UpdateCollectionWithNonExistingId_NothingHappens() = runBlocking {
        addAllCollectionToDb()
        val nonExistingCollection = Collection(
            id = "doesNotExist",
            name = "Non-Existing Collection",
            value = 10.0,
        ).toEntity()

        collectionDao.update(nonExistingCollection)

        val allCollections = collectionDao.getAllCollections().first().map { it.toCollection().toEntity() }
        assertEquals(allCollections[0].id, collection1.id)
        assertEquals(allCollections[0].name, collection1.name)
        assertEquals(allCollections[0].description, collection1.description)
        assertEquals(allCollections[1].id, collection2.id)
        assertEquals(allCollections[1].name, collection2.name)
        assertEquals(allCollections[1].description, collection2.description)
        assertEquals(allCollections[2].id, collection3.id)
        assertEquals(allCollections[2].name, collection3.name)
        assertEquals(allCollections[2].description, collection3.description)
        assertEquals(allCollections[3].id, collection4.id)
        assertEquals(allCollections[3].name, collection4.name)
        assertEquals(allCollections[3].description, collection4.description)

    }

    @Test
    @Throws(Exception::class)
    fun collectionDao_DeleteAllCollections_AllCollectionsDeleted() = runBlocking {
        addAllCollectionToDb()
        addAllBoxToDb()
        addAllItemToDb()

        collectionDao.clearAllCollections()
        val allCollections = collectionDao.getAllCollections().first()

        assertTrue(allCollections.isEmpty())
    }

    @Test
    @Throws(Exception::class)
    fun collectionDao_DeleteSelectedCollections_SelectedCollectionsDeleted() = runBlocking {
        addAllCollectionToDb()
        addAllBoxToDb()
        addAllItemToDb()

        val collectionEntities = listOf(collection1, collection3, collection4)
        collectionDao.deleteAll(collectionEntities)

        val allCollections = collectionDao.getAllCollections().first()

        assertTrue(allCollections.size == 1)
        assertEquals(allCollections[0].id, collection2.id)
        assertEquals(allCollections[0].name, collection2.name)
        assertEquals(allCollections[0].description, collection2.description)
    }

    @Test
    fun collectionDao_DeleteItem_CollectionItemCountUpdated() = runBlocking {
        addAllCollectionToDb()
        addAllBoxToDb()
        addAllItemToDb()

        val boxIds = boxDao.getAllBoxes().first().filter { it.collection_id == collection1.id }.map { it.id }
        val allItems = itemDao.getAllItems().first().filter { boxIds.contains(it.boxId) }
        val collection = collectionDao.getQueryCollection(collection1.id).first()

        assertEquals(collection.item_count, allItems.size)

        itemDao.delete(item1)

        val boxIds2 = boxDao.getAllBoxes().first().filter { it.collection_id == collection1.id }.map { it.id }
        val allItems2 = itemDao.getAllItems().first().filter { boxIds2.contains(it.boxId) }
        val collection2 = collectionDao.getQueryCollection(collection1.id).first()

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
        addAllCollectionToDb()
        addAllBoxToDb()
        addAllItemToDb()

        val itemNewValue = 500.0

        val allBoxes = boxDao.getAllBoxes().first().filter { it.collection_id == collection1.id }
        val allItemsSum1 = itemDao.getAllItems().first().filter { allBoxes.map { box -> box.id }.contains(it.boxId) }.sumOf { it.value }
        val collection = collectionDao.getQueryCollection(collection1.id).first()

        assertEquals(allItemsSum1, collection.value)

        val updatedItem = item1.copy(value = itemNewValue)
        itemDao.update(updatedItem)

        val allBoxes2 = boxDao.getAllBoxes().first().filter { it.collection_id == collection1.id }
        val allItemsSum2 = itemDao.getAllItems().first().filter { allBoxes2.map { box -> box.id }.contains(it.boxId) }.sumOf { it.value }
        val collection2 = collectionDao.getQueryCollection(collection1.id).first()

        assertEquals(allItemsSum2, collection2.value)
    }

    @Test
    @Throws(Exception::class)
    fun itemDao_UpdateItemIsFragileProperty_CollectionIsFragilePropertyUpdated() = runBlocking {
        // TODO: update BoxDao to automatically trigger when associated Item instance updates its value or isFragile
        addAllCollectionToDb()
        addAllBoxToDb()
        addAllItemToDb()

        val collection = collectionDao.getQueryCollection(collection1.id).first()
        assertEquals(collection.is_fragile, false)

        val updatedItem = item1.copy(isFragile = true)
        itemDao.update(updatedItem)

        val collection2 = collectionDao.getQueryCollection(collection1.id).first()
        assertEquals(collection2.is_fragile, true)
    }

    @Test
    fun collectionDao_DeleteCollection_AssociatedBoxesDeleted() = runBlocking {
        addAllCollectionToDb()
        addAllBoxToDb()
        addAllItemToDb()

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