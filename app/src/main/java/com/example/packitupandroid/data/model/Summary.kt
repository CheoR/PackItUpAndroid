package com.example.packitupandroid.data.model


/**
 * Data class to hold the summary information from the database.
 *
 * This class represents a summary of the inventory data, including the total
 * number of items, boxes, and collections, the total value of all items, and
 * whether any item is marked as fragile.
 *
 * @property itemCount The total number of items.
 * @property boxCount The total number of boxes.
 * @property collectionCount The total number of collections.
 * @property value The total sum of all item values.
 * @property isFragile Whether any item is fragile.
 */
data class Summary(
    val itemCount: Int,
    val boxCount: Int,
    val collectionCount: Int,
    val value: Double,
    val isFragile: Boolean
)
