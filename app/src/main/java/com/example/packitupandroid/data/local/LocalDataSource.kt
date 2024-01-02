package com.example.packitupandroid.data.local

import com.example.packitupandroid.model.Box
import com.example.packitupandroid.model.Item
import com.example.packitupandroid.model.Collection
class LocalDataSource() {
    fun loadItems(): List<Item> {
        return listOf<Item> (
            Item(
                id = "0L",
                name = "pot",
                value = 5.00,
            ),
            Item(
                id = "1L",
                name = "pan",
                value = 10.00,
            ),
            Item(
                id = "2L",
                name = "fork",
                value = 0.85,
            ),
            Item(
                id = "3L",
                name = "shoes",
                value = 6.50,
            ),
            Item(
                id = "4L",
                name = "shirt",
                value = 7.50,
            ),
            Item(
                id = "5L",
                name = "ties",
                value = 0.85,
            ),
            Item(
                id = "6L",
                name = "soap",
                value = 25.00,
            ),
            Item(
                id = "7L",
                name = "shampoo",
                value = 7.50,
            ),
            Item(
                id = "8L",
                name = "toilette paper",
                value = 45.50,
            ),
            Item(
                id = "9L",
                name = "tools",
                value = 100.00,
            ),
        )
    }

    private fun itemsAndTotalValue(start: Int, stop: Int) : Pair<List<Item>, Double> {
        /**
         * Return sublist of [Item] and sublist toal value.
         */

        val itemSublist = loadItems().slice( start .. stop )
        val totalValue = itemSublist.sumOf { it.value }
        return Pair(itemSublist, totalValue)
    }

    private fun boxesAndTotalValue(start: Int, stop: Int) : Pair<List<Box>, Double> {
        /**
         * Return sublist of [Box] and sublist toal value.
         */

        val boxSublist = loadBoxes().slice( start .. stop )
        val totalValue = boxSublist.sumOf { it.totalValue }

        return Pair(boxSublist, totalValue)
    }
    fun loadBoxes(): List<Box> {
        return listOf<Box>(
            Box(
                id = "0L",
                name = "kitchen",
//                items = loadItems().take(3),
//                totalValue = loadItems().take(3).sumOf { it.value }
                items = itemsAndTotalValue(0,3).first,
                totalValue = itemsAndTotalValue(0,3).second
            ),
            Box(
                id = "1L",
                name = "bedroom1",
                items = itemsAndTotalValue(3,5).first,
                totalValue = itemsAndTotalValue(3,5).second
            ),
            Box(
                id = "2L",
                name = "bedroom2",
                items = itemsAndTotalValue(6,9).first,
                totalValue = itemsAndTotalValue(6,9).second
            ),
            Box(
                id = "3L",
                name = "garage",
                items = listOf(),
                totalValue = 0.00
            ),
        )
    }

    fun loadCollections(): List<Collection> {
        return listOf<Collection>(
            Collection(
                id = "0L",
                name = "for home",
                boxes = boxesAndTotalValue(0,2).first,
                totalValue = boxesAndTotalValue(0,2).second,
            ),
            Collection(
                id = "1L",
                name = "for donation",
                boxes = boxesAndTotalValue(3,3).first,
                totalValue = boxesAndTotalValue(3,3).second,
            ),
            Collection(
                id = "2L",
                name = "not sure yet",
                boxes = listOf(),
                totalValue = 0.00,
            ),
        )
    }
}