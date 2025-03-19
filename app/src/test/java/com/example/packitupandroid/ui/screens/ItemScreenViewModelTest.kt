package com.example.packitupandroid.ui.screens


import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import com.example.packitupandroid.MainCoroutineRule
import com.example.packitupandroid.data.repository.MockBoxesRepository
import com.example.packitupandroid.data.repository.MockCollectionsRepository
import com.example.packitupandroid.data.repository.MockItemsRepository
import com.example.packitupandroid.source.local.TestDataSource
import com.example.packitupandroid.ui.screens.item.ItemsScreenViewModel
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
import org.junit.Before
import org.junit.Rule
import org.junit.Test


private const val COUNT: Int = 5

@OptIn(ExperimentalCoroutinesApi::class)
class ItemsScreenViewModelTest {
    val dataSource = TestDataSource()
    val items = dataSource.items
    val boxes = dataSource.boxes
    val collections = dataSource.collections
    val boxIdAndNames = dataSource.boxIdAndNames

    private lateinit var viewModel: ItemsScreenViewModel

    private lateinit var collectionsRepository: MockCollectionsRepository
    private lateinit var boxRepository: MockBoxesRepository
    private lateinit var repository: MockItemsRepository

    private val testDispatcher: TestDispatcher = StandardTestDispatcher()

    @get:Rule
    val coroutineRule = MainCoroutineRule()

    @Before
    fun setup() {
        repository = MockItemsRepository()
        boxRepository = MockBoxesRepository(
            itemsRepository = repository
        )
        collectionsRepository = MockCollectionsRepository(
            boxesRepository = boxRepository,
            itemsRepository = repository,
        )

        viewModel = ItemsScreenViewModel(
            savedStateHandle = SavedStateHandle(),
            repository = repository,
            defaultDispatcher = testDispatcher,
        )

        // TODO: write TestRule that only runs if testName != "testItemsViewModel_init_stateIsLoading"
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
            boxRepository.clear()
            repository.clear()
        }
    }

    @Test
    fun testItemsViewModel_init_stateIsLoading() = runTest {
        val result = viewModel.elements.value
        assertTrue(result is Result.Loading)
    }

    @Test
    fun testItemsViewModel_loadEmptyList_stateIsSuccess() = runTest {
        repository.load(emptyList())
        advanceUntilIdle()

        val result = viewModel.elements.value
        assertTrue(result is Result.Success)
    }

    @Test
    fun testItemsViewModel_load_stateIsSuccess() = runTest {
        repository.load(items)
        advanceUntilIdle()

        val result = viewModel.elements.value
        assertTrue(result is Result.Success)
    }

    @Test
    fun testItemsViewModel_load_listOfItemsIsCorrect() = runTest {
        repository.load(items)
        advanceUntilIdle()

        val result = viewModel.elements.first() // .value - current state vs .first() - first state emission
        val data = (result as Result.Success).data

        assertEquals(items, data)
    }

    @Test
    fun testItemsViewModel_load_listOfItemsCountIsCorrect() = runTest {
        repository.load(items)
        advanceUntilIdle()

        val data = when(val result = viewModel.elements.value) {
            is Result.Success -> result.data
            else -> emptyList()
        }

        assertEquals(items.size, data.size)
    }

    @Test
    fun testItemsViewModel_createCountItems_CountItemsCreated() = runTest {
        repository.load(emptyList())

        val result1 = viewModel.elements.first()
        val data1 = when(result1) {
            is Result.Success -> result1.data
            else -> emptyList()
        }

        assertEquals(0, data1.size)

        viewModel.create(COUNT)
        advanceUntilIdle()

        val result2 = viewModel.elements.first()
        val data2 = (result2 as Result.Success).data

        assertEquals(COUNT, data2.size)
    }

    @Test
    fun testItemsViewModel_createCountItemsWithBoxId_CountItemsCreated() = runTest {
        repository.load(emptyList())

        val boxId = boxes.first().id
        val savedStateHandle = SavedStateHandle(mapOf("boxId" to boxId))
        val viewModel = ItemsScreenViewModel(
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
        }.filterNotNull().filter { it.boxId == boxId }

        assertEquals(COUNT, data2.size)
    }

    @Test
    fun testItemsViewModel_onFieldChange_nameUpdated() = runTest {
        repository.load(items)
        advanceUntilIdle()

        val result = viewModel.elements.first()
        val item = when(result) {
            is Result.Success -> result.data.first()
            else -> null
        }
        val mutableItem =  mutableStateOf(item)

        val newName = "New Name"
        async {
            viewModel.onFieldChange(mutableItem, EditFields.Name, newName)
            viewModel.update(mutableItem.value!!)
        }.await()
//        advanceUntilIdle()

        val result2 = viewModel.elements.first()
        val item2 = when(result2) {
            is Result.Success -> result2.data.first()
            else -> null
        }

        assertEquals(mutableItem.value?.id, item2?.id)
        assertEquals(newName, item2?.name)
    }

    @Test
    fun testItemsViewModel_onFieldChange_descriptionUpdated() = runTest {
        repository.load(items)
        advanceUntilIdle()

        val result = viewModel.elements.first()
        val item = when(result) {
            is Result.Success -> result.data.first()
            else -> null
        }
        val mutableItem =  mutableStateOf(item)

        val newDescription = "New Description"

        async {
            viewModel.onFieldChange(mutableItem, EditFields.Description, newDescription)
            viewModel.update(mutableItem.value!!)
        }.await()


        val result2 = viewModel.elements.first()
        val item2 = when(result2) {
            is Result.Success -> result2.data.first()
            else -> null
        }

        assertEquals(mutableItem.value?.id, item2?.id)
        assertEquals(newDescription, item2?.description)
    }

    @Test
    fun testItemsViewModel_onFieldChange_isFragileUpdated() = runTest {
        repository.load(items)

        val result = viewModel.elements.first()
        val item = when(result) {
            is Result.Success -> result.data.first()
            else -> null
        }

        assertEquals(item?.isFragile, true)
        val mutableItem =  mutableStateOf(item)

        val newIsFragile = item?.isFragile?.not().toString()

        async {
            viewModel.onFieldChange(mutableItem, EditFields.IsFragile, newIsFragile)
            viewModel.update(mutableItem.value!!)
        }.await()

        val result2 = viewModel.elements.first()
        val item2 = when(result2) {
            is Result.Success -> result2.data.first()
            else -> null
        }

        assertEquals(mutableItem.value?.id, item2?.id)
        assertEquals(newIsFragile.toBoolean(), item2?.isFragile)
    }

    @Test
    fun testItemsViewModel_onFieldChange_valueUpdated() = runTest {
        repository.load(items)
        advanceUntilIdle()

        val result = viewModel.elements.value // .first()
        val item = when(result) {
            is Result.Success -> result.data.first()
            else -> null
        }
        val mutableItem =  mutableStateOf(item)

        val newValue = "5000.12"

        async {
            viewModel.onFieldChange(mutableItem, EditFields.Value, newValue)
            viewModel.update(mutableItem.value!!)
        }.await()

        advanceUntilIdle()

        val result2 = viewModel.elements.first()
        val item2 = when(result2) {
            is Result.Success -> result2.data.first()
            else -> null
        }

        assertEquals(mutableItem.value?.id, item2?.id)
        assertEquals(newValue.toDouble(), item2?.value)
    }

    @Test
    fun testItemsViewModel_onFieldChange_boxIdUpdated() = runTest {
        repository.load(items)
        advanceUntilIdle()

        val currBoxId = boxes.first().id // "e99a99f8-748d-427a-a305-14bda19d71a0" // first box in data.kt
        val result = viewModel.elements.first()

        val item = when(result) {
            is Result.Success -> result.data.first()
            else -> null
        }
        val mutableItem =  mutableStateOf(item)

        assertEquals(mutableItem.value?.boxId, currBoxId)

        val newBoxId = boxIdAndNames.last().id

        async {
            viewModel.onFieldChange(mutableItem, EditFields.Dropdown, newBoxId)
            viewModel.update(mutableItem.value!!)
        }.await()

        advanceUntilIdle()

        val result2 = viewModel.elements.first()
        val item2 = when(result2) {
            is Result.Success -> result2.data.first()
            else -> null
        }

        assertEquals(mutableItem.value?.id, item2?.id)
        assertEquals(newBoxId, item2?.boxId)
    }

    @Test
    fun testItemsViewModel_DeleteBox_AssociatedItemsDeletedFromDB() = runTest {
        boxRepository.load(boxes)
        repository.load(items)

        val boxResult = boxRepository.elements.first()
        val boxData = (boxResult as Result.Success).data.filterNotNull()
        val boxId = boxData.first().id

        val itemResult = repository.elements.first()
        val itemData = (itemResult as Result.Success).data.filterNotNull()
        val itemIds = itemData.filter { it.boxId == boxId }

        assertEquals(items.size, itemData.size)

        boxRepository.delete(listOf(boxId))
        boxRepository.cascadeDelete(listOf(boxId))

        val itemResult2 = repository.elements.first()
        val itemData2 = (itemResult2 as Result.Success).data.filterNotNull()
        val itemIds2 = itemData2.filter { it.boxId == boxId }

        assertEquals(0, itemIds2.size)
        assertEquals(items.size - itemIds.size, itemData2.size)
    }

    @Test
    fun testItemsViewModel_DeleteCollection_AssociatedItemsDeletedFromDB() = runTest {
        collectionsRepository.load(collections)
        boxRepository.load(boxes)
        repository.load(items)

        val collectionResult = collectionsRepository.elements.first()
        val collectionData = (collectionResult as Result.Success).data.filterNotNull()
        val collectionId = collectionData.first().id

        val boxResult = boxRepository.elements.first()
        val boxData = (boxResult as Result.Success).data.filterNotNull()
        val boxIds = boxData.filter { it.collectionId == collectionId }

        val itemResult = repository.elements.first()
        val itemData = (itemResult as Result.Success).data.filterNotNull()
        val itemIds = itemData.filter { it.boxId in boxIds.map { it.id } }

        assertEquals(items.size, itemData.size)

        collectionsRepository.delete(listOf(collectionId))
        collectionsRepository.cascadeDelete(listOf(collectionId))

        val itemResult2 = repository.elements.first()
        val itemData2 = (itemResult2 as Result.Success).data.filterNotNull()
        val itemIds2 = itemData2.filter { it.boxId in boxIds.map { it.id } }

        assertEquals(0, itemIds2.size)
        assertEquals(items.size - itemIds.size, itemData2.size)
    }
}
