package ntutifm.game.google.entity.sync

import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import ntutifm.game.google.global.MyLog
import ntutifm.game.google.net.ApiCallBack
import ntutifm.game.google.apiClass.OilStation
import ntutifm.game.google.apiClass.Parking
import ntutifm.game.google.apiClass.WeatherLocation
import ntutifm.game.google.net.ApiManager
import ntutifm.game.google.net.ApiProcessor

object SyncPosition {

    private val _parkingLists = MutableSharedFlow<List<Parking>>()
    val parkingLists = _parkingLists.asSharedFlow()

    private val _weatherLocation = MutableLiveData<WeatherLocation>()
    val weatherLocation = _weatherLocation

    private val _oilStation = MutableSharedFlow<List<OilStation>>()
    val oilStation = _oilStation.asSharedFlow()
    fun parkingApi(){
        CoroutineScope(Dispatchers.IO).launch {
            MyLog.e("StartCallParkingApi")
            ApiProcessor().getParking()
        }
    }
    suspend fun updateParking(data:List<Parking>){
        MyLog.e("updateParking")
        _parkingLists.emit(data)
    }
    fun weatherLocationApi(callBack: ApiCallBack, fragment: Fragment,latLong:LatLng){
        MyLog.e("StartCallWeatherLocationApi")
        ApiManager(callBack,listOf(latLong.latitude.toString(),latLong.longitude.toString())).execute(fragment, ApiProcessor.getWeatherLocation)
    }
    fun updateWeatherLocation(data: WeatherLocation){
        MyLog.e("updateWeatherLocation")
        _weatherLocation.postValue(data)

    }
    fun oilStationApi(){
        CoroutineScope(Dispatchers.IO).launch {
            MyLog.e("startOilStationApi")
            ApiProcessor().getOilStation()
        }
    }
    suspend fun updateOilStation(data:List<OilStation>){
        MyLog.e("updateOilStation")
        _oilStation.emit(data)
    }
    fun districtToIndex(): Int {
        if(_weatherLocation.value!=null) {
            val res = when (_weatherLocation.value?.districtName) {
                "南港區" -> 0
                "文山區" -> 1
                "萬華區" -> 2
                "大同區" -> 3
                "中正區" -> 4
                "中山區" -> 5
                "大安區" -> 6
                "信義區" -> 7
                "松山區" -> 8
                "北投區" -> 9
                "士林區" -> 10
                "內湖區" -> 11
                else -> 0
            }
            return res
        }else{
            return 0
        }
    }
}