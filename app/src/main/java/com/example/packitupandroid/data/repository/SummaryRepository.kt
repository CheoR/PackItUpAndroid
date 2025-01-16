package com.example.packitupandroid.data.repository

import com.example.packitupandroid.data.model.Summary
import com.example.packitupandroid.utils.Result
import kotlinx.coroutines.flow.Flow


/**
 * Repository interface for accessing summary data.
 *
 * This interface defines the contract for a repository that provides access to
 * summary information about the application's data. It provides a stream of
 * [Summary] objects, allowing for asynchronous updates whenever the underlying
 * data changes.
 */
interface SummaryRepository {
    /**
     * Returns a [Flow] that emits the current [Summary].
     *
     * This function provides a stream of [Summary] objects, allowing for
     * asynchronous updates whenever the underlying data changes.
     *
     * @return A [Flow] that emits the current [Summary], wrapped in a [Result].
     */
    fun observe(): Flow<Result<Summary?>>
}
