package com.example.packitupandroid.data.local

import com.example.packitupandroid.model.Box
import com.example.packitupandroid.model.Item
import com.example.packitupandroid.model.Collection
//import com.example.packitupandroid.model.hasFragileItem


//fun List<Item>.hasFragileItems() : Boolean {
//    /**
//     * Check if any [Item] in [Box]'s items list are marked as fragile.
//     */
//    return this.any{ it.isFragile }
//}
//
//fun List<Box>.hasFragileBoxes() : Boolean {
//    /**
//     * Check if any [Box] in [Collection]'s box list are marked as fragile.
//     */
//    return this.any{ it.isFragile }
//}

class LocalDataSource() {
    private fun itemsAndTotalValue(start: Int, stop: Int) : Pair<List<Item>, Double> {
        /**
         * Return sublist of [Item] and sublist total value.
         */

        val itemSublist = loadItems().slice( start .. stop )
        val totalValue = itemSublist.sumOf { it.value }
        return Pair(itemSublist, totalValue)
    }

    private fun boxesAndTotalValue(start: Int, stop: Int) : Pair<List<Box>, Double> {
        /**
         * Return sublist of [Box] and sublist total value.
         */

        val boxSublist = loadBoxes().slice( start .. stop )
        val totalValue = boxSublist.sumOf { it.totalValue }

        return Pair(boxSublist, totalValue)
    }

    fun loadItems(): List<Item> {
        return listOf<Item> (
            Item(
                id = "0L",
                name = "pot",
                description = "not the weed kind",
                isFragile = true,
                value = 5.00,
            ),
            Item(
                id = "1L",
                name = "pan",
                description = "not the weed kind",
                isFragile = true,
                value = 10.00,
            ),
            Item(
                id = "2L",
                name = "fork",
                description = "because i like plastic forks",
                value = 0.85,
            ),
            Item(
                id = "3L",
                name = "shoes",
                description = "from walmart but comfy",
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
                description = "not for breaking into homes",
                isFragile = true,
                value = 100.00,
            ),
        )
    }

    fun loadBoxes(): List<Box> {
        return listOf<Box>(
            Box(
                id = "0L",
                name = "kitchen",
//                items = loadItems().take(3),
//                totalValue = loadItems().take(3).sumOf { it.value }
                items = itemsAndTotalValue(0,3).first,
//                totalValue = itemsAndTotalValue(0,3).second,
                description = "don't open until i get home",
//                isFragile = itemsAndTotalValue(0,3).first.hasFragileItems(),
            ),
            Box(
                id = "1L",
                name = "bedroom1",
                items = itemsAndTotalValue(3,5).first,
//                totalValue = itemsAndTotalValue(3,5).second,
//                isFragile = itemsAndTotalValue(3,5).first.hasFragileItems(),
            ),
            Box(
                id = "2L",
                name = "bedroom2",
                items = itemsAndTotalValue(6,9).first,
//                totalValue = itemsAndTotalValue(6,9).second,
//                isFragile = itemsAndTotalValue(6,9).first.hasFragileItems(),
            ),
            Box(
                id = "3L",
                name = "garage",
                items = listOf(),
//                totalValue = 0.00,
            ),
        )
    }

    fun loadCollections(): List<Collection> {
        return listOf<Collection>(
            Collection(
                id = "0L",
                name = "for home",
                boxes = boxesAndTotalValue(0,2).first,
//                totalValue = boxesAndTotalValue(0,2).second,
//                isFragile = boxesAndTotalValue(0,2).first.hasFragileBoxes(),
            ),
            Collection(
                id = "1L",
                name = "for donation",
                boxes = boxesAndTotalValue(3,3).first,
//                totalValue = boxesAndTotalValue(3,3).second,
//                isFragile = boxesAndTotalValue(3,3).first.hasFragileBoxes(),
            ),
            Collection(
                id = "2L",
                name = "not sure yet",
                boxes = listOf(),
//                totalValue = 0.00,
            ),
        )
    }
}