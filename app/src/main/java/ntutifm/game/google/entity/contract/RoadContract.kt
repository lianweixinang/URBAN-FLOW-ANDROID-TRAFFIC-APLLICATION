package ntutifm.game.google.entity.contract

import ntutifm.game.google.apiClass.CCTV
import ntutifm.game.google.apiClass.Parking
import ntutifm.game.google.apiClass.RoadFavorite
import ntutifm.game.google.global.UiEffect
import ntutifm.game.google.global.UiEvent
import ntutifm.game.google.global.UiState

class RoadContract {

    sealed class Event : UiEvent {
        object OnFetchRoads : Event()
        data class OnDeleteItem(val dataName : String) : Event()
    }

    data class State(
        val postsState: RoadState
    ) : UiState

    sealed class RoadState {
        object Idle : RoadState()
        object Loading : RoadState()
        data class Success(val posts : List<RoadFavorite>) : RoadState()
    }

    sealed class Effect : UiEffect {
        data class ShowError(val message : String?) : Effect()

    }

}