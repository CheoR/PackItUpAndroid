package com.example.packitupandroid.data.repository

import com.example.packitupandroid.data.database.dao.SummaryDao
import com.example.packitupandroid.utils.Result
import kotlinx.coroutines.flow.Flow
import com.example.packitupandroid.data.model.Summary
import kotlinx.coroutines.flow.map


/**
 * Concrete implementation of [SummaryRepository] that fetches data from the local database.
 *
 * This class provides an offline data access implementation for [Summary] data,
 * using the [SummaryDao] to perform data operations on the local database.
 *
 * @param summaryDao The [SummaryDao] instance used to access the database.
 */
class OfflineSummaryRepository(private val summaryDao: SummaryDao): SummaryRepository {

    /**
     * Returns a [Flow] that emits the current [Summary] wrapped in a [Result].
     *
     * This function fetches the summary data from the database using
     * [SummaryDao.observe()], and maps the result to a [Result.Success]
     * containing the [Summary] object.
     *
     * @return A [Flow] that emits [Result.Success] with the current [Summary] or an error.
     */
    override fun observe(): Flow<Result<Summary?>> {
        return summaryDao.observe().map {
            Result.Success(it)
        }
    }
}
