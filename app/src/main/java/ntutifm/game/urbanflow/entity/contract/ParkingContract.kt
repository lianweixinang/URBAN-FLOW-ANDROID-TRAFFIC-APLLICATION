package ntutifm.game.urbanflow.entity.contract

import ntutifm.game.urbanflow.apiClass.Parking
import ntutifm.game.urbanflow.global.UiEffect
import ntutifm.game.urbanflow.global.UiEvent
import ntutifm.game.urbanflow.global.UiState

class ParkingContract {

    sealed class Event : UiEvent {
        object OnFetchParkings : Event()
        data class OnDeleteItem(val dataName : String) :Event()
    }

    data class State(
        val postsState: ParkingState
    ) : UiState

    sealed class ParkingState {
        object Idle : ParkingState()
        object Loading : ParkingState()
        data class Success(val posts : List<Parking>) : ParkingState()
    }

    sealed class Effect : UiEffect {
        data class ShowError(val message : String?) : Effect()

    }

}