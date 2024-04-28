package com.example.packitupandroid.data.repository

import com.example.packitupandroid.data.database.dao.SummaryDao
import com.example.packitupandroid.data.model.QuerySummary
import kotlinx.coroutines.flow.Flow

class OfflineSummaryRepository (private val summaryDao: SummaryDao): SummaryRepository {
    override fun getAllSummaryStream(): Flow<QuerySummary> = summaryDao.getSummary()
    override suspend fun clearAllSummary() = summaryDao.clearAllSummary()
}