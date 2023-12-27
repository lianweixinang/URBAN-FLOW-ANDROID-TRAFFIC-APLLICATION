package ntutifm.game.urbanflow.entity.contract

import ntutifm.game.urbanflow.apiClass.OilStation
import ntutifm.game.urbanflow.global.UiEffect
import ntutifm.game.urbanflow.global.UiEvent
import ntutifm.game.urbanflow.global.UiState

class MessageContract {

    sealed class Event : UiEvent {
        object OnFetchMessage : Event()
        data class OnDeleteItem(val dataName : String) : Event()
    }

    data class State(
        val postsState: MessageState
    ) : UiState

    sealed class MessageState {
        object Idle : MessageState()
        object Loading : MessageState()
        data class Success(val posts : List<OilStation>) : MessageState()
    }

    sealed class Effect : UiEffect {
        data class ShowError(val message : String?) : Effect()

    }

}