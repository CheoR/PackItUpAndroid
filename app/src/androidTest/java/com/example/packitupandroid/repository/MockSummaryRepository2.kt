package com.example.packitupandroid.repository

import com.example.packitupandroid.data.model.Summary
import com.example.packitupandroid.data.repository.SummaryRepository
import com.example.packitupandroid.utils.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map


class MockSummaryRepository2(loadDataSourceFromAssets: Boolean = true): SummaryRepository {
//    val testDataSource = TestDataSource()
//    val collections = testDataSource.collections
//    val boxes = testDataSource.boxes
//    val items = testDataSource.items

    val data = if(loadDataSourceFromAssets) {
//        Summary(
//            itemCount = items.size,
//            boxCount = boxes.size,
//            collectionCount = collections.size,
//            value = items.sumOf { it.value },
//            isFragile = items.any { it.isFragile }
//        )
        Summary(
            itemCount = 0,
            boxCount = 0,
            collectionCount = 0,
            value = 0.00,
            isFragile = false,
        )
    } else {
        Summary(
            itemCount = 0,
            boxCount = 0,
            collectionCount = 0,
            value = 0.00,
            isFragile = false,
        )
    }

    private val summaryFlow = MutableStateFlow<List<Summary>>(listOf(data))

    override fun observe(): Flow<Result<Summary?>> {
        return summaryFlow.map { listOfSummary -> Result.Success(listOfSummary.firstOrNull()) }
    }

    override fun getCollectionName(id: String): Result<String?> {
        return Result.Success("collection1") // collections.find { it.id == id }?.name)
    }

    override fun getBoxName(id: String): Result<String?> {
        return Result.Success("box1") // boxes.find { it.id == id }?.name)
    }

    override fun getItemName(id: String): Result<String?> {
        return Result.Success("item1") // items.find { it.id == id }?.name)
    }
}
