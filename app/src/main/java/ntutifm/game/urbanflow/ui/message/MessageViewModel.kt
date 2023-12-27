package ntutifm.game.urbanflow.ui.message

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import ntutifm.game.urbanflow.dataBase.MessageRepository
import ntutifm.game.urbanflow.entity.contract.MessageContract
import ntutifm.game.urbanflow.global.BaseViewModel
import ntutifm.game.urbanflow.global.MyLog
import ntutifm.game.urbanflow.global.Resource

class MessageViewModel(application: Application) : BaseViewModel<MessageContract.Event, MessageContract.State, MessageContract.Effect>(){

    private val repository: MessageRepository by lazy{
        MessageRepository(application)
    }
    override fun createInitialState(): MessageContract.State {
        return MessageContract.State(
            postsState = MessageContract.MessageState.Idle,
        )
    }

    override fun handleEvent(event: MessageContract.Event) {
        when (event) {
            is MessageContract.Event.OnFetchMessage -> {
                fetchPosts()
            }
            is MessageContract.Event.OnDeleteItem -> {
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
                            setState { copy(postsState = MessageContract.MessageState.Loading) }
                        }
                        is Resource.Empty -> {
                            // Set State
                            setState { copy(postsState = MessageContract.MessageState.Idle) }
                        }
                        is Resource.Success -> {
                            MyLog.e("資料流過")
                            // Set State
                            setState { copy(postsState = MessageContract.MessageState.Success(posts = it.data)) }
                        }
                        is Resource.Error -> {
                            // Set Effect
                            setEffect { MessageContract.Effect.ShowError(message = it.exception.message) }
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

    class MessageViewModelFactory(private val application: Application): ViewModelProvider.AndroidViewModelFactory(application) {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return MessageViewModel(
                application
            ) as T
        }
    }

}