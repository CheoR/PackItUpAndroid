package com.example.packitupandroid.ui.screens

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import com.example.packitupandroid.MainCoroutineRule
import com.example.packitupandroid.data.repository.MockBoxesRepository
import com.example.packitupandroid.data.repository.MockCollectionsRepository
import com.example.packitupandroid.data.repository.MockItemsRepository
import com.example.packitupandroid.source.local.TestDataSource
import com.example.packitupandroid.ui.screens.box.BoxesScreenViewModel
import com.example.packitupandroid.ui.screens.collection.CollectionsScreenViewModel
import com.example.packitupandroid.utils.EditFields
import com.example.packitupandroid.utils.Result
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test


private const val COUNT = 5

@OptIn(ExperimentalCoroutinesApi::class)
class CollectionsScreenViewModelTest {
    val dataSource = TestDataSource()
    val items = dataSource.items
    val boxes = dataSource.boxes
    val collections = dataSource.collections

    private lateinit var viewModel: CollectionsScreenViewModel

    private lateinit var repository: MockCollectionsRepository
    private lateinit var itemsRepository: MockItemsRepository
    private lateinit var boxesRepository: MockBoxesRepository

    private val testDispatcher: TestDispatcher = StandardTestDispatcher()

    @get:Rule
    val coroutineRule = MainCoroutineRule()

    @Before
    fun setUp() {
        itemsRepository = MockItemsRepository()
        boxesRepository = MockBoxesRepository(
            itemsRepository = itemsRepository,
        )
        repository = MockCollectionsRepository(
            boxesRepository = boxesRepository,
            itemsRepository = itemsRepository,
        )

        viewModel = CollectionsScreenViewModel(
            savedStateHandle = SavedStateHandle(),
            repository = repository,
            defaultDispatcher = coroutineRule.testDispatcher, // testDispatcher - // TODO: review usage
        )

        // TODO: write TestRule that only runs if testName != "testCollectionsViewModel_init_stateIsLoading"
        // so that won't need to manually call repository.load() in each test so that the state
        // is correct on test start
//        runBlocking {
//            repository.load(collections)
//            repository.load(emptyList())
//        }
    }

    @After
    fun tearDown() {
        runBlocking {
            itemsRepository.clear()
            boxesRepository.clear()
            repository.clear()
        }
    }

    @Test
    fun test_CollectionsScreenViewModel_init_stateIsLoading() = runTest {
        val result = viewModel.elements.first()

        assertTrue(result is Result.Loading)
    }


    @Test
    fun test_CollectionsScreenViewModel_loadEmptyList_stateIsSuccess() = runTest {
        repository.load(emptyList())

        val result = viewModel.elements.first()

        assertTrue(result is Result.Success)
    }

    @Test
    fun test_CollectionsScreenViewModel_load_stateIsSuccess() = runTest {
        repository.load(collections)
        advanceUntilIdle()

        val result = viewModel.elements.first()
        assertTrue(result is Result.Success)
    }

    @Test
    @Throws(Exception::class)
    fun test_CollectionsScreenViewModel_load_listOfBoxesIsCorrect() = runTest {
        repository.load(collections)
        advanceUntilIdle()

        val result = viewModel.elements.first() // .value - current state vs .first() - first state emission
        val data = (result as Result.Success).data

        assertEquals(collections, data)
    }

    @Test
    fun test_CollectionsScreenViewModel_load_listOfItemsCountIsCorrect() = runTest {
        repository.load(collections)
        advanceUntilIdle()

        val data = when(val result = viewModel.elements.first()) {
            is Result.Success -> result.data
            else -> emptyList()
        }

        assertEquals(boxes.size, data.size)
    }

    @Test
    fun test_CollectionsScreenViewModel_createCountCollections_CountCollectionsCreated() = runTest {
        repository.load(emptyList())

        val result1 = viewModel.elements.first()
        val data1 = when(result1) {
            is Result.Success -> result1.data
            else -> emptyList()
        }

        assertEquals(data1.size, 0)

        viewModel.create(COUNT)
        advanceUntilIdle()

        val result2 = viewModel.elements.first()
        val data2 = (result2 as Result.Success).data

        assertEquals(COUNT, data2.size)
    }

