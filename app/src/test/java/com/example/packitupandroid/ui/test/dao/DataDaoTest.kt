package com.example.packitupandroid.ui.test.dao

import com.example.packitupandroid.ui.test.MainCoroutineRule
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test


@OptIn(ExperimentalCoroutinesApi::class)
class DataDaoTest {

    @get:Rule
    val coroutineRule = MainCoroutineRule()

    private lateinit var dao: MockData.MockDataDao
    private val testDispatcher: TestDispatcher = StandardTestDispatcher()

    private val item1 = MockData(id = "1", name = "Item 1")
    private val item2 = MockData(id = "2", name = "Item 2")
    private val item3 = MockData(id = "3", name = "Item 3")

    @Before
    fun setup() {
        dao = MockDataDao()
    }

    @Test
    fun test_get_item_exists() = runTest {
        dao.insert(item1)
        val result = dao.get(item1.id)
        assertEquals(item1, result)
    }

    @Test
    fun test_get_item_does_not_exist() = runTest {
        val result = dao.get("nonexistent")
        assertNull(result)
    }

    @Test
    fun test_observe_item_exists() = runTest {
        dao.insert(item1)
        val result = dao.observe(item1.id).first()
        assertEquals(item1, result)
    }

    @Test
    fun test_observe_item_does_not_exist() = runTest {
        val result = dao.observe("nonexistent").first()
        assertNull(result)
    }

    @Test
    fun test_observe_item_updates() = runTest {
        dao.insert(item1)
        val flow = dao.observe(item1.id)
        val initialResult = flow.first()
        assertEquals(item1, initialResult)

        val updatedItem = item1.copy(name = "Updated Item 1")
        dao.insert(updatedItem)
        advanceUntilIdle()

        val updatedResult = flow.first()
        assertEquals(updatedItem, updatedResult)
    }

    @Test
    fun test_observeAll_empty() = runTest {
        val result = dao.observeAll().first()
        assertEquals(emptyList<MockData>(), result)
    }

    @Test
    fun test_observeAll_multiple_items() = runTest {
        dao.insert(listOf(item1, item2, item3))
        val result = dao.observeAll().first()
        assertEquals(listOf(item1, item2, item3), result)
    }

    @Test
    fun test_observeAll_updates() = runTest {
        dao.insert(listOf(item1, item2))
        val flow = dao.observeAll()
        val initialResult = flow.first()
        assertEquals(listOf(item1, item2), initialResult)

        dao.insert(item3)
        advanceUntilIdle()

        val updatedResult = flow.first()
        assertEquals(listOf(item1, item2, item3), updatedResult)
    }

    @Test
    fun test_clear() = runTest {
        dao.insert(listOf(item1, item2, item3))
        dao.clear()
        val result = dao.observeAll().first()
        assertEquals(emptyList<MockData>(), result)
    }

    @Test
    fun test_delete_single_item() = runTest {
        dao.insert(listOf(item1, item2, item3))
        dao.delete(listOf(item2.id))
        val result = dao.observeAll().first()
        assertEquals(listOf(item1, item3))
    }
}
