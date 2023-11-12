package ntutifm.game.urbanflow.entity.sync

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

object SyncBottomBar {

    sealed class State {
        object Open: State()
        object Close: State()
    }

    private val mutableState = Channel<State>()
    private val stateFlow = mutableState.receiveAsFlow()
    val state: LiveData<State> = stateFlow.asLiveData()

    suspend fun updateState(state: State) {
        mutableState.send(state)
    }
}