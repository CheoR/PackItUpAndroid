package com.example.packitupandroid.data.model

import androidx.compose.runtime.Composable
import com.example.packitupandroid.utils.EditFields
import java.util.Date
import java.util.UUID


data class QueryCollection(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val description: String? = null,
    val value: Double = 0.0,
    val is_fragile: Boolean = false,
    val last_modified: Date,
    val item_count: Int = 0,
    val box_count: Int = 0,
)

data class Collection(
    override val id: String = UUID.randomUUID().toString(),
    override val name: String,
    override val description: String? = null,
    override val value: Double = 0.0,
    override val isFragile: Boolean = false,
    override val lastModified: Date = Date(),
    val item_count: Int = 0,
    val box_count: Int = 0,
) : BaseCardData {
    companion object {
        val EDIT_FIELDS = setOf(
            EditFields.Name,
            EditFields.Description,
        )
    }
    override val editFields get() = EDIT_FIELDS
}
