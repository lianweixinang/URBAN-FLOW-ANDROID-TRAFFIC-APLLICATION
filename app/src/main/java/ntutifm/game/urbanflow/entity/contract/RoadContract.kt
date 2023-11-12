package ntutifm.game.urbanflow.entity.contract

import ntutifm.game.urbanflow.apiClass.RoadFavorite
import ntutifm.game.urbanflow.global.UiEffect
import ntutifm.game.urbanflow.global.UiEvent
import ntutifm.game.urbanflow.global.UiState

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