package com.example.packitupandroid.ui.test


import android.util.Log
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import com.example.packitupandroid.assertSameExcept
import com.example.packitupandroid.data.model.Box
import com.example.packitupandroid.data.model.Item
import com.example.packitupandroid.ui.screens.item.ItemsScreenViewModel
import com.example.packitupandroid.utils.EditFields
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import com.example.packitupandroid.utils.Result
import junit.framework.TestCase.assertTrue
import junit.framework.TestCase.fail
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.withContext
import kotlin.collections.addAll
import kotlin.collections.find
import kotlin.collections.firstOrNull
import kotlin.collections.indexOfFirst
import kotlin.collections.removeAll
import kotlin.jvm.optionals.getOrNull
import kotlin.time.Duration.Companion.seconds

private const val COUNT: Int = 5

@OptIn(ExperimentalCoroutinesApi::class)
class ItemsScreenViewModelTest {

    private lateinit var viewModel: ItemsScreenViewModel
    private lateinit var repository: MockItemsRepository
    private val testDispatcher: TestDispatcher = StandardTestDispatcher()

    @get:Rule
    val coroutineRule = MainCoroutineRule()

    @Before
    fun setup() {
        repository = MockItemsRepository()
        viewModel = ItemsScreenViewModel(
            savedStateHandle = SavedStateHandle(),
            repository = repository,
            defaultDispatcher = testDispatcher,
        )

//        viewModel.loadData()
//        viewModel.create(5)
    }

    @Test
    fun test_ItemScreenViewModel_init_state_is_loading() = runTest {
        assertEquals(Result.Loading, viewModel.elements.value)
    }

    @Test
    fun test_ItemScreenViewModel_afterLoad_state_is_success() = runTest {

        val currState = viewModel.elements.drop(1).take(1).toList()[0]
        val state = viewModel.elements.first{ it is Result.Success }

        assertTrue(currState is Result.Success)
//        assertEquals(emptyList<Item>(), (state as Result.Success).data)
    }
}





//
//    @Test
//    fun `create method should insert items into repository`() = runTest {
//        viewModel.create(COUNT)
//        advanceUntilIdle()
//
//        val state = viewModel.elements.first { it is Result.Success }
//        assertTrue(state is Result.Success)
//        assertEquals(COUNT, (state as Result.Success).data.size + 10)
//    }

//    @Test
//    fun test_ItemScreenViewModel_loadData_state_is_success2() = runTest {
        // Arrange: Prepare test data and insert it into the repository
//        val items = List(COUNT) { index -> Item(name = "Item ${index + 1}") }
//        repository.insert(items)

//        viewModel.create(COUNT)
//         Act: Call the load method to load the data
//        viewModel.loadData()

        // Assert: Check if the state is Success and contains the expected items
//        val state = viewModel.elements.first { it is Result.Success }
//        assertTrue(state is Result.Success)
//        assertEquals(items.size, (state as Result.Success).data.size + 10)
//        assertEquals(items, state.data)
//    }


//    @Test
//    fun test_ItemScreenViewModel_loadData_state_is_success()  = runTest {
//        val result = withContext(Dispatchers.IO) {
//            viewModel.elements.first { it is Result.Success }
//        }

//        viewModel.loadData()
//        advanceUntilIdle()
//        val result = (viewModel.elements.value as? Result.Success)
//        viewModel.elements.first { it is Result.Success }

//        val result = viewModel.elements.value
//        assertEquals(Result.Success(emptyList<Item?>()), result) // viewModel.elements.value)

        // Wait for the StateFlow to emit a Result.Success
//        viewModel.elements.first { it is Result.Success }
//
//        // Check the state
//        when (val result = viewModel.elements.value) {
//            is Result.Success -> assertEquals(emptyList<Item>(), result.data)
//            is Result.Error -> fail("Expected Result.Success but got Result.Error: ${result.exception}")
//            is Result.Loading -> fail("Expected Result.Success but got Result.Loading")
//        }
//        assertTrue(viewModel.elements.value is Result.Success)
//    }
//
//    @Test
//    fun test_ItemScreenViewModel_create_COUNTnumberOfItems_COUNTnumberOfItemsexists() = runTest {
//        viewModel.loadData()
//
//        viewModel.create(COUNT)
//        advanceUntilIdle()
//
//        when (val result = viewModel.elements.value) {
//            is Result.Success -> assertEquals(COUNT, result.data.size)
//            else -> fail("Expected Result.Success but got Result.Error: $result")
//        }
//    }
//
//    @Test
//    fun test_ItemsScreenViewModel_create_COUNT_numberOfItems_combinedNumberOfItemsEqual() = runTest {
//        viewModel.loadData()
//        advanceUntilIdle()
//
//        val initialSize =  when(val result = viewModel.elements.value) { // Result.Success should return 0
//            is Result.Success -> result.data.size
//            else -> 5000
//        }
//
//        viewModel.create(COUNT)
//        advanceUntilIdle()
//
////        // Wait for StateFlow to emit Result.Success
////        val result = viewModel.elements.first { it is Result.Success }
////
////        when (result) {
////            is Result.Success -> assertEquals(COUNT + initialSize, result.data.size)
////            is Result.Error -> fail("Expected Result.Success but got Result.Error: ${result.exception}")
////            is Result.Loading -> fail("Expected Result.Success but got Result.Loading")
////        }
//
//        // Get the final size
//        val finalSize = when (val result = viewModel.elements.value) {
//            is Result.Success -> result.data.size
//            else -> 300
//        }
//
//        // Assert the final size
//        assertEquals(COUNT + initialSize, finalSize)
//    }
//}