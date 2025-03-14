package com.example.packitupandroid.source.local

import com.example.packitupandroid.data.model.Box
import com.example.packitupandroid.data.model.BoxIdAndName
import com.example.packitupandroid.data.model.Collection
import com.example.packitupandroid.data.model.Item
import java.util.Date


class TestDataSource() {
    val items = LocalDataSource().loadItems()
    val boxes = LocalDataSource().loadBoxes().map {
        val (updatedValue, updatedItemCount, updatedIsFragile) = items
            .filter { item -> item.boxId == it.id }
            .let { filteredItems ->
                Triple(
                    filteredItems.sumOf { item -> item.value },
                    filteredItems.size,
                    filteredItems.any { item -> item.isFragile == true }
                )
            }

        it.copy(
            value = updatedValue,
            itemCount = updatedItemCount,
            isFragile = updatedIsFragile,
        )
    }
    val collections = LocalDataSource().loadCollections().map { collection ->
        val (totalValue, anyIsFragile, counts) = boxes
            .filter { box -> box.collectionId == collection.id }
            .let { filteredBoxes ->
                Triple(
                    filteredBoxes.sumOf { box -> box.value },
                    filteredBoxes.any { box -> box.isFragile },
                    Pair<Int, Int>(
                        filteredBoxes.sumOf{box -> box.itemCount},
                        filteredBoxes.size,
                    )
                )
            }

        collection.copy(
            value = totalValue,
            boxCount = counts.second,
            itemCount = counts.first,
            isFragile = anyIsFragile,
        )
    }
    val boxIdAndNames = boxes.map { BoxIdAndName(it.id, it.name) }
    val collectionIdAndNames = collections.map { BoxIdAndName(it.id, it.name) }
}

class LocalDataSource() {
    fun loadItems(): List<Item> {
        return listOf(
            Item(
                id = "4adfb752-c6b3-4002-901c-c2fa7889473b",
                name = "Peggy PUg",
                description = "Cutest little pug in the world",
                isFragile = true,
                value = 100.00, // 100000000000000.00,
                imageUri = null, // ImageUri.ResourceUri(R.drawable.pug),
                boxId = "e99a99f8-748d-427a-a305-14bda19d71a0",
                lastModified = Date(1638912000000L), // Date represented as a String "2021-12-08T00:00:00Z"
            ),
            Item(
                id = "9bbbaa9f-d11c-4410-a9aa-906221f0ec8b",
                name = "pot",
                description = "not the weed kind",
                isFragile = true,
                value = 5.00,
                boxId = "e99a99f8-748d-427a-a305-14bda19d71a0",
                lastModified = Date(1638922800000L),
            ),
            Item(
                id = "dd1ff0be-f4a9-4ed0-b393-e275809754e1",
                name = "pan",
                description = "not the weed kind",
                isFragile = true,
                value = 10.00,
                boxId = "e99a99f8-748d-427a-a305-14bda19d71a0",
                lastModified = Date(1641004800000L),
            ),
            Item(
                id = "ebaba247-330d-47be-8cd1-ec40db5c6ae7",
                name = "fork",
                description = "because i like plastic forks",
                value = 0.85,
                boxId = "5a1b8a9d-2e57-4f7c-bfd7-988bb9653f1d",
                lastModified = Date(1641091200000L),
            ),
            Item(
                id = "ea48ecfa-4990-4e1c-b257-c021b7f9adef",
                name = "shoes",
                description = "from walmart but comfy",
                value = 6.50,
                boxId = "5a1b8a9d-2e57-4f7c-bfd7-988bb9653f1d",
                lastModified = Date(1641177600000L),
            ),
            Item(
                id = "d6f9727c-93ef-46c0-a29b-2d6c0b3ea767",
                name = "shirt",
                value = 7.50,
                boxId = "5a1b8a9d-2e57-4f7c-bfd7-988bb9653f1d",
                lastModified = Date(1641264000000L),
            ),
            Item(
                id = "cfb5f212-daa2-4832-99d1-da9af7468dc1",
                name = "ties",
                value = 0.85,
                boxId = "c4b6d417-e077-4a20-8d6e-f21f72885cdd",
                lastModified = Date(1641350400000L),
            ),
            Item(
                id = "7c28497f-8a1e-4130-b6bb-facdc1cdbac0",
                name = "soap",
                value = 25.00,
                boxId = "c4b6d417-e077-4a20-8d6e-f21f72885cdd",
                lastModified = Date(1641436800000L),
            ),
            Item(
                id = "1027e225-e0a5-4c30-80c4-f26ae99f32ac",
                name = "shampoo",
                value = 7.50,
                boxId = "c4b6d417-e077-4a20-8d6e-f21f72885cdd",
                lastModified = Date(1641523200000L),
            ),
            Item(
                id = "38a61857-9dfc-490f-b23a-389e4c14a6b3",
                name = "toilette paper",
                value = 45.50,
                boxId = "c4b6d417-e077-4a20-8d6e-f21f72885cdd",
                lastModified = Date(1641609600000L),
            ),
            Item(
                id = " 1ece56ff-b044-4485-a391-8b2ea3e25b11",
                name = "tools",
                description = "not for breaking into homes",
                isFragile = true,
                value = 100.00,
                boxId = "2d6c2c8c-56fe-4f7e-9483-e6f9511f9fc5",
                lastModified = Date(1641696000000L),
            ),
            Item(
                id = "95265bc7-9126-4a1d-b25a-b04de1c6cb6b",
                name = "10L",
                description = "not for breaking into homes",
                isFragile = true,
                value = 125.00,
                boxId = "2d6c2c8c-56fe-4f7e-9483-e6f9511f9fc5",
                lastModified = Date(1641782400000L),
            ),
            Item(
                id = "abb939b2-5b5c-4e56-a3bc-840e46c7151c",
                name = "11L",
                description = "not for breaking into homes",
                isFragile = true,
                value = 3.00,
                boxId = "2d6c2c8c-56fe-4f7e-9483-e6f9511f9fc5",
                lastModified = Date(1641868800000L),
            ),
            Item(
                id = "3d06b39e-886c-4776-8d13-3febf859d46f",
                name = "12L",
                description = "not for breaking into homes",
                isFragile = true,
                value = 100.00,
                boxId = "1cb842ee-9636-4394-a49f-f56c922ef0d1",
                lastModified = Date(1641955200000L),
            ),
            Item(
                id = "228ba249-b5f2-408e-a41c-645a99927a28",
                name = "13L",
                description = "not for breaking into homes",
                isFragile = true,
                value = 125.00,
                boxId = "1cb842ee-9636-4394-a49f-f56c922ef0d1",
                lastModified = Date(1642041600000L),
            ),
            Item(
                id = "1c86b55f-5879-4552-8266-8e5e98952ecd",
                name = "14L",
                description = "not for breaking into homes",
                isFragile = true,
                value = 3.00,
                boxId = "37d40996-393b-4a3c-bed8-e67382d56d19",
                lastModified = Date(1642128000000L),
            ),
            Item(
                id = "3f8b7b8d-b7d1-4b7b-b8e0-b3d7a8a8e9d8",
                name = "15L",
                description = "not for breaking into homes",
                isFragile = true,
                value = 100.00,
                boxId = "37d40996-393b-4a3c-bed8-e67382d56d19",
                lastModified = Date(1642214400000L),
            ),
            Item(
                id = "8c1b0d0f-9d5c-4b7d-8b1c-0f9d5c4b7d8b",
                name = "16L",
                description = "not for breaking into homes",
                isFragile = true,
                value = 125.00,
                boxId = "37d40996-393b-4a3c-bed8-e67382d56d19",
                lastModified = Date(1642300800000L),
            ),
            Item(
                id = "9b1c0d0f-9d5c-4b7d-8b1c-0f9d5c4b7d8b",
                name = "17L",
                description = "not for breaking into homes",
                isFragile = true,
                value = 3.00,
                boxId = "37d40996-393b-4a3c-bed8-e67382d56d19",
                lastModified = Date(1642387200000L),
            ),
        )
    }

