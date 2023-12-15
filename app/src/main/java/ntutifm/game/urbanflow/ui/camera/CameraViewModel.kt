package ntutifm.game.urbanflow.ui.camera

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import ntutifm.game.urbanflow.dataBase.CameraRepository
import ntutifm.game.urbanflow.entity.contract.CameraContract
import ntutifm.game.urbanflow.global.BaseViewModel
import ntutifm.game.urbanflow.global.MyLog
import ntutifm.game.urbanflow.global.Resource

class CameraViewModel(application: Application) : BaseViewModel<CameraContract.Event, CameraContract.State, CameraContract.Effect>(){

    private val repository: CameraRepository by lazy{
        CameraRepository(application)
    }
    override fun createInitialState(): CameraContract.State {
        return CameraContract.State(
            postsState = CameraContract.CameraState.Idle,
        )
    }

    override fun handleEvent(event: CameraContract.Event) {
        when (event) {
            is CameraContract.Event.OnFetchCameras -> {
                fetchPosts()
            }
            is CameraContract.Event.OnDeleteItem -> {
                deletePost(event.dataName)
            }
        }
    }

    /**
     * Fetch posts
     */
    private fun fetchPosts() {
        viewModelScope.launch {
            repository.getAllFavorite().flowOn(Dispatchers.IO)
                .onStart { emit(Resource.Loading) }
                .collect {
                    when (it) {
                        is Resource.Loading -> {
                            // Set State
                            setState { copy(postsState = CameraContract.CameraState.Loading) }
                        }
                        is Resource.Empty -> {
                            // Set State
                            setState { copy(postsState = CameraContract.CameraState.Idle) }
                        }
                        is Resource.Success -> {
                            // Set State
                            setState { copy(postsState = CameraContract.CameraState.Success(posts = it.data)) }
                        }
                        is Resource.Error -> {
                            // Set Effect
                            setEffect { CameraContract.Effect.ShowError(message = it.exception.message) }
                        }
                    }
                }
        }
    }
    private fun deletePost(data:String){
        viewModelScope.launch {
            repository.deleteFavorite(data)
            MyLog.e("資料被刪")
        }
    }

    class CameraViewModelFactory(private val application: Application): ViewModelProvider.AndroidViewModelFactory(application) {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return CameraViewModel(
                application
            ) as T
        }
    }

}