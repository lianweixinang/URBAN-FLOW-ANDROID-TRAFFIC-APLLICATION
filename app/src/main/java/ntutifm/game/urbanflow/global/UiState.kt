package ntutifm.game.urbanflow.global
interface UiState

data class UiElementState(
    var isOpen: Boolean = false,
    var sitMode: Boolean = false,
    var favoriteFlag: Boolean = false,
    var expandDrawer: Boolean = false,
    var favoriteMark: Boolean = false,
)

data class InitializationState(
    var isTTSInitialized: Boolean = false,
    var permissionDenied: Boolean = false,
)