    fun loadBoxes(): List<Box> {
        return listOf(
            Box(
                id = "e99a99f8-748d-427a-a305-14bda19d71a0",
                name = "kitchen",
                description = "don't open until i get home",
                collectionId = "748c0084-4f2b-4957-ae2f-4892670c85f3",
                lastModified = Date(1641187200000L),
            ),
            Box(
                id = "5a1b8a9d-2e57-4f7c-bfd7-988bb9653f1d",
                name = "bedroom1",
                collectionId = "748c0084-4f2b-4957-ae2f-4892670c85f3",
                lastModified = Date(1676486400000L),
            ),
            Box(
                id = "c4b6d417-e077-4a20-8d6e-f21f72885cdd",
                name = "bedroom2",
                collectionId = "cc92160b-b6f1-41f2-bac5-8cd5f868e645",
                lastModified = Date(1711785600000L),
            ),
            Box(
                id = "2d6c2c8c-56fe-4f7e-9483-e6f9511f9fc5",
                name = "garage",
                collectionId = "cc92160b-b6f1-41f2-bac5-8cd5f868e645",
                lastModified = Date(1747084800000L),
            ),
            Box(
                id = "1cb842ee-9636-4394-a49f-f56c922ef0d1",
                name = "garage",
                collectionId = "cc92160b-b6f1-41f2-bac5-8cd5f868e645",
                lastModified = Date(1782384000000L),
            ),
            Box(
                id = "37d40996-393b-4a3c-bed8-e67382d56d19",
                name = "garage",
                collectionId = "0d394337-0886-48b2-b788-5a706cda52e6",
                lastModified = Date(1817683200000L),
            ),
        )
    }

    fun loadCollections(): List<Collection> {
        return listOf(
            Collection(
                id = "748c0084-4f2b-4957-ae2f-4892670c85f3",
                name = "for home",
                description = "junk and stuff",
                lastModified = Date(1852982400000L),
            ),
            Collection(
                id = "cc92160b-b6f1-41f2-bac5-8cd5f868e645",
                name = "for donation",
                lastModified = Date(1888281600000L),
            ),
            Collection(
                id = "0d394337-0886-48b2-b788-5a706cda52e6",
                name = "not sure yet",
                description = "more junk",
                lastModified = Date(1923580800000L),
            ),
            Collection(
                id = "ffd69d63-8bb1-4f15-856e-316777c36f3e",
                name = "3L",
                lastModified = Date(1958880000000L),
            ),
            Collection(
                id = "bc71bf4b-4771-4508-917d-b615211bc786",
                name = "4L not sure yet",
                lastModified = Date(1994179200000L),
            ),
        )
    }
}
