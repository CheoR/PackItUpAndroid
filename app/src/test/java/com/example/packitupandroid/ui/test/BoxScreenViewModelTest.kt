package com.example.packitupandroid.ui.test

import androidx.lifecycle.SavedStateHandle
import com.example.packitupandroid.assertSameExcept
import com.example.packitupandroid.ui.screens.box.BoxesScreenViewModel
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class BoxScreenViewModelTest {
    private lateinit var viewModel: BoxesScreenViewModel
    private val mockRepository = MockBoxesRepository()

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val coroutineRule = MainCoroutineRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        val savedStateHandle = SavedStateHandle()

        viewModel = BoxesScreenViewModel(
            savedStateHandle = savedStateHandle,
            boxesRepository = mockRepository,
            defaultDispatcher = coroutineRule.testDispatcher,
        )

        viewModel.create(COUNT)
    }

    @Test
    fun testgetAllElements() = runTest {
        val result = viewModel.getAllElements()
        assertEquals(COUNT, result.size)
    }

    @Test
    fun testCreateBox() = runTest {
        val initialSize = viewModel.getAllElements().size

        viewModel.create(COUNT)
        val result = viewModel.getAllElements()

        assertEquals(initialSize + COUNT, result.size)
    }

    @Test
    fun testUpdateBox() = runTest {
        val box = viewModel.getAllElements().first()
        val updatedBox = box.copy(name = "Updated Box 1")

        viewModel.update(updatedBox)

        val result = viewModel.getAllElements().first()

        assertEquals(updatedBox.name, result.name)
        assertSameExcept(updatedBox, result, "name", "lastModified")
    }

    @Test
    fun testDestroyBox() = runTest {
        val initialSize = viewModel.getAllElements().size
        val box = viewModel.getAllElements().first()

        viewModel.destroy(box)

//        coroutineRule.testDispatcher.scheduler.advanceUntilIdle()
//        coroutineRule.testDispatcher.scheduler.runCurrent()
//        coroutineRule.testDispatcher.scheduler.advanceTimeBy(initialSize * TIMEOUT_MILLIS)

        val result = viewModel.getAllElements()
        val deleted = result.find { it.id == box.id }

        assertEquals(initialSize - 1, result.size)
        assertNull(deleted)
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
