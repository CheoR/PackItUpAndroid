package com.example.packitupandroid.ui

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.packitupandroid.PackItUpApplication
import com.example.packitupandroid.ui.screens.ScreenViewModel
import com.example.packitupandroid.ui.screens.box.BoxesScreenViewModel
import com.example.packitupandroid.ui.screens.collection.CollectionsScreenViewModel
import com.example.packitupandroid.ui.screens.item.ItemsScreenViewModel
import com.example.packitupandroid.ui.screens.summary.SummaryScreenViewModel

/**
 * Provides Factory to create ViewModel instance for entire PackItUp app
 */
object PackItUpViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            ItemsScreenViewModel(
                itemsRepository = packItUpApplication().container.itemsRepository,
            )
        }

        initializer {
            BoxesScreenViewModel(
                boxesRepository = packItUpApplication().container.boxesRepository,
            )
        }

        initializer {
            CollectionsScreenViewModel(
                collectionsRepository = packItUpApplication().container.collectionsRepository,
            )
        }

        initializer {
            SummaryScreenViewModel(
                summaryRepository = packItUpApplication().container.summaryRepository,
            )
        }

        initializer {
            ScreenViewModel(
                summaryRepository = packItUpApplication().container.summaryRepository,
            )
        }
    }
}

/**
 * Extension function queries for [PackItUpApplication] object and returns [PackItUpApplication] instance.
 */
fun CreationExtras.packItUpApplication(): PackItUpApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as PackItUpApplication)
