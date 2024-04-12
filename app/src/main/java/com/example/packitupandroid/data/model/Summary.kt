package com.example.packitupandroid.data.model

import com.example.packitupandroid.utils.EditFields
import java.util.Date
import kotlin.collections.Collection

data class Summary (
    override val id: String,
    override val name: String,
    override val description: String? = null,
    val collections: List<Collection> = emptyList(),
    val boxes: List<Box> = emptyList(),
    val items: List<Item> = emptyList(),
    override val isFragile: Boolean = items.any { it.isFragile },
    override val value: Double = items.sumOf { it.value },
    override val editFields: Set<EditFields> = emptySet(),
    override val lastModified: Date = Date(),
) : BaseCardData
