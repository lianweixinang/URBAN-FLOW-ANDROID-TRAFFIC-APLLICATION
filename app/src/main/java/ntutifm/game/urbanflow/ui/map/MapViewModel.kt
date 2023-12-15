package ntutifm.game.urbanflow.ui.map

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ntutifm.game.urbanflow.apiClass.CCTV
import ntutifm.game.urbanflow.apiClass.Camera
import ntutifm.game.urbanflow.apiClass.OilStation
import ntutifm.game.urbanflow.apiClass.Parking
import ntutifm.game.urbanflow.apiClass.RoadFavorite
import ntutifm.game.urbanflow.apiClass.SearchHistory
import ntutifm.game.urbanflow.dataBase.CCTVRepository
import ntutifm.game.urbanflow.dataBase.CameraRepository
import ntutifm.game.urbanflow.dataBase.OilStationRepository
import ntutifm.game.urbanflow.dataBase.ParkingRepository
import ntutifm.game.urbanflow.dataBase.RoadFavoriteRepository
import ntutifm.game.urbanflow.dataBase.SearchHistoryRepository
import ntutifm.game.urbanflow.global.MyLog
import ntutifm.game.urbanflow.net.APIResult
import ntutifm.game.urbanflow.net.ApiProcessor

class MapViewModel(application: Application) : ViewModel() {

    private val searchHistoryRepository: SearchHistoryRepository by lazy {
        SearchHistoryRepository(application)
    }
    private val roadFavoriteRepository: RoadFavoriteRepository by lazy {
        RoadFavoriteRepository(application)
    }
    private val cameraRepository: CameraRepository by lazy {
        CameraRepository(application)
    }
    private val parkingRepository: ParkingRepository by lazy {
        ParkingRepository(application)
    }
    private val oilStationRepository: OilStationRepository by lazy {
        OilStationRepository(application)
    }
    private val cctvRepository: CCTVRepository by lazy {
        CCTVRepository(application)
    }

    private val _parkingLists = MutableSharedFlow<List<Parking>>()
    val parkingLists = _parkingLists.asSharedFlow()
    private val _oilStation = MutableSharedFlow<List<OilStation>>()
    val oilStation = _oilStation.asSharedFlow()
    private val _cameraLists = MutableSharedFlow<List<Camera>>()
    val cameraLists = _cameraLists.asSharedFlow()
    private val _camera = MutableSharedFlow<Camera>()
    val camera = _camera.asSharedFlow()
    val searchHistory = searchHistoryRepository.searchHistory
    private val _favoriteState = MutableStateFlow(false)
    val favoriteState = _favoriteState.asStateFlow()
    private val _markState = MutableStateFlow(false)
    val markState = _markState.asStateFlow()
    fun deleteHistory(data: SearchHistory) {
        viewModelScope.launch(Dispatchers.Default) {
            searchHistoryRepository.deleteHistory(data)
        }
    }

    fun insertHistory(data: SearchHistory) {
        viewModelScope.launch(Dispatchers.Default) {
            searchHistoryRepository.insertHistory(data)
        }
    }

    fun checkFavorite(data: String) {
        viewModelScope.launch(Dispatchers.Default) {
            _favoriteState.value = roadFavoriteRepository.isRoadFavorite(data)
        }
    }

    fun deleteFavorite(data: String) {
        viewModelScope.launch(Dispatchers.Default) {
            roadFavoriteRepository.deleteFavorite(data)
            _favoriteState.value = false
        }
    }

    fun addFavorite(data: RoadFavorite) {
        viewModelScope.launch(Dispatchers.Default) {
            roadFavoriteRepository.addFavorite(data)
            _favoriteState.value = true
        }
    }

    fun checkMarkCamera(data: String) {
        viewModelScope.launch(Dispatchers.Default) {
            _markState.value = cameraRepository.isRoadFavorite(data)
        }
    }

    fun deleteMarkCamera(data: String) {
        viewModelScope.launch(Dispatchers.Default) {
            cameraRepository.deleteFavorite(data)
            _markState.value = false
        }
    }

    fun addMarkCamera(data: Camera) {
        viewModelScope.launch(Dispatchers.Default) {
            cameraRepository.addFavorite(data)
            _markState.value = true
        }
    }

    fun checkMarkOil(data: String) {
        viewModelScope.launch(Dispatchers.Default) {
            _markState.value = oilStationRepository.isRoadFavorite(data)
        }
    }

    fun deleteMarkOil(data: String) {
        viewModelScope.launch(Dispatchers.Default) {
            oilStationRepository.deleteFavorite(data)
            _markState.value = false
        }
    }

    fun addMarkOil(data: OilStation) {
        viewModelScope.launch(Dispatchers.Default) {
            oilStationRepository.addFavorite(data)
            _markState.value = true
        }
    }

    fun checkMarkParking(data: String) {
        viewModelScope.launch(Dispatchers.Default) {
            _markState.value = parkingRepository.isRoadFavorite(data)
        }
    }

    fun deleteMarkParking(data: String) {
        viewModelScope.launch(Dispatchers.Default) {
            parkingRepository.deleteFavorite(data)
            _markState.value = false
        }
    }

    fun addMarkParking(data: Parking) {
        viewModelScope.launch(Dispatchers.Default) {
            parkingRepository.addFavorite(data)
            _markState.value = true
        }
    }

    fun checkCCTV(data: String) {
        viewModelScope.launch(Dispatchers.Default) {
            _favoriteState.value = cctvRepository.isCCTVFavorite(data)
        }
    }

    fun deleteCCTV(data: String) {
        viewModelScope.launch(Dispatchers.Default) {
            cctvRepository.deleteFavorite(data)
            _favoriteState.value = false
        }
    }

    fun addCCTV(data: CCTV) {
        viewModelScope.launch(Dispatchers.Default) {
            cctvRepository.addFavorite(data)
            _favoriteState.value = true
        }
    }


    fun cameraMarkApi() {
        viewModelScope.launch(Dispatchers.IO) {
            MyLog.e("StartCameraMarkApi")
            ApiProcessor().getCameraMark().let { _response ->
                when (_response) {
                    is APIResult.Success -> _cameraLists.emit(_response.data)
                    is APIResult.Error -> MyLog.e(_response.exception.toString())
                }
            }
        }
    }

    fun cameraFindCamera(latLng:LatLng) {
        viewModelScope.launch(Dispatchers.IO) {
            MyLog.e("StartCameraMarkApi")
            ApiProcessor().getFindCamera(latLng).let { _response ->
                when (_response) {
                    is APIResult.Success -> _camera.emit(_response.data[0])
                    is APIResult.Error -> MyLog.e(_response.exception.toString())
                }
            }
        }
    }

    fun parkingMarkApi() {
        viewModelScope.launch(Dispatchers.IO) {
            MyLog.e("StartCameraMarkApi")
            ApiProcessor().getParking().let { _response ->
                when (_response) {
                    is APIResult.Success -> _parkingLists.emit(_response.data)
                    is APIResult.Error -> MyLog.e(_response.exception.toString())
                }
            }
        }
    }

    fun oilStationMarkApi() {
        viewModelScope.launch(Dispatchers.IO) {
            MyLog.e("StartOilStationMarkApi")
            ApiProcessor().getOilStation().let { _response ->
                when (_response) {
                    is APIResult.Success -> _oilStation.emit(_response.data)
                    is APIResult.Error -> MyLog.e(_response.exception.toString())
                }
            }
        }
    }

    class MapViewModelFactory(private val application: Application) :
        ViewModelProvider.AndroidViewModelFactory(application) {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return MapViewModel(
                application
            ) as T
        }
    }
}