    @Test
    fun test_CollectionsScreenViewModel_createCountBoxesWithCollectionId_CollectionBoxCountUpdated() = runTest {
        repository.load(emptyList())
        boxesRepository.load(boxes)

        val collection = collections.first()
        val collectionId = collection.id
        val associatedBoxes = boxes.filter { it.collectionId == collectionId }
        val savedStateHandle = SavedStateHandle(mapOf("collectionId" to collectionId))
        val viewModel = BoxesScreenViewModel(
            savedStateHandle = savedStateHandle,
            repository = boxesRepository,
            defaultDispatcher = testDispatcher,
        )

        val result1 = viewModel.elements.first()
        val data1 = when(result1) {
            is Result.Success -> result1.data
            else -> emptyList()
        }

        assertEquals(collection.boxCount, associatedBoxes.size)

        viewModel.create(COUNT)
        advanceUntilIdle()

        val result2 = viewModel.elements.first()
        val associatedBoxes2 = when(result2) {
            is Result.Success -> result2.data
            else -> emptyList()
        }.filterNotNull().filter { it.collectionId == collectionId }

        assertEquals(collection.boxCount + COUNT, associatedBoxes2.size)
    }

    @Test
    fun test_CollectionsScreenViewModel_onFieldChange_nameUpdated() = runTest {
        repository.load(collections)
        advanceUntilIdle()

        val result = viewModel.elements.first()
        val box = when(result) {
            is Result.Success -> result.data.first()
            else -> null
        }
        val mutableBox =  mutableStateOf(box)

        val newName = "New Name"
        async {
            viewModel.onFieldChange(mutableBox, EditFields.Name, newName)
            viewModel.update(mutableBox.value!!)
        }.await()
//        advanceUntilIdle()

        val result2 = viewModel.elements.first()
        val box2 = when(result2) {
            is Result.Success -> result2.data.first()
            else -> null
        }

        assertEquals(mutableBox.value?.id, box2?.id)
        assertEquals(newName, box2?.name)
    }

    @Test
    fun test_CollectionsScreenViewModel_onFieldChange_descriptionUpdated() = runTest {
        repository.load(collections)
        advanceUntilIdle()

        val result = viewModel.elements.first()
        val box = when(result) {
            is Result.Success -> result.data.first()
            else -> null
        }
        val mutableBox =  mutableStateOf(box)

        val newDescription = "New Description"

        async {
            viewModel.onFieldChange(mutableBox, EditFields.Description, newDescription)
            viewModel.update(mutableBox.value!!)
        }.await()


        val result2 = viewModel.elements.first()
        val box2 = when(result2) {
            is Result.Success -> result2.data.first()
            else -> null
        }

        assertEquals(mutableBox.value?.id, box2?.id)
        assertEquals(newDescription, box2?.description)
    }

    @Test
    fun test_CollectionsScreenViewModel_onFieldChange_isFragileNotUpdated() = runTest {
        repository.load(collections)

        val result = viewModel.elements.first()
        val collection = when(result) {
            is Result.Success -> result.data.first()
            else -> null
        }

        assertEquals(collection?.isFragile, true)
        val mutableBox =  mutableStateOf(collection)

        val newIsFragile = collection?.isFragile?.not().toString()

        async {
            viewModel.onFieldChange(mutableBox, EditFields.IsFragile, newIsFragile)
            viewModel.update(mutableBox.value!!)
        }.await()

        val result2 = viewModel.elements.first()
        val collection2 = when(result2) {
            is Result.Success -> result2.data.first()
            else -> null
        }

        assertEquals(mutableBox.value?.id, collection2?.id)
        assertEquals(newIsFragile.toBoolean().not(), collection2?.isFragile)
    }

