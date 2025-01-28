package com.example.packitupandroid.ui

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.packitupandroid.PackItUpApplication
import com.example.packitupandroid.data.model.BaseCardData
import com.example.packitupandroid.ui.components.strategyCard.EditCardViewModel
//import com.example.packitupandroid.ui.screens.Screen2ViewModel
import com.example.packitupandroid.ui.screens.ScreenViewModel
import com.example.packitupandroid.ui.screens.box.BoxesScreenViewModel
import com.example.packitupandroid.ui.screens.collection.CollectionsScreenViewModel
import com.example.packitupandroid.ui.screens.item.ItemsScreenViewModel
import com.example.packitupandroid.ui.screens.summary.SummaryScreenViewModel
import com.example.packitupandroid.utils.CardType

/**
 * Provides Factory to create ViewModel instance for entire PackItUp app
 */
object PackItUpViewModelProvider {
    val Factory = viewModelFactory {
//        initializer {
//            ItemsScreenViewModel(
//                savedStateHandle = createSavedStateHandle(),
//                itemsRepository = packItUpApplication().container.itemsRepository,
//            )
//        }

//        initializer {
//            BoxesScreenViewModel(
//                savedStateHandle = createSavedStateHandle(),
//                boxesRepository = packItUpApplication().container.boxesRepository,
//            )
//        }

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
                boxesRepository = packItUpApplication().container.boxesRepository,
                collectionsRepository = packItUpApplication().container.collectionsRepository,
                summaryRepository = packItUpApplication().container.summaryRepository,
            )
        }
    }

//    fun editCardViewModelFactory(cardType: CardType, id: String?): ViewModelProvider.Factory {
//        return viewModelFactory {
//            initializer {
//                EditCardViewModel(
//                    savedStateHandle = createSavedStateHandle(),
//                    itemsRepository = packItUpApplication().container.itemsRepository,
//                    boxesRepository = packItUpApplication().container.boxesRepository,
//                    collectionsRepository = packItUpApplication().container.collectionsRepository,
//                    cardType = cardType,
//                    id = id
//                )
//            }
//        }
//    }
//
//    fun <T: BaseCardData>screen2ViewModelFactory(): ViewModelProvider.Factory {
//        return viewModelFactory {
//            initializer {
//                Screen2ViewModel<T>(
//                    savedStateHandle = createSavedStateHandle(),
//                    itemsRepository = packItUpApplication().container.itemsRepository,
//                    boxesRepository = packItUpApplication().container.boxesRepository,
//                    collectionsRepository = packItUpApplication().container.collectionsRepository,
//                )
//            }
//        }
//    }
}

/**
 * Extension function queries for [PackItUpApplication] object and returns [PackItUpApplication] instance.
 */
fun CreationExtras.packItUpApplication(): PackItUpApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as PackItUpApplication)
