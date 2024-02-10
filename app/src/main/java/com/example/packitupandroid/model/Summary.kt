package com.example.packitupandroid.model

import com.example.packitupandroid.ui.components.card.EditFields

data class Summary (
    override val id: String,
    override var name: String,
    override var description: String = "",
    val collections: List<Collection> = emptyList(),
    val boxes: List<Box> = emptyList(),
    val items: List<Item> = emptyList(),
) : BaseCardData {
    override var isFragile: Boolean = items.any { it.isFragile }
    override var value: Double = items.sumOf { it.value }
    override val editFields: Set<EditFields> = emptySet()
}
