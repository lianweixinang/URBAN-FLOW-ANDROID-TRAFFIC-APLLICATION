package ntutifm.game.google.ui.oilStation

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import ntutifm.game.google.dataBase.OilStationRepository
import ntutifm.game.google.entity.contract.OilStationContract
import ntutifm.game.google.global.BaseViewModel
import ntutifm.game.google.global.MyLog
import ntutifm.game.google.global.Resource

class OilStationViewModel(application: Application) : BaseViewModel<OilStationContract.Event, OilStationContract.State, OilStationContract.Effect>(){

    private val repository: OilStationRepository by lazy{
        OilStationRepository(application)
    }
    override fun createInitialState(): OilStationContract.State {
        return OilStationContract.State(
            postsState = OilStationContract.OilStationState.Idle,
        )
    }

    override fun handleEvent(event: OilStationContract.Event) {
        when (event) {
            is OilStationContract.Event.OnFetchOilStations -> {
                fetchPosts()
            }
            is OilStationContract.Event.OnDeleteItem -> {
                deletePost(event.dataName)
            }
        }
    }

    /**
     * Fetch posts
     */
    private fun fetchPosts() {
        viewModelScope.launch {
            repository.getAllOilStation().flowOn(Dispatchers.IO)
                .onStart { emit(Resource.Loading) }
                .collect {
                    when (it) {
                        is Resource.Loading -> {
                            // Set State
                            setState { copy(postsState = OilStationContract.OilStationState.Loading) }
                        }
                        is Resource.Empty -> {
                            // Set State
                            setState { copy(postsState = OilStationContract.OilStationState.Idle) }
                        }
                        is Resource.Success -> {
                            MyLog.e("資料流過")
                            // Set State
                            setState { copy(postsState = OilStationContract.OilStationState.Success(posts = it.data)) }
                        }
                        is Resource.Error -> {
                            // Set Effect
                            setEffect { OilStationContract.Effect.ShowError(message = it.exception.message) }
                        }
                    }
                }
        }
        MyLog.e("資料結束")
    }
    private fun deletePost(data:String){
        viewModelScope.launch {
            repository.deleteFavorite(data)
            MyLog.e("資料被刪")
        }
    }

    class OilStationViewModelFactory(private val application: Application): ViewModelProvider.AndroidViewModelFactory(application) {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return OilStationViewModel(
                application
            ) as T
        }
    }

}