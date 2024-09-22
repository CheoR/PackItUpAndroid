package com.example.packitupandroid.ui.test

import com.example.packitupandroid.data.database.entities.BoxEntity
import com.example.packitupandroid.data.database.entities.toBox
import com.example.packitupandroid.data.model.QueryBox
import com.example.packitupandroid.data.model.QueryDropdownOptions
import com.example.packitupandroid.data.model.toQueryBox
import com.example.packitupandroid.data.repository.BoxesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

class MockBoxesRepository: BoxesRepository {
    private val boxes = mutableListOf<BoxEntity>()
    private val boxFlow = MutableStateFlow<List<BoxEntity>>(emptyList())

    override suspend fun getBox(id: String): BoxEntity? {
        return this.boxes.find { it.id == id }
    }

    override suspend fun getQueryBox(id: String): QueryBox? {
        return this.getBox(id)?.toBox()?.toQueryBox()
    }

    override fun getAllBoxesStream(): Flow<List<QueryBox>> {
        return flowOf(this.boxes.map { it.toBox().toQueryBox() })
    }

    override suspend fun getDropdownSelections(): Flow<List<QueryDropdownOptions>> {
        return flowOf(boxes.map { it -> QueryDropdownOptions(it.id, it.name) })
    }

    override fun getBoxStream(id: String): Flow<BoxEntity?> {
        return boxFlow.map { it.find { box -> box.id == id } }
    }

    override suspend fun insertBox(box: BoxEntity) {
        this.boxes.add(box)
        boxFlow.value = this.boxes.toList()
    }

    override suspend fun insertAll(boxes: List<BoxEntity>) {
        this.boxes.addAll(boxes)
        boxFlow.value = this.boxes
    }

    override suspend fun updateBox(box: BoxEntity) {
        val index = boxes.indexOfFirst { it.id == box.id }
        if (index != -1) {
            this.boxes[index] = box
            boxFlow.value = this.boxes
        }
    }

    override suspend fun deleteBox(box: BoxEntity) {
        this.boxes.remove(box)
        boxFlow.value = this.boxes
    }

    override suspend fun deleteAll(boxes: List<BoxEntity>) {
        this.boxes.removeAll(boxes)
        boxFlow.value = this.boxes.toList()
    }

    override suspend fun clearAllBoxes() {
        this.boxes.clear()
        boxFlow.value = this.boxes
    }
}
