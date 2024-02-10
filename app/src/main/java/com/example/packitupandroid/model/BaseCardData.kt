package com.example.packitupandroid.model

import com.example.packitupandroid.ui.components.card.EditFields

sealed interface BaseCardData {
    val id: String
    var name: String
    var description: String
    var isFragile: Boolean
    var value: Double
    val editFields: Set<EditFields>
}
