package com.example.packitupandroid.ui.test

import com.example.packitupandroid.assertSameExcept
import com.example.packitupandroid.ui.screens.collection.CollectionsScreenViewModel
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class CollectionsScreenViewModelTest {
    private lateinit var viewModel: CollectionsScreenViewModel
    private val mockRepository = MockCollectionsRepository()

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val coroutineRule = MainCoroutineRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        viewModel = CollectionsScreenViewModel(
            collectionsRepository = mockRepository,
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
    fun testCreateCollection() = runTest {
        val initialSize = viewModel.getAllElements().size

        viewModel.create(COUNT)
        val result = viewModel.getAllElements()

        assertEquals(initialSize + COUNT, result.size)
    }

    @Test
    fun testUpdateCollection() = runTest {
        val collection = viewModel.getAllElements().first()
        val updatedCollection = collection.copy(name = "Updated Collection 1")

        viewModel.update(updatedCollection)

        val result = viewModel.getAllElements().first()

        assertEquals(updatedCollection.name, result.name)
        assertSameExcept(updatedCollection, result, "name", "lastModified")
    }

    @Test
    fun testDestroyCollection() = runTest {
        val initialSize = viewModel.getAllElements().size
        val collection = viewModel.getAllElements().first()

        viewModel.destroy(collection)

        val result = viewModel.getAllElements()
        val deleted = result.find { it.id == collection.id }

        assertEquals(initialSize - 1, result.size)
        assertNull(deleted)
    }

//    @Test
//    fun testDestroyBox_destroyAssociatedItems() = runTest {
//        val box = viewModel.getAllBoxes().first()
//
//    }

    companion object {
        private const val COUNT = 5
        private const val TIMEOUT_MILLIS = 5_000L
    }
}
