package com.example.packitupandroid

import android.app.Application
import com.example.packitupandroid.data.AppContainer
import com.example.packitupandroid.data.DefaultAppContainer

class PackItUpApplication : Application() {
    // to store the DefaultAppContainer object
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(this)
    }
}
