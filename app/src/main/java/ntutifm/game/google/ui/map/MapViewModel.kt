package ntutifm.game.google.ui.map

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ntutifm.game.google.apiClass.CCTV
import ntutifm.game.google.apiClass.Camera
import ntutifm.game.google.apiClass.OilStation
import ntutifm.game.google.apiClass.Parking
import ntutifm.game.google.apiClass.RoadFavorite
import ntutifm.game.google.apiClass.SearchHistory
import ntutifm.game.google.dataBase.CameraRepository
import ntutifm.game.google.dataBase.OilStationRepository
import ntutifm.game.google.dataBase.ParkingRepository
import ntutifm.game.google.dataBase.RoadFavoriteRepository
import ntutifm.game.google.dataBase.SearchHistoryRepository
import ntutifm.game.google.dataBase.cctvRepository

class MapViewModel(application: Application) : ViewModel() {

    private val searchHistoryRepository:SearchHistoryRepository by lazy{
        SearchHistoryRepository(application)
    }
    private val roadFavoriteRepository: RoadFavoriteRepository by lazy{
        RoadFavoriteRepository(application)
    }
    private val cameraRepository: CameraRepository by lazy{
        CameraRepository(application)
    }
    private val parkingRepository: ParkingRepository by lazy{
        ParkingRepository(application)
    }
    private val oilStationRepository: OilStationRepository by lazy{
        OilStationRepository(application)
    }
    private val cctvRepository:cctvRepository by lazy {
        cctvRepository(application)
    }
    val searchHistory = searchHistoryRepository.searchHistory
    private val _favoriteState = MutableStateFlow(false)
    val favoriteState = _favoriteState.asStateFlow()
    private val _markState = MutableStateFlow(false)
    val markState = _markState.asStateFlow()
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

    fun checkMarkCamera(data:String){
        viewModelScope.launch(Dispatchers.Default) {
            _markState.value = cameraRepository.isRoadFavorite(data)
        }
    }
    fun deleteMarkCamera(data:String){
        viewModelScope.launch(Dispatchers.Default) {
            cameraRepository.deleteFavorite(data)
            _markState.value = false
        }
    }

    fun addMarkCamera(data: Camera){
        viewModelScope.launch(Dispatchers.Default) {
            cameraRepository.addFavorite(data)
            _markState.value = true
        }
    }
    fun checkMarkOil(data:String){
        viewModelScope.launch(Dispatchers.Default) {
            _markState.value = oilStationRepository.isRoadFavorite(data)
        }
    }
    fun deleteMarkOil(data:String){
        viewModelScope.launch(Dispatchers.Default) {
            oilStationRepository.deleteFavorite(data)
            _markState.value = false
        }
    }

    fun addMarkOil(data: OilStation){
        viewModelScope.launch(Dispatchers.Default) {
            oilStationRepository.addFavorite(data)
            _markState.value = true
        }
    }
    fun checkMarkParking(data:String){
        viewModelScope.launch(Dispatchers.Default) {
            _markState.value = parkingRepository.isRoadFavorite(data)
        }
    }
    fun deleteMarkParking(data:String){
        viewModelScope.launch(Dispatchers.Default) {
            parkingRepository.deleteFavorite(data)
            _markState.value = false
        }
    }

    fun addMarkParking(data: Parking){
        viewModelScope.launch(Dispatchers.Default) {
            parkingRepository.addFavorite(data)
            _markState.value = true
        }
    }
    fun checkCCTV(data:String){
        viewModelScope.launch(Dispatchers.Default) {
            _favoriteState.value = cctvRepository.isCCTVFavorite(data)
        }
    }
    fun deleteCCTV(data:String){
        viewModelScope.launch(Dispatchers.Default) {
            cctvRepository.deleteFavorite(data)
            _markState.value = false
        }
    }
    fun addCCTV(data: CCTV){
        viewModelScope.launch(Dispatchers.Default) {
            cctvRepository.addFavorite(data)
            _markState.value = true
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