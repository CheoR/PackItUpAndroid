package com.example.packitupandroid.data.model

import com.example.packitupandroid.utils.EditFields
import java.util.Date

sealed interface BaseCardData {
    val id: String
    val name: String
    val description: String?
    val isFragile: Boolean
    val value: Double
    val editFields: Set<EditFields>
    val lastModified: Date
}
