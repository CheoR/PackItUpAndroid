package com.example.packitupandroid.fake.data

import com.example.packitupandroid.data.model.Box
import com.example.packitupandroid.data.model.Collection
import com.example.packitupandroid.data.model.Item
import com.example.packitupandroid.data.repository.toEntity


val collections = listOf(
    Collection("1", "collections1").toEntity(),
    Collection("2", "collections2").toEntity(),
    Collection("3", "collections3").toEntity(),
    Collection("4", "collections4").toEntity(),
)

val boxes = listOf(
    Box("1", "Box1", "1", collectionId=collections[0].id).toEntity(),
    Box("2", "Box2", "2", collectionId=collections[0].id).toEntity(),
    Box("3", "Box3", "3", collectionId=collections[1].id).toEntity(),
    Box("4", "Box4", "4").toEntity(),
)

val items = listOf(
    Item("1", "item1", "1", boxId=boxes[0].id).toEntity(),
    Item("2", "Item2", "1", boxId=boxes[0].id).toEntity(),
    Item("3", "Item3", "2", boxId=boxes[0].id).toEntity(),
    Item("4", "Item4", "2", boxId=boxes[1].id).toEntity(),
    Item("5", "Item5", "3", boxId=boxes[1].id).toEntity(),
    Item("6", "Item6", "3", boxId=boxes[2].id).toEntity(),
    Item("7", "Item7", "4", boxId=boxes[2].id).toEntity(),
    Item("8", "Item8", "4", boxId=boxes[3].id).toEntity(),
)
