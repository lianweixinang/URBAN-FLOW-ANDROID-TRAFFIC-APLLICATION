package ntutifm.game.google.ui.parking

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import ntutifm.game.google.dataBase.ParkingRepository
import ntutifm.game.google.entity.contract.ParkingContract
import ntutifm.game.google.global.BaseViewModel
import ntutifm.game.google.global.Resource

class GasViewModel(application: Application) : BaseViewModel<ParkingContract.Event, ParkingContract.State, ParkingContract.Effect>(){

    private val repository: ParkingRepository by lazy{
        ParkingRepository(application)
    }
    override fun createInitialState(): ParkingContract.State {
        return ParkingContract.State(
            postsState = ParkingContract.ParkingState.Idle,
        )
    }

    override fun handleEvent(event: ParkingContract.Event) {
        when (event) {
            is ParkingContract.Event.OnFetchParkings -> {
                fetchPosts()
            }
        }
    }

    /**
     * Fetch posts
     */
    private fun fetchPosts() {
        viewModelScope.launch {
            repository.getAllStation().flowOn(Dispatchers.IO)
                .onStart { emit(Resource.Loading) }
                .collect {
                    when (it) {
                        is Resource.Loading -> {
                            // Set State
                            setState { copy(postsState = ParkingContract.ParkingState.Loading) }
                        }
                        is Resource.Empty -> {
                            // Set State
                            setState { copy(postsState = ParkingContract.ParkingState.Idle) }
                        }
                        is Resource.Success -> {
                            // Set State
                            setState { copy(postsState = ParkingContract.ParkingState.Success(posts = it.data)) }
                        }
                        is Resource.Error -> {
                            // Set Effect
                            setEffect { ParkingContract.Effect.ShowError(message = it.exception.message) }
                        }
                    }
                }
        }
    }

    class ParkingViewModelFactory(private val application: Application): ViewModelProvider.AndroidViewModelFactory(application) {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return GasViewModel(
                application
            ) as T
        }
    }

}