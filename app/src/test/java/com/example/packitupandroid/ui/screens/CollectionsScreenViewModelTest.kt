package com.example.packitupandroid.ui.screens

import androidx.lifecycle.SavedStateHandle
import com.example.packitupandroid.assertSameExcept
import com.example.packitupandroid.data.model.Collection
import com.example.packitupandroid.data.repository.MockCollectionsRepository
import com.example.packitupandroid.ui.screens.collection.CollectionsScreenViewModel
import com.example.packitupandroid.MainCoroutineRule
import com.example.packitupandroid.data.repository.MockBoxesRepository
import com.example.packitupandroid.data.repository.MockItemsRepository
import com.example.packitupandroid.utils.Result
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class CollectionsScreenViewModelTest {
    private lateinit var itemsRepository: MockItemsRepository
    private lateinit var boxesRepository: MockBoxesRepository
    private lateinit var viewModel: CollectionsScreenViewModel
    private val repository = MockCollectionsRepository(
        boxesRepository = boxesRepository,
        itemsRepository = itemsRepository,
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val coroutineRule = MainCoroutineRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        val savedStateHandle = SavedStateHandle()

        viewModel = CollectionsScreenViewModel(
            repository = repository,
            defaultDispatcher = coroutineRule.testDispatcher,
            savedStateHandle = savedStateHandle,
        )

        viewModel.create(COUNT)
    }

    @Test
    fun testgetAllElements() = runTest {
        val result = Result.Success(viewModel.elements.value as List<Collection>).data
        assertEquals(COUNT, result.size)
    }

    @Test
    fun testCreateCollections() = runTest {
        val initialSize = Result.Success(viewModel.elements.value as List<Collection>).data.size

        viewModel.create(COUNT)

        val result = Result.Success(viewModel.elements.value as List<Collection>).data
        assertEquals(initialSize + COUNT, result.size)
    }

    @Test
    fun testUpdateCollection() = runTest {
        val result1 = Result.Success(viewModel.elements.value as List<Collection>).data
        val item1 = result1.first()
        val updatedCollection = item1.copy(name = "Updated Collection 1")

        viewModel.update(updatedCollection)

        val result2 = Result.Success(viewModel.elements.value as List<Collection>).data
        val item2 = result2.first()


        assertEquals(updatedCollection.name, item2.name)
        assertSameExcept(updatedCollection, result2, "name", "lastModified")
    }

    @Test
    fun testDestroyCollection() = runTest {
        val initialSize = Result.Success(viewModel.elements.value as List<Collection>).data.size

        val result1 = Result.Success(viewModel.elements.value as List<Collection>).data
        val item1 = result1.first()

        viewModel.delete(listOf(item1.id))

        val result2 = Result.Success(viewModel.elements.value as List<Collection>).data
        val deleted = result2.find { it.id == item1.id }

        assertEquals(initialSize - 1, result2.size)
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
