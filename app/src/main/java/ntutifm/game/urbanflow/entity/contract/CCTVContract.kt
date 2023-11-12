package ntutifm.game.urbanflow.entity.contract

import ntutifm.game.urbanflow.apiClass.CCTV
import ntutifm.game.urbanflow.global.UiEffect
import ntutifm.game.urbanflow.global.UiEvent
import ntutifm.game.urbanflow.global.UiState

class CCTVContract {

    sealed class Event : UiEvent {
        object OnFetchCCTVs : Event()
        data class OnDeleteItem(val dataName : String) :Event()
    }

    data class State(
        val postsState: CCTVState
    ) : UiState

    sealed class CCTVState {
        object Idle : CCTVState()
        object Loading : CCTVState()
        data class Success(val posts : List<CCTV>) : CCTVState()
    }

    sealed class Effect : UiEffect {
        data class ShowError(val message : String?) : Effect()

    }

}