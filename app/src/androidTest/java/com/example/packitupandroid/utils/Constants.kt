package com.example.packitupandroid.utils


const val initialValue = 0
const val incrementCountByFive = 5
const val decrementCountByFour = 4
const val DELAY = 2000L
const val COUNT = 5

const val USE_MOCK_DATA = false

/**
 * The name of the database file to be used by the application.
 * If `USE_MOCK_DATA` is `true`, the database name will be "app.db".
 * If `USE_MOCK_DATA` is `false`, the database name will be "test.db".
 */
val DATABASE_NAME = if (USE_MOCK_DATA) "app.db" else "test.db"


//TODO: // TESTS ARE USING DB THAT'S WHY THERE'S USNG MOCK DATA AND NOT MOCKREPO DATA