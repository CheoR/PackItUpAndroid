package com.example.packitupandroid

import android.app.Application
import com.example.packitupandroid.data.AppContainer
import com.example.packitupandroid.data.DefaultAppContainer
import com.example.packitupandroid.utils.USE_MOCK_DATA


/**
 * Application class for the PackItUp app.
 *
 * This class initializes the dependency injection container for the app.
 */
class AppApplication : Application() {

    /**
     * The AppContainer instance that holds the application's dependencies.
     */
    lateinit var container: AppContainer

    /**
     * Called when the application is starting, before any other application objects have been created.
     * Initializes the DefaultAppContainer with the application context.
     */
    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(this, useMockData = USE_MOCK_DATA)
    }
}
