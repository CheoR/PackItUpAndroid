package com.example.packitupandroid.fake.data

import com.example.packitupandroid.data.model.Box
import com.example.packitupandroid.data.model.Collection
import com.example.packitupandroid.data.model.Item


val collections = listOf(
    Collection(id = "1", name = "Collection 1"),
    Collection(id = "2", name = "Collection 2"),
    Collection(id = "3", name = "Collection 3"),
    Collection(id = "4", name = "Collection 4"),
)
val boxes = listOf(
    Box(id = "1", name = "Box 1", collectionId = collections.first().id),
    Box(id = "2", name = "Box 2", collectionId = collections.first().id),
    Box(id = "3", name = "Box 3", collectionId = collections.last().id),
    Box(id = "4", name = "Box 4"),
)
val items = listOf(
    Item(id = "1", name = "Item 1", boxId = boxes.first().id),
    Item(id = "2", name = "Item 2", boxId = boxes.first().id),
    Item(id = "3", name = "Item 3", boxId = boxes[2].id),
    Item(id = "4", name = "Item 4", boxId = boxes[2].id),
    Item(id = "5", name = "Item 5", boxId = boxes[2].id),
    Item(id = "6", name = "Item 6", boxId = boxes[3].id),
    Item(id = "7", name = "Item 7", boxId = boxes[3].id),
)