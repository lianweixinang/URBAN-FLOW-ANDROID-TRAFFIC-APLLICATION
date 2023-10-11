package ntutifm.game.google.ui.map

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ntutifm.game.google.apiClass.RoadFavorite
import ntutifm.game.google.apiClass.SearchHistory
import ntutifm.game.google.dataBase.RoadFavoriteRepository
import ntutifm.game.google.dataBase.SearchHistoryRepository

class MapViewModel(application: Application) : ViewModel() {

    private val searchHistoryRepository:SearchHistoryRepository by lazy{
        SearchHistoryRepository(application)
    }
    private val roadFavoriteRepository: RoadFavoriteRepository by lazy{
        RoadFavoriteRepository(application)
    }
    val searchHistory = searchHistoryRepository.searchHistory
    private val _favoriteState = MutableStateFlow(false)
    val favoriteState = _favoriteState.asStateFlow()
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
    fun checkFavorite(data:String){
        viewModelScope.launch(Dispatchers.Default) {
            _favoriteState.emit(roadFavoriteRepository.isRoadFavorite(data))
        }
    }
    fun deleteFavorite(data:String){
        viewModelScope.launch(Dispatchers.Default) {
            roadFavoriteRepository.deleteFavorite(data)
            _favoriteState.value = false
        }
    }

    fun addFavorite(data: RoadFavorite){
        viewModelScope.launch(Dispatchers.Default) {
            roadFavoriteRepository.addFavorite(data)
            _favoriteState.value = true
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