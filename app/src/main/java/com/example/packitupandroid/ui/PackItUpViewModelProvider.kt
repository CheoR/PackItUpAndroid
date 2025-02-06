package com.example.packitupandroid.ui

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.packitupandroid.PackItUpApplication
import com.example.packitupandroid.ui.navigation.NavHostViewModel
import com.example.packitupandroid.ui.screens.box.BoxesScreenViewModel
import com.example.packitupandroid.ui.screens.collection.CollectionsScreenViewModel
import com.example.packitupandroid.ui.screens.item.ItemsScreenViewModel
import com.example.packitupandroid.ui.screens.summary.SummaryScreenViewModel


/**
 * Provides a factory to create ViewModel instances for the entire PackItUp app.
 *
 * This object defines a [viewModelFactory] that creates instances of various ViewModels,
 * injecting their required dependencies.
 */
object MoveHaulViewModelProvider {
    /**
     * The [ViewModelProvider.Factory] used to create ViewModel instances.
     *
     * This factory uses initializers to create instances of [ItemsScreenViewModel],
     * [BoxesScreenViewModel], [CollectionsScreenViewModel], and [SummaryScreenViewModel],
     * injecting their respective repositories and [SavedStateHandle] where needed.
     */
    val Factory = viewModelFactory {
        initializer {
            ItemsScreenViewModel(
                savedStateHandle = createSavedStateHandle(),
                repository = packItUpApplication().container.itemsRepository,
            )
        }
        initializer {
            BoxesScreenViewModel(
                savedStateHandle = createSavedStateHandle(),
                repository = packItUpApplication().container.boxesRepository,
            )
        }
        initializer {
            CollectionsScreenViewModel(
                savedStateHandle = createSavedStateHandle(),
                repository = packItUpApplication().container.collectionsRepository,
            )
        }
        initializer {
            SummaryScreenViewModel(
                repository = packItUpApplication().container.summaryRepository,
            )
        }
        initializer {
            NavHostViewModel(
                summaryRepository = packItUpApplication().container.summaryRepository,
            )
        }
    }
}


/**
 * Extension function to retrieve the [PackItUpApplication] instance from [CreationExtras].
 *
 * This function is used to access the application's dependency container, which holds
 * the repositories needed by the ViewModels.
 *
 * @return The [PackItUpApplication] instance.
 */
fun CreationExtras.packItUpApplication(): PackItUpApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as PackItUpApplication)
