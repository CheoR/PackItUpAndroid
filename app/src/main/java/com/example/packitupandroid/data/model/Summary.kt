package com.example.packitupandroid.data.model

import com.example.packitupandroid.utils.EditFields
import java.util.Date


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
 * Represents a summary of data, providing aggregated counts and general information.
 *
 * This data class encapsulates a summary view of underlying data, such as items, boxes, and collections.
 * It extends [BaseCardData] and provides properties for describing the summary, along with
 * counts for related entities.
 *
 * @property id The unique identifier for this summary. Inherited from [BaseCardData].
 * @property name The name or title of this summary. Inherited from [BaseCardData].
 * @property description An optional description providing more context for this summary. Inherited from [BaseCardData].
 * @property isFragile Indicates if the data represented by this summary is considered fragile or sensitive. Inherited from [BaseCardData].
 * @property value A numerical value associated with this summary, such as a total cost or score. Inherited from [BaseCardData].
 * @property editFields A set of [EditFields] indicating which fields of the underlying data can be edited. Inherited from [BaseCardData].
 * @property lastModified The date and time when this summary was last modified. Inherited from [BaseCardData].
 *                      **Note:** This field might be removed or adjusted in future versions (see TODO).
 * @property itemCount The total number of items included in this summary.
 * @property boxCount The total number of boxes associated with this summary.
 * @property collectionCount The total number of collections represented in this summary.
 *
 * **TODO:** Update the interface [BaseCardData] to remove unneeded fields, such as `lastModified`, if they are not relevant for all implementations.
 */
data class Summary (
    override val id: String,
    override val name: String,
    override val description: String? = null,
    override val isFragile: Boolean = false,
    override val value: Double = 0.0,
    override val editFields: Set<EditFields> = emptySet(),
    override val lastModified: Date = Date(),
    // TODO: Fix - update interface to remove unneeded fields e.g. lastModified
    val itemCount: Int = 0,
    val boxCount: Int = 0,
    val collectionCount: Int = 0,
) : BaseCardData
