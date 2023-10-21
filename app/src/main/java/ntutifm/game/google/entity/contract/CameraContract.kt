package ntutifm.game.google.entity.contract

import ntutifm.game.google.apiClass.Camera
import ntutifm.game.google.global.UiEffect
import ntutifm.game.google.global.UiEvent
import ntutifm.game.google.global.UiState

class CameraContract {

    sealed class Event : UiEvent {
        object OnFetchCameras : Event()
        data class OnDeleteItem(val dataName : String):Event()
    }

    data class State(
        val postsState: CameraState
    ) : UiState

    sealed class CameraState {
        object Idle : CameraState()
        object Loading : CameraState()
        data class Success(val posts : List<Camera>) : CameraState()
    }

    sealed class Effect : UiEffect {
        data class ShowError(val message : String?) : Effect()

    }

}