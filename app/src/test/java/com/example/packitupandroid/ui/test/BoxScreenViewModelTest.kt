package com.example.packitupandroid.ui.test

import androidx.lifecycle.SavedStateHandle
import com.example.packitupandroid.assertSameExcept
import com.example.packitupandroid.data.model.Box
import com.example.packitupandroid.ui.screens.box.BoxesScreenViewModel
import com.example.packitupandroid.utils.Result
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class BoxScreenViewModelTest {
    private lateinit var viewModel: BoxesScreenViewModel
    private val repository = MockBoxesRepository()

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val coroutineRule = MainCoroutineRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        val savedStateHandle = SavedStateHandle()

        viewModel = BoxesScreenViewModel(
            savedStateHandle = savedStateHandle,
            repository = repository,
            defaultDispatcher = coroutineRule.testDispatcher,
        )

        viewModel.create(COUNT)
    }

    @Test
    fun testgetAllElements() = runTest {
        val result = (viewModel.elements.value as Result.Success<List<Box?>>).data
        assertEquals(COUNT, result.size)
    }

    @Test
    fun testCreateBox() = runTest {
        val initialSize = (viewModel.elements.value as Result.Success<List<Box?>>).data.size

        viewModel.create(COUNT)

        val result = (viewModel.elements.value as Result.Success<List<Box?>>).data
        assertEquals(initialSize + COUNT, result.size)
    }

    @Test
    fun testUpdateBox() = runTest {
        val result1 = (viewModel.elements.value as Result.Success<List<Box?>>).data
        val item1 = result1.first()
        val updatedBox = item1?.copy(name = "Updated Box 1")

        if (updatedBox != null) {
            viewModel.update(updatedBox)

            val result2 = (viewModel.elements.value as Result.Success<List<Box?>>).data
            val item2 = result2.first()


            assertEquals(updatedBox.name, item2?.name)
            assertSameExcept(updatedBox, result2, "name", "lastModified")
        }
    }

    @Test
    fun testDestroyBox() = runTest {
        val initialSize = (viewModel.elements.value as Result.Success<List<Box?>>).data.size

        val result1 = (viewModel.elements.value as Result.Success<List<Box?>>).data
        val item1 = result1.first()

        if(item1 != null) {
            viewModel.delete(listOf(item1.id))

//        coroutineRule.testDispatcher.scheduler.advanceUntilIdle()
//        coroutineRule.testDispatcher.scheduler.runCurrent()
//        coroutineRule.testDispatcher.scheduler.advanceTimeBy(initialSize * TIMEOUT_MILLIS)

            val result2 = (viewModel.elements.value as Result.Success<List<Box?>>).data
            val deleted = result2.find { it?.id == item1.id }

            assertEquals(initialSize - 1, result2.size)
            assertNull(deleted)
        }
    }

//    @Test
//    fun testDestroyBox_destroyAssociatedItems() = runTest {
//        val box = viewModel.getAllElements().first()
//
//    }

    companion object {
        private const val COUNT = 5
        private const val TIMEOUT_MILLIS = 5_000L
    }
}
