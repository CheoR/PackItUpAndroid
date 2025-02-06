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

    /**
     * Retrieves the collection name associated with the given ID.
     *
     * The mapping from ID to collection name is implementation-specific.
     *
     * @param id The ID to look up.
     * @return A `Result` containing:
     *   - `Success(String?)`: The collection name, or `null` if no collection is found for the ID.
     *   - `Error(Throwable)`: An error that occurred during lookup.
     */
    fun getCollectionName(id: String) : Result<String?>

    /**
     * Retrieves the box name associated with the given ID.
     *
     * The mapping from ID to box name is implementation-specific.
     *
     * @param id The ID to look up.
     * @return A `Result` containing:
     *   - `Success(String?)`: The box name, or `null` if no box is found for the ID.
     *   - `Error(Throwable)`: An error that occurred during lookup.
     */
    fun getBoxName(id: String) : Result<String?>

    /**
     * Retrieves the item name associated with the given ID.
     *
     * The mapping from ID to item name is implementation-specific.
     *
     * @param id The ID to look up.
     * @return A `Result` containing:
     *   - `Success(String?)`: The item name, or `null` if no item is found for the ID.
     *   - `Error(Throwable)`: An error that occurred during lookup.
     */
    fun getItemName(id: String) : Result<String?>
}
