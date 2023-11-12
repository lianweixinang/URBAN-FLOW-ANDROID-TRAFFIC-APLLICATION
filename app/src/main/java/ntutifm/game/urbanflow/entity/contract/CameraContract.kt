package ntutifm.game.urbanflow.entity.contract

import ntutifm.game.urbanflow.apiClass.Camera
import ntutifm.game.urbanflow.global.UiEffect
import ntutifm.game.urbanflow.global.UiEvent
import ntutifm.game.urbanflow.global.UiState

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