package ntutifm.game.google.ui.map

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ntutifm.game.google.apiClass.SearchHistory
import ntutifm.game.google.dataBase.SearchHistoryRepository

class MapViewModel(application: Application) : ViewModel() {
    private val searchHistoryRepository:SearchHistoryRepository by lazy{
        SearchHistoryRepository(application)
    }
    val searchHistory = searchHistoryRepository.searchHistory
    fun deleteHistory(data:SearchHistory){
        viewModelScope.launch(Dispatchers.Default) {
            searchHistoryRepository.deleteHistory(data)
        }
    }
    fun insertHistory(data:SearchHistory){
        viewModelScope.launch(Dispatchers.Default) {
            searchHistoryRepository.insertHistory(data)
        }
    }
    fun updateHistory(data:SearchHistory){
        viewModelScope.launch(Dispatchers.Default) {
            searchHistoryRepository.updateHistory(data)
        }
    }

    class MapViewModelFactory(private val application: Application): ViewModelProvider.AndroidViewModelFactory(application) {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return MapViewModel(
                application
            ) as T
        }
    }
}