package com.example.packitupandroid.ui

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.packitupandroid.PackItUpApplication


/**
 * Provides Factory to create ViewModel instance for entire PackItUp app
 */
object PackItUpViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            PackItUpViewModel(
                localDataRepository = packItUpApplication().container.localDataRepository,
                itemsRepository =  packItUpApplication().container.itemsRepository,
            )
        }
    }
}

/**
 * Extension function queries for [PackItUpApplication] object and returns [PackItUpApplication] instance.
 */
fun CreationExtras.packItUpApplication(): PackItUpApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as PackItUpApplication)
