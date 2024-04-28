package com.example.packitupandroid.data.repository

import com.example.packitupandroid.data.model.QuerySummary
import kotlinx.coroutines.flow.Flow

interface SummaryRepository {
    /**
     * Retrieve all the Summary from the given data source.
     */
    fun getAllSummaryStream(): Flow<QuerySummary>

    /**
     * Clear all summary in the data source
     */
    suspend fun clearAllSummary()
}