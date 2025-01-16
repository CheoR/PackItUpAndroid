package com.example.packitupandroid.data.model


/**
 * Represents a summary of a query result, providing aggregated information about the data.
 *
 * This data class encapsulates several key metrics derived from a query, including a numeric
 * value, indicators of potential fragility, and counts of different item types.
 *
 * @property value A numeric value representing a primary result of the query. This could be
 *                  a score, a total, an average, or any other relevant numerical measure.
 *                  Defaults to 0.0.
 * @property is_fragile A boolean flag indicating whether the data or the process that generated
 *                      it might be unreliable or sensitive to changes.  A value of `true`
 *                      suggests that the results should be interpreted with caution. Defaults
 *                      to `false`.
 * @property item_count The total number of individual items included in the query results.
 *                     Defaults to 0.
 * @property box_count The total number of "boxes" or containers represented in the query
 *                    results. This is used when items are grouped into larger units.
 *                    Defaults to 0.
 * @property collection_count The total number of distinct collections represented in the
 *                           query results.  This provides insight into the diversity of
 *                           the data. Defaults to 0.
 */
data class QuerySummary(
    val value: Double = 0.0,
    val is_fragile: Boolean = false,
    val item_count: Int = 0,
    val box_count: Int = 0,
    val collection_count: Int = 0,
)

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
