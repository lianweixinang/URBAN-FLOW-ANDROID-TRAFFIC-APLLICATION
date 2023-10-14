package ntutifm.game.google.entity.contract

import ntutifm.game.google.apiClass.Camera
import ntutifm.game.google.global.UiEffect
import ntutifm.game.google.global.UiEvent
import ntutifm.game.google.global.UiState

class CameraContract {

    sealed class Event : UiEvent {
        object OnFetchCameras : Event()
    }

    data class State(
        val postsState: CamerasState
    ) : UiState

    sealed class CamerasState {
        object Idle : CamerasState()
        object Loading : CamerasState()
        data class Success(val posts : List<Camera>) : CamerasState()
    }

    sealed class Effect : UiEffect {
        data class ShowError(val message : String?) : Effect()

    }

}