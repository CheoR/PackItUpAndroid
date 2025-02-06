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

    /**
     * Retrieves the collection name associated with a given ID.
     *
     * This function delegates to the `summaryDao` to fetch the collection name.
     * It then wraps the result in a `Result` object to handle potential errors or null values.
     *
     * @param id The ID for which to retrieve the collection name.
     * @return A `Result` object.
     *   - `Result.Success(String?)`: If successful, contains the collection name as a String or null if not found.
     *   - `Result.Error`: If an error occurs during the data retrieval process. (Note that in the current implementation, an error is not explicitly handled and null will be return if the database failed.)
     */
    override fun getCollectionName(id: String): Result<String?> {
        return summaryDao.getCollectionName(id).let { name ->
            Result.Success(name)
        }
    }

    /**
     * Retrieves the box name associated with a given ID.
     *
     * This function delegates to the `summaryDao` to fetch the box name.
     * It then wraps the result in a `Result` object to handle potential errors or null values.
     *
     * @param id The ID for which to retrieve the box name.
     * @return A `Result` object.
     *   - `Result.Success(String?)`: If successful, contains the box name as a String or null if not found.
     *   - `Result.Error`: If an error occurs during the data retrieval process. (Note that in the current implementation, an error is not explicitly handled and null will be return if the database failed.)
     */
    override fun getBoxName(id: String): Result<String?> {
        return summaryDao.getBoxName(id).let { name ->
            Result.Success(name)
        }
    }

    /**
     * Retrieves the item name associated with a given ID.
     *
     * This function delegates to the `summaryDao` to fetch the item name.
     * It then wraps the result in a `Result` object to handle potential errors or null values.
     *
     * @param id The ID for which to retrieve the item name.
     * @return A `Result` object.
     *   - `Result.Success(String?)`: If successful, contains the item name as a String or null if not found.
     *   - `Result.Error`: If an error occurs during the data retrieval process. (Note that in the current implementation, an error is not explicitly handled and null will be return if the database failed.)
     */
    override fun getItemName(id: String): Result<String?> {
        return summaryDao.getItemName(id).let { name ->
            Result.Success(name)
        }
    }
}
