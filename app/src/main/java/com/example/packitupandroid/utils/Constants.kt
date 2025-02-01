package com.example.packitupandroid.utils


// TODO: define variables in defaulfConfig
// TODO: check how to set USE_MOCK_DATA in for different flavors
// TODO: Create mockdb

/**
 * Flag to determine whether to use mock data.
 * If set to `true`, the application will use mock data for development and testing purposes.
 * If set to `false`, the application will use actual data.
 */
const val USE_MOCK_DATA = true

/**
 * The name of the database file to be used by the application.
 * If `USE_MOCK_DATA` is `true`, the database name will be "app.db".
 * If `USE_MOCK_DATA` is `false`, the database name will be "app.db".
 */
val DATABASE_NAME = if (USE_MOCK_DATA) "app.db" else "app.db"
