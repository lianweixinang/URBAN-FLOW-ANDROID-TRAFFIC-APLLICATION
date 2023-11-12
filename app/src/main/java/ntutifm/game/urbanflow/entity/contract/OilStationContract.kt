package ntutifm.game.urbanflow.entity.contract

import ntutifm.game.urbanflow.apiClass.OilStation
import ntutifm.game.urbanflow.global.UiEffect
import ntutifm.game.urbanflow.global.UiEvent
import ntutifm.game.urbanflow.global.UiState

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