package com.example.packitupandroid.ui.screens

//
//class Screen2ViewModel<T: BaseCardData> (
//    val savedStateHandle: SavedStateHandle,
//    val itemsRepository: ItemsRepository,
//    val boxesRepository: BoxesRepository,
//    val collectionsRepository: CollectionsRepository,
//    private val cardType: CardType = CardType.Default,
//    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default,
//) : ViewModel() {
//    private val _uiState = MutableStateFlow(Screen2ViewModelUiState<T>())
//    val uiState: StateFlow<Screen2ViewModelUiState<T>> = _uiState.asStateFlow()
//
//    init {
//        initialize()
//    }
//
//    private fun initialize() {
//        viewModelScope.launch(defaultDispatcher) {
//            val result = when (cardType) {
//                CardType.Item -> getItemElements()
//                CardType.Box -> getBoxElements()
//                CardType.Collection -> getCollectionElements()
//                else -> throw IllegalArgumentException("CardType not supported")
//            }
//            _uiState.value = Screen2ViewModelUiState(result.first as List<T>, result.second)
//        }
//    }
//
//    // TODO: DROPDOWN OPTIONS NEEDS TO BE DROPDOWNOPTIONSTYPE WITH DROPDOWN  NAME AND ID
//
//    fun onCreate(count: Int) {}
//    suspend fun onDestory(element: Collection) {}
//     fun onUpdate() {}
//     fun onDestroy() {}
////    suspend fun filterElement(lst: List<Collection>) -> List<Collection>) = { it }
//
//    private suspend fun getItemElements(): Pair<List<Item>, List<String?>> {
//        val elements = itemsRepository.getAllItemsStream()
//            .map { list ->
//                list.map {
//                    it.toItem().copy(
//                        currentSelection = boxesRepository.getQueryBox(it.boxId ?: "")?.name,
//                        iconsContent = @Composable {
//                            IconBadge(
//                                image = ImageContent.VectorImage(Icons.AutoMirrored.Filled.Label),
//                                badgeCount = 0,
//                                badgeContentDescription = "Item Image Placeholder"
//                            )
//                        }
//                    )
//                }
//            }
//            .first()
//        val dropdownSelections = boxesRepository.getDropdownSelections()
//            .map { list -> list.map { it.toString() } }
//            .first()
//        return Pair(elements, dropdownSelections)
//    }
//
//    private suspend fun getBoxElements(): Pair<List<Box>, List<String?>> {
//        val elements = boxesRepository.getAllBoxesStream()
//            .map { list ->
//                list.map {
//                    it.toBox().copy(
//                        currentSelection = collectionsRepository.getQueryCollection(it.collection_id ?: "")?.name,
//                        iconsContent = @Composable {
//                            IconBadge(
//                                image = ImageContent.VectorImage(Icons.AutoMirrored.Filled.Label),
//                                badgeCount = it.item_count ?: 0,
//                                badgeContentDescription = "Number Of Items"
//                            )
//                        }
//                    )
//                }
//            }
//            .first()
//        val dropdownSelections = collectionsRepository.getDropdownSelections()
//            .map { list -> list.map { it.toString() } }
//            .first()
//        return Pair(elements, dropdownSelections)
//    }
//
//    private suspend fun getCollectionElements(): Pair<List<Collection>, List<String?>> {
//        val elements = collectionsRepository.getAllCollectionsStream()
//            .map { list ->
//                list.map {
//                    it.toCollection().copy(
//                        iconsContent = @Composable {
//                            IconBadge(
//                                image = ImageContent.DrawableImage(R.drawable.ic_launcher_foreground),
//                                badgeCount = it.box_count ?: 0,
//                                badgeContentDescription = "Number of boxes"
//                            )
//                            IconBadge(
//                                image = ImageContent.VectorImage(Icons.AutoMirrored.Filled.Label),
//                                badgeCount = it.item_count ?: 0,
//                                badgeContentDescription = "Number of items"
//                            )
//                        }
//                    )
//                }
//            }
//            .first()
//        return Pair(elements, emptyList())
//    }
//}
//
//// TODO: TURN THIS INTO RESULT TYPE WITH LOADING, ERROR, COMPLETE
//data class Screen2ViewModelUiState<D: BaseCardData>(
//    val elements : List<D> = emptyList(),
//    val dropdownSelections : List<String?> = emptyList()
//)
