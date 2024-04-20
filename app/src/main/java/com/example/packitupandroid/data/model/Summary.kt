package com.example.packitupandroid.data.model

import com.example.packitupandroid.utils.EditFields
import java.util.Date

data class QuerySummary(
    val value: Double = 0.0,
    val is_fragile: Boolean = false,
    val item_count: Int = 0,
    val box_count: Int = 0,
    val collection_count: Int = 0,
)

fun QuerySummary.toSummary(
    name: String
): Summary = Summary(
    id = name,
    name = name,
    isFragile = this.is_fragile,
    value = this.value,
    itemCount = this.item_count,
    boxCount = this.box_count,
    collectionCount = this.collection_count,
)
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

