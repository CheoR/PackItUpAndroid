package com.example.packitupandroid.data.local

import com.example.packitupandroid.R
import com.example.packitupandroid.model.Box
import com.example.packitupandroid.model.Collection
import com.example.packitupandroid.model.Item

class LocalDataSource() {
    private fun itemsAndValue(start: Int, stop: Int) : Pair<List<Item>, Double> {
        /**
         * Return sublist of [Item] and sublist total value.
         */

        val itemSublist = loadItems().slice( start .. stop )
        val value = itemSublist.sumOf { it.value }
        return Pair(itemSublist, value)
    }

    private fun boxesAndValue(start: Int, stop: Int) : Pair<List<Box>, Double> {
        /**
         * Return sublist of [Box] and sublist total value.
         */

        val boxSublist = loadBoxes().slice( start .. stop )
        val value = boxSublist.sumOf { it.value}

        return Pair(boxSublist, value)
    }

    fun loadItems(): List<Item> {
        return listOf<Item> (
            Item(
                id = 100L,
                name = "Peggy PUg",
                description = "Cutest little pug in the world",
                isFragile = true,
                value = 100000000000000.00,
                imageUri = R.drawable.pug,
            ),
            Item(
                id = 0L,
                name = "pot",
                description = "not the weed kind",
                isFragile = true,
                value = 5.00,
            ),
            Item(
                id = 1L,
                name = "pan",
                description = "not the weed kind",
                isFragile = true,
                value = 10.00,
            ),
            Item(
                id = 2L,
                name = "fork",
                description = "because i like plastic forks",
                value = 0.85,
            ),
            Item(
                id = 3L,
                name = "shoes",
                description = "from walmart but comfy",
                value = 6.50,
            ),
            Item(
                id = 4L,
                name = "shirt",
                value = 7.50,
            ),
            Item(
                id = 5L,
                name = "ties",
                value = 0.85,
            ),
            Item(
                id = 6L,
                name = "soap",
                value = 25.00,
            ),
            Item(
                id = 7L,
                name = "shampoo",
                value = 7.50,
            ),
            Item(
                id = 8L,
                name = "toilette paper",
                value = 45.50,
            ),
            Item(
                id = 9L,
                name = "tools",
                description = "not for breaking into homes",
                isFragile = true,
                value = 100.00,
            ),
            Item(
                id = 10L,
                name = "10L",
                description = "not for breaking into homes",
                isFragile = true,
                value = 125.00,
            ),
            Item(
                id = 11L,
                name = "11L",
                description = "not for breaking into homes",
                isFragile = true,
                value = 3.00,
            ),
            Item(
                id = 12L,
                name = "12L",
                description = "not for breaking into homes",
                isFragile = true,
                value = 100.00,
            ),
            Item(
                id = 13L,
                name = "13L",
                description = "not for breaking into homes",
                isFragile = true,
                value = 125.00,
            ),
            Item(
                id = 14L,
                name = "14L",
                description = "not for breaking into homes",
                isFragile = true,
                value = 3.00,
            ),
        )
    }

    fun loadBoxes(): List<Box> {
        return listOf<Box>(
            Box(
                id = 0L,
                name = "kitchen",
                items = itemsAndValue(0,3).first,
                description = "don't open until i get home",
            ),
            Box(
                id = 1L,
                name = "bedroom1",
                items = itemsAndValue(3,5).first,
            ),
            Box(
                id = 2L,
                name = "bedroom2",
                items = itemsAndValue(6,9).first,
            ),
            Box(
                id = 3L,
                name = "garage",
                items = itemsAndValue(10,12).first,
            ),
            Box(
                id = 4L,
                name = "garage",
                items = listOf(),
            ),
            Box(
                id = 5L,
                name = "garage",
                items = itemsAndValue(12,15).first,
            ),
        )
    }

    fun loadCollections(): List<Collection> {
        return listOf<Collection>(
            Collection(
                id = 0L,
                name = "for home",
                boxes = boxesAndValue(0,2).first,
            ),
            Collection(
                id = 1L,
                name = "for donation",
                boxes = boxesAndValue(3,3).first,
            ),
            Collection(
                id = 2L,
                name = "not sure yet",
                boxes = boxesAndValue(4,4).first,
            ),
            Collection(
                id = 3L,
                name = "3L",
                boxes = listOf(),
            ),
            Collection(
                id = 4L,
                name = "4L not sure yet",
                boxes = boxesAndValue(5,5).first,
            ),
        )
    }
}