package ntutifm.game.google.ui.road

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import ntutifm.game.google.dataBase.RoadFavoriteRepository
import ntutifm.game.google.entity.contract.RoadContract
import ntutifm.game.google.global.BaseViewModel
import ntutifm.game.google.global.Resource

class RoadViewModel(application: Application) : BaseViewModel<RoadContract.Event, RoadContract.State, RoadContract.Effect>(){

    private val repository: RoadFavoriteRepository by lazy{
        RoadFavoriteRepository(application)
    }
    override fun createInitialState(): RoadContract.State {
        return RoadContract.State(
            postsState = RoadContract.RoadState.Idle,
        )
    }

    override fun handleEvent(event: RoadContract.Event) {
        when (event) {
            is RoadContract.Event.OnFetchRoads -> {
                fetchPosts()
            }
        }
    }

    /**
     * Fetch posts
     */
    private fun fetchPosts() {
        viewModelScope.launch {
            repository.getAllRoad().flowOn(Dispatchers.IO)
                .onStart { emit(Resource.Loading) }
                .collect {
                    when (it) {
                        is Resource.Loading -> {
                            // Set State
                            setState { copy(postsState = RoadContract.RoadState.Loading) }
                        }
                        is Resource.Empty -> {
                            // Set State
                            setState { copy(postsState = RoadContract.RoadState.Idle) }
                        }
                        is Resource.Success -> {
                            // Set State
                            setState { copy(postsState = RoadContract.RoadState.Success(posts = it.data)) }
                        }
                        is Resource.Error -> {
                            // Set Effect
                            setEffect { RoadContract.Effect.ShowError(message = it.exception.message) }
                        }
                    }
                }
        }
    }

    class RoadViewModelFactory(private val application: Application): ViewModelProvider.AndroidViewModelFactory(application) {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return RoadViewModel(
                application
            ) as T
        }
    }

}