package ntutifm.game.google.entity.contract

import ntutifm.game.google.apiClass.OilStation
import ntutifm.game.google.global.UiEffect
import ntutifm.game.google.global.UiEvent
import ntutifm.game.google.global.UiState

class OilStationContract {

    sealed class Event : UiEvent {
        object OnFetchOilStations : Event()
        data class OnDeleteItem(val dataName : String) : Event()
    }

    data class State(
        val postsState: OilStationState
    ) : UiState

    sealed class OilStationState {
        object Idle : OilStationState()
        object Loading : OilStationState()
        data class Success(val posts : List<OilStation>) : OilStationState()
    }

    sealed class Effect : UiEffect {
        data class ShowError(val message : String?) : Effect()

    }

}