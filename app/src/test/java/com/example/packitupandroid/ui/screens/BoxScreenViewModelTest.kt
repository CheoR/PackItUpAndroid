package com.example.packitupandroid.ui.screens

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import com.example.packitupandroid.MainCoroutineRule
import com.example.packitupandroid.data.repository.MockBoxesRepository
import com.example.packitupandroid.data.repository.MockCollectionsRepository
import com.example.packitupandroid.data.repository.MockItemsRepository
import com.example.packitupandroid.source.local.TestDataSource
import com.example.packitupandroid.ui.screens.box.BoxesScreenViewModel
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


private const val COUNT: Int = 5

@OptIn(ExperimentalCoroutinesApi::class)
class BoxScreenViewModelTest {
    val dataSource = TestDataSource()
    val items = dataSource.items
    val boxes = dataSource.boxes
    val collections = dataSource.collections
    val collectionIdAndNames = dataSource.collectionIdAndNames

    private lateinit var viewModel: BoxesScreenViewModel

    private lateinit var collectionsRepository: MockCollectionsRepository
    private lateinit var repository: MockBoxesRepository
    private lateinit var itemsRepository: MockItemsRepository

    private val testDispatcher: TestDispatcher = StandardTestDispatcher()

    @get:Rule
    val coroutineRule = MainCoroutineRule()

    @Before
    fun setUp() {
        itemsRepository = MockItemsRepository()
        repository = MockBoxesRepository(
            itemsRepository = itemsRepository,
        )

        collectionsRepository = MockCollectionsRepository(
            boxesRepository = repository,
            itemsRepository = itemsRepository,
        )

        viewModel = BoxesScreenViewModel(
            savedStateHandle = SavedStateHandle(),
            repository = repository,
            defaultDispatcher = testDispatcher, // coroutineRule.testDispatcher,
        )

        // TODO: write TestRule that only runs if testName != "test_BoxesScreenViewModel_init_stateIsLoading"
        // so that won't need to manually call repository.load() in each test so that the state
        // is correct on test start
//        runBlocking {
//            repository.load(items)
//            repository.load(emptyList())
//        }
    }

    @After
    fun tearDown() {
        runBlocking {
            collectionsRepository.clear()
            repository.clear()
        }
    }

    @Test
    fun test_BoxesScreenViewModel_init_stateIsLoading() = runTest {
        val result = viewModel.elements.first()

        assertTrue(result is Result.Loading)
    }

    @Test
    fun test_BoxesScreenViewModel_loadEmptyList_stateIsSuccess() = runTest {
        repository.load(emptyList())

        val result = viewModel.elements.first()
        assertTrue(result is Result.Success)
    }

    @Test
    fun test_BoxesScreenViewModel_load_stateIsSuccess() = runTest {
        repository.load(boxes)
        advanceUntilIdle()

        val result = viewModel.elements.first()
        assertTrue(result is Result.Success)
    }

    @Test
    @Throws(Exception::class)
    fun test_BoxesScreenViewModel_load_listOfBoxessIsCorrect() = runTest {
        repository.load(boxes)
        advanceUntilIdle()

        val result = viewModel.elements.first() // .value - current state vs .first() - first state emission
        val data = (result as Result.Success).data

        assertEquals(boxes, data)
    }

    @Test
    fun test_BoxesScreenViewModel_load_listOfItemsCountIsCorrect() = runTest {
        repository.load(boxes)
        advanceUntilIdle()

        val data = when(val result = viewModel.elements.first()) {
            is Result.Success -> result.data
            else -> emptyList()
        }

        assertEquals(boxes.size, data.size)
    }

    @Test
    fun test_BoxesScreenViewModel_createCountBoxes_CountBoxesCreated() = runTest {
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
    fun test_BoxesScreenViewModel_createCountBoxesWithCollectionId_CountBoxesCreated() = runTest {
        repository.load(emptyList())

        val collectionId = collections.first().id
        val savedStateHandle = SavedStateHandle(mapOf("collectionId" to collectionId))
        val viewModel = BoxesScreenViewModel(
            savedStateHandle = savedStateHandle,
            repository = repository,
            defaultDispatcher = testDispatcher,
        )

        val result1 = viewModel.elements.first()
        val data1 = when(result1) {
            is Result.Success -> result1.data
            else -> emptyList()
        }

        assertEquals(0, data1.size)

        viewModel.create(COUNT)
        advanceUntilIdle()

        val result2 = viewModel.elements.first()
        val data2 = when(result2) {
            is Result.Success -> result2.data
            else -> emptyList()
        }.filterNotNull().filter { it.collectionId == collectionId }

        assertEquals(COUNT, data2.size)
    }

    @Test
    fun test_BoxesScreenViewModel_onFieldChange_nameUpdated() = runTest {
        repository.load(boxes)
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
    fun test_BoxesScreenViewModel_onFieldChange_descriptionUpdated() = runTest {
        repository.load(boxes)
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
    fun test_BoxesScreenViewModel_onFieldChange_isFragileNotUpdated() = runTest {
        repository.load(boxes)

        val result = viewModel.elements.first()
        val box = when(result) {
            is Result.Success -> result.data.first()
            else -> null
        }

        assertEquals(box?.isFragile, true)
        val mutableBox =  mutableStateOf(box)

        val newIsFragile = box?.isFragile?.not().toString()

        async {
            viewModel.onFieldChange(mutableBox, EditFields.IsFragile, newIsFragile)
            viewModel.update(mutableBox.value!!)
        }.await()

        val result2 = viewModel.elements.first()
        val box2 = when(result2) {
            is Result.Success -> result2.data.first()
            else -> null
        }

        assertEquals(mutableBox.value?.id, box2?.id)
        assertEquals(newIsFragile.toBoolean().not(), box2?.isFragile)
    }


    @Test
    fun test_BoxesScreenViewModel_onFieldChange_valueNotUpdated() = runTest {
        repository.load(boxes)
        advanceUntilIdle()

        val result = viewModel.elements.first()
        val box = when(result) {
            is Result.Success -> result.data.first()
            else -> null
        }
        val mutableBox =  mutableStateOf(box)

        val newValue = "5000.12"

        async {
            viewModel.onFieldChange(mutableBox, EditFields.Value, newValue)
            viewModel.update(mutableBox.value!!)
        }.await()

        advanceUntilIdle()

        val result2 = viewModel.elements.first()
        val box2 = when(result2) {
            is Result.Success -> result2.data.first()
            else -> null
        }

        assertEquals(mutableBox.value?.id, box2?.id)
        assertNotEquals(newValue.toDouble(), box2?.value)
        assertEquals(box?.value, box2?.value)
    }

    @Test
    fun test_BoxesScreenViewModel_onFieldChange_collectionIdUpdated() = runTest {
        repository.load(boxes)
        advanceUntilIdle()

        val currCollectionId = collections.first().id // "748c0084-4f2b-4957-ae2f-4892670c85f3" // first collection in data.kt
        val result = viewModel.elements.first()

        val box = when(result) {
            is Result.Success -> result.data.first()
            else -> null
        }
        val mutableBox =  mutableStateOf(box)

        assertEquals(mutableBox.value?.collectionId, currCollectionId)

        val newCollectionId = collectionIdAndNames.last().id

        async {
            viewModel.onFieldChange(mutableBox, EditFields.Dropdown, newCollectionId)
            advanceUntilIdle()
            viewModel.update(mutableBox.value!!)
        }.await()

        advanceUntilIdle()

        val result2 = viewModel.elements.first()
        val box2 = when(result2) {
            is Result.Success -> result2.data.first()
            else -> null
        }

        assertEquals(mutableBox.value?.id, box2?.id)
        assertEquals(newCollectionId, box2?.collectionId)
    }

    @Test
    fun test_BoxesScreenViewModel_ItemOnFieldChangeIsFragileUpdated_isFragileUpdated() = runTest {
        repository.load(boxes)
        itemsRepository.load(items)

        val result = viewModel.elements.first()
        val box = when (result) {
            is Result.Success -> result.data.first()
            else -> null
        }

        assertEquals(true, box?.isFragile)

        val itemResult = itemsRepository.elements.first()
        val itemIds = (itemResult as Result.Success).data
            .filterNotNull()
            .filter { it.boxId == box?.id }
            .map { it.id }

        itemsRepository.delete(itemIds)
        advanceUntilIdle()

        val itemResult2 = itemsRepository.elements.first()
        val (updatedValue, updatedItemCount, updatedIsFragile) = (itemResult2 as Result.Success).data
            .filterNotNull()
            .filter { item -> item.boxId == box?.id }
            .let { filteredItems ->
                Triple(
                    filteredItems.sumOf { item -> item.value },
                    filteredItems.size,
                    filteredItems.any { item -> item.isFragile == true }
                )
            }

        val box2 = box?.copy(
            value = updatedValue,
            itemCount = updatedItemCount,
            isFragile = updatedIsFragile,
        )

        assertEquals(box?.id, box2?.id)
        assertEquals(false, box2?.isFragile)
    }

    @Test
    fun test_BoxesScreenViewModel_ItemOnFieldChangeValueUpdated_valueUpdated() = runTest {
        repository.load(boxes)
        itemsRepository.load(items)

        val result = viewModel.elements.first()
        val box = when (result) {
            is Result.Success -> result.data.first()
            else -> null
        }

        val itemResult = itemsRepository.elements.first()
        val items = (itemResult as Result.Success).data
            .filterNotNull()
            .filter { it.boxId == box?.id }
        val initialItemsValue = items.sumOf { it.value }
        val itemIds = items.map { it.id }

        assertEquals(initialItemsValue, box?.value)

        itemsRepository.delete(itemIds)
        advanceUntilIdle()

        val itemResult2 = itemsRepository.elements.first()
        val (updatedValue, updatedItemCount, updatedIsFragile) = (itemResult2 as Result.Success).data
            .filterNotNull()
            .filter { item -> item.boxId == box?.id }
            .let { filteredItems ->
                Triple(
                    filteredItems.sumOf { item -> item.value ?: 0.0},
                    filteredItems.size,
                    filteredItems.any { item -> item.isFragile == true }
                )
            }

        val box2 = box?.copy(
            value = updatedValue,
            itemCount = updatedItemCount,
            isFragile = updatedIsFragile,
        )

        assertEquals(box?.id, box2?.id)
        assertEquals(0.0, box2?.value)
    }

    @Test
    fun test_BoxesScreenViewModel_DeleteCollection_AssociatedboxesDeletedFromDB() = runTest {
        collectionsRepository.load(collections)
        repository.load(boxes)
        itemsRepository.load(items)

        val collectionResult = collectionsRepository.elements.first()
        val collectionData = (collectionResult as Result.Success).data.filterNotNull()
        val collection = collectionData.first()

        val boxResult = repository.elements.first()
        val boxData = (boxResult as Result.Success).data.filterNotNull()
        val boxIds = boxData.filter { it.collectionId == collection.id }

        assertEquals(boxIds.size, collection.boxCount)

        collectionsRepository.delete(listOf(collection.id))
        collectionsRepository.cascadeDelete(listOf(collection.id))
        advanceUntilIdle()

        val boxResult2 = repository.elements.first()
        val boxData2 = (boxResult2 as Result.Success).data.filterNotNull()
        val boxIds2 = boxData2.filter { it.collectionId == collection.id }

        assertEquals(0, boxIds2.size)
        assertEquals(boxes.size - boxIds.size, boxData2.size)
    }
}
