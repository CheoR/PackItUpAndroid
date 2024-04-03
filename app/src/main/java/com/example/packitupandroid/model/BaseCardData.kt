package com.example.packitupandroid.model

import com.example.packitupandroid.ui.components.card.EditFields
import java.util.Date

sealed interface BaseCardData {
    val id: String
    val name: String
    val description: String
    val isFragile: Boolean
    val value: Double
    val editFields: Set<EditFields>
    val lastModified: Date
}
