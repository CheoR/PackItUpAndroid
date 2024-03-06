package com.example.packitupandroid.model

import com.example.packitupandroid.ui.components.card.EditFields

data class Summary (
    override val id: String,
    override val name: String,
    override val description: String = "",
    val collections: List<Collection> = emptyList(),
    val boxes: List<Box> = emptyList(),
    val items: List<Item> = emptyList(),
    override val isFragile: Boolean = items.any { it.isFragile },
    override val value: Double = items.sumOf { it.value },
    override val editFields: Set<EditFields> = emptySet(),
) : BaseCardData