    @Test
    fun test_CollectionsScreenViewModel_onFieldChange_valueNotUpdated() = runTest {
        repository.load(collections)
        advanceUntilIdle()

        val result = viewModel.elements.first()
        val collection = when(result) {
            is Result.Success -> result.data.first()
            else -> null
        }
        val mutableBox =  mutableStateOf(collection)

        val newValue = "5000.12"

        async {
            viewModel.onFieldChange(mutableBox, EditFields.Value, newValue)
            viewModel.update(mutableBox.value!!)
        }.await()

        advanceUntilIdle()

        val result2 = viewModel.elements.first()
        val collection2 = when(result2) {
            is Result.Success -> result2.data.first()
            else -> null
        }

        assertEquals(mutableBox.value?.id, collection2?.id)
        assertNotEquals(newValue.toDouble(), collection2?.value)
        assertEquals(collection?.value, collection2?.value)
    }

    @Test
    fun test_CollectionsScreenViewModel_ItemOnFieldChangeIsFragileUpdated_isFragileUpdated() = runTest {
        repository.load(collections)
        boxesRepository.load(boxes)
        itemsRepository.load(items)

        val result = viewModel.elements.first()
        val collection = when (result) {
            is Result.Success -> result.data.first()
            else -> null
        }

        assertEquals(true, collection?.isFragile)

        val boxResult = boxesRepository.elements.first()
        val associatedBoxes = (boxResult as Result.Success).data
            .filterNotNull()
            .filter { it.collectionId == collection?.id }
        val boxIds = associatedBoxes.map { it.id }
        val itemResult = itemsRepository.elements.first()
        val associatedItems = (itemResult as Result.Success).data
            .filterNotNull()
            .filter { boxIds.contains(it.boxId)  }
        val itemIds = associatedItems.map { it.id }

        itemsRepository.delete(itemIds)
        advanceUntilIdle()

        val boxResult2 = itemsRepository.elements.first()
        val (updatedValue, updatedItemCount, updatedIsFragile) = (boxResult2 as Result.Success).data
            .filterNotNull()
            .filter { it.boxId == collection?.id }
            .let { filteredBox ->
                Triple(
                    filteredBox.sumOf { box -> box.value },
                    filteredBox.size,
                    filteredBox.any { box -> box.isFragile == true }
                )
            }

        val collection2 = collection?.copy(
            value = updatedValue,
            itemCount = updatedItemCount,
            isFragile = updatedIsFragile,
        )

        assertEquals(collection?.id, collection2?.id)
        assertEquals(false, collection2?.isFragile)
    }

    @Test
    fun test_CollectionsScreenViewModel_ItemOnFieldChangeValueUpdated_valueUpdated() = runTest {
        repository.load(collections)
        boxesRepository.load(boxes)
        itemsRepository.load(items)

        val result = viewModel.elements.first()
        val collection = when (result) {
            is Result.Success -> result.data.first()
            else -> null
        }

        assertEquals(true, collection?.isFragile)

        val boxResult = boxesRepository.elements.first()
        val associatedBoxes = (boxResult as Result.Success).data
            .filterNotNull()
            .filter { it.collectionId == collection?.id }
        val boxIds = associatedBoxes.map { it.id }
        val itemResult = itemsRepository.elements.first()
        val associatedItems = (itemResult as Result.Success).data
            .filterNotNull()
            .filter { boxIds.contains(it.boxId)  }
        val itemIds = associatedItems.map { it.id }

        itemsRepository.delete(itemIds)
        advanceUntilIdle()

        val boxResult2 = itemsRepository.elements.first()
        val (updatedValue, updatedItemCount, updatedIsFragile) = (boxResult2 as Result.Success).data
            .filterNotNull()
            .filter { it.boxId == collection?.id }
            .let { filteredBox ->
                Triple(
                    filteredBox.sumOf { box -> box.value },
                    filteredBox.size,
                    filteredBox.any { box -> box.isFragile == true }
                )
            }

        val collection2 = collection?.copy(
            value = updatedValue,
            itemCount = updatedItemCount,
            isFragile = updatedIsFragile,
        )

        assertEquals(collection?.id, collection2?.id)
        assertEquals(0.0, collection2?.value)
    }
}
