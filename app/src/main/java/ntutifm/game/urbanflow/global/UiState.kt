package ntutifm.game.urbanflow.global
interface UiState

data class UiEelementState(
    var isOpen: Boolean = false,
    var sitMode: Boolean = false,
    var favoriteFlag: Boolean = false,
    var markLike: Boolean = false,
    var noChange: Boolean = false,
)

data class InitializationState(
    var isTTSInitialized: Boolean = false,
    var isUiInitialized: Boolean = false,
    var permissionDenied: Boolean = false,
)