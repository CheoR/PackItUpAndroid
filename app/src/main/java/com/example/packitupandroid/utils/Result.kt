package com.example.packitupandroid.utils


/**
 * A sealed class representing the result of an operation that can either be a success, an error, or in a loading state.
 * This is commonly used for asynchronous operations like network requests or database interactions.
 *
 * @param T The type of the data returned in the case of success.
 */
sealed class Result<out T> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()
    data object Loading : Result<Nothing>()
}
