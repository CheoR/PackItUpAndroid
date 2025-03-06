package com.example.packitupandroid.data.database.dao

import com.example.packitupandroid.utils.EditFields
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import java.util.Date
import java.util.UUID


data class MockData(
    override val id: String = UUID.randomUUID().toString(),
    override val name: String,
    override val description: String? = null,
    override val value: Double = 0.0,
    override val isFragile: Boolean = false,
    override val lastModified: Date = Date(),
) : BaseCardData {
    // Mock DAO
    class MockDataDao : DataDao<MockData> {
        private val data = mutableMapOf<String, MockData>()
        private val dataFlow = MutableStateFlow<List<MockData?>>(emptyList())

        override fun get(id: String): MockData? {
            return data[id]
        }

        override fun observe(id: String): Flow<MockData?> {
            return dataFlow.map { list -> list.firstOrNull { it?.id == id } }
        }

        override fun observeAll(): Flow<List<MockData?>> {
            return dataFlow
        }

        override suspend fun clear() {
            data.clear()
            dataFlow.value = data.values.toList()
        }

        override suspend fun delete(ids: List<String>) {
            ids.forEach { data.remove(it) }
            dataFlow.value = data.values.toList()
        }

        fun insert(item: MockData) {
            data[item.id] = item
            dataFlow.value = data.values.toList()
        }

        fun insert(items: List<MockData>) {
            items.forEach { data[it.id] = it }
            dataFlow.value = data.values.toList()
        }
    }
    override val editFields: Set<EditFields> = setOf()
}
