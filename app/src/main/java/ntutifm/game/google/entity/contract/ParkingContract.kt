package ntutifm.game.google.entity.contract

import ntutifm.game.google.apiClass.CCTV
import ntutifm.game.google.apiClass.Parking
import ntutifm.game.google.global.UiEffect
import ntutifm.game.google.global.UiEvent
import ntutifm.game.google.global.UiState

class ParkingContract {

    sealed class Event : UiEvent {
        object OnFetchParkings : Event()
        data class OnDeleteItem(val dataName : String) : ParkingContract.Event()
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