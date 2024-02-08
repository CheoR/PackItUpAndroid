package com.example.packitupandroid.ui.utils

import com.example.packitupandroid.ui.components.card.BaseCardData

fun extractBaseCardData(data: BaseCardData): List<Any> {
    var id: String
    var name: String
    var description: String
    var isFragile: Boolean
    var value: Double

    when (data) {
        is BaseCardData.ItemData -> {
            id = data.item.id
            name = data.item.name
            description = data.item.description
            isFragile = data.item.isFragile
            value = data.item.value
        }

        is BaseCardData.BoxData -> {
            id = data.box.id
            name = data.box.name
            description = data.box.description
            isFragile = data.box.isFragile
            value = data.box.value
        }

        is BaseCardData.CollectionData -> {
            id = data.collection.id
            name = data.collection.name
            description = data.collection.description
            isFragile = data.collection.isFragile
            value = data.collection.value
        }

        is BaseCardData.SummaryData -> {
            id = data.summary.id
            name = data.summary.name
            description = data.summary.description
            isFragile = data.summary.isFragile
            value = data.summary.value
        }
    }

    return listOf(id, name, description, isFragile, value)
}
