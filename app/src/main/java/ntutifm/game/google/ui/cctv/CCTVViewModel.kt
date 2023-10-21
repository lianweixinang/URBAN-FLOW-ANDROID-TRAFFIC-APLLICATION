package ntutifm.game.google.ui.cctv

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import ntutifm.game.google.dataBase.CCTVRepository
import ntutifm.game.google.entity.contract.CCTVContract
import ntutifm.game.google.entity.contract.ParkingContract
import ntutifm.game.google.global.BaseViewModel
import ntutifm.game.google.global.MyLog
import ntutifm.game.google.global.Resource

class CCTVViewModel(application: Application) : BaseViewModel<CCTVContract.Event, CCTVContract.State, CCTVContract.Effect>(){

    private val repository: CCTVRepository by lazy{
        CCTVRepository(application)
    }
    override fun createInitialState(): CCTVContract.State {
        return CCTVContract.State(
            postsState = CCTVContract.CCTVState.Idle,
        )
    }

    override fun handleEvent(event: CCTVContract.Event) {
        when (event) {
            is CCTVContract.Event.OnFetchCCTVs -> {
                fetchPosts()
            }
            is CCTVContract.Event.OnDeleteItem -> {
                deletePost(event.dataName)
            }
        }
    }
    private fun deletePost(data:String){
        viewModelScope.launch {
            repository.deleteFavorite(data)
            MyLog.e("資料被刪")
            fetchPosts()
        }
    }

    /**
     * Fetch posts
     */
    private fun fetchPosts() {
        viewModelScope.launch {
            repository.getAllCCTV().flowOn(Dispatchers.IO)
                .onStart { emit(Resource.Loading) }
                .collect {
                    when (it) {
                        is Resource.Loading -> {
                            // Set State
                            setState { copy(postsState = CCTVContract.CCTVState.Loading) }
                        }
                        is Resource.Empty -> {
                            // Set State
                            setState { copy(postsState = CCTVContract.CCTVState.Idle) }
                        }
                        is Resource.Success -> {
                            // Set State
                            setState { copy(postsState = CCTVContract.CCTVState.Success(posts = it.data)) }
                        }
                        is Resource.Error -> {
                            // Set Effect
                            setEffect { CCTVContract.Effect.ShowError(message = it.exception.message) }
                        }
                    }
                }
        }
    }

    class CCTVViewModelFactory(private val application: Application): ViewModelProvider.AndroidViewModelFactory(application) {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return CCTVViewModel(
                application
            ) as T
        }
    }

}