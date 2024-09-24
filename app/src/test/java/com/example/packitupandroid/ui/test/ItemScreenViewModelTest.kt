package com.example.packitupandroid.ui.test


import androidx.lifecycle.SavedStateHandle
import com.example.packitupandroid.assertSameExcept
import com.example.packitupandroid.ui.screens.item.ItemsScreenViewModel
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ItemScreenViewModelTest {
    private lateinit var viewModel: ItemsScreenViewModel
    private val mockRepository = MockItemsRepository()

    //    @get:Rule
    //    val rule = InstantTaskExecutorRule()
    // val testDispatcher: TestDispatcher = StandardTestDispatcher() //UnconfinedTestDispatcher()
    // Set the main coroutines dispatcher for unit testing.
    @ExperimentalCoroutinesApi
    @get:Rule
    val coroutineRule = MainCoroutineRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
//        Dispatchers.setMain(dispatcher) // testDispatcher Dispatchers.Unconfined
        val savedStateHandle = SavedStateHandle()

        viewModel = ItemsScreenViewModel(
            savedStateHandle = savedStateHandle,
            itemsRepository = mockRepository,
            defaultDispatcher = coroutineRule.testDispatcher,
        )

        viewModel.create(COUNT)
    }

//    @OptIn(ExperimentalCoroutinesApi::class)
//    @After
//    fun tearDown() {
//        Dispatchers.resetMain()
//    }

    @Test
    fun testgetAllElements() = runTest { // (UnconfinedTestDispatcher()) {
        // Set Main dispatcher to not run coroutines eagerly, for just this one test
        // Dispatchers.setMain(StandardTestDispatcher())
        val result = viewModel.getAllElements()
        assertEquals(COUNT, result.size)
    }

    @Test
    fun testCreateItems() = runTest {
        val initialSize = viewModel.getAllElements().size
        viewModel.create(COUNT)
        val result = viewModel.getAllElements()
        assertEquals(initialSize + COUNT, result.size)
    }

    @Test
    fun testUpdateItem() = runTest {
        val item = viewModel.getAllElements().first()
        val updatedItem = item.copy(name = "Updated Item 1")

        viewModel.update(updatedItem)

        val result = viewModel.getAllElements().first()

        assertEquals(updatedItem.name, result.name)
        assertSameExcept(updatedItem, result, "name", "lastModified")
    }

    @Test
    fun testDestroyItem() = runTest {
        val initialSize = viewModel.getAllElements().size
        val item = viewModel.getAllElements().first()

        viewModel.destroy(item)

        val result = viewModel.getAllElements()
        val deleted = result.find { it.id == item.id }

        assertEquals(initialSize - 1, result.size)
        assertNull(deleted)
    }

    companion object {
        private const val COUNT = 5
    }
}
