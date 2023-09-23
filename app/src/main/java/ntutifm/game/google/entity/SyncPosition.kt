package ntutifm.game.google.entity

import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.LatLng
import ntutifm.game.google.global.MyLog
import ntutifm.game.google.net.ApiCallBack
import ntutifm.game.google.net.ApiClass.OilStation
import ntutifm.game.google.net.ApiClass.Parking
import ntutifm.game.google.net.ApiClass.Weather
import ntutifm.game.google.net.ApiClass.WeatherLocation
import ntutifm.game.google.net.ApiManager
import ntutifm.game.google.net.ApiProcessor
import ntutifm.game.google.ui.weather.WeatherFragment

object SyncPosition {

    private val _parkingLists = MutableLiveData<List<Parking>>()
    val parkingLists = _parkingLists

    private val _weatherLocation = MutableLiveData<WeatherLocation>()
    val weatherLocation = _weatherLocation

    private val _oilStation = MutableLiveData<List<OilStation>>()
    val oilStation = _oilStation
    fun parkingApi(callBack: ApiCallBack, fragment: Fragment){
        MyLog.e("StartCallParkingApi")
        ApiManager(callBack).execute(fragment, ApiProcessor.getParking)
    }
    fun updateParking(data:List<Parking>){
        MyLog.e("updateParking")
        _parkingLists.postValue(data)
    }
    fun weatherLocationApi(callBack: ApiCallBack, fragment: Fragment,latLong:LatLng){
        MyLog.e("StartCallWeatherLocationApi")
        ApiManager(callBack,listOf(latLong.latitude.toString(),latLong.longitude.toString())).execute(fragment, ApiProcessor.getWeatherLocation)
    }
    fun updateWeatherLocation(data:WeatherLocation){
        MyLog.e("updateWeatherLocation")
        _weatherLocation.postValue(data)
    }
    fun oilStationApi(callBack: ApiCallBack, fragment: Fragment){
        MyLog.e("startOilStationApi")
        ApiManager(callBack).execute(fragment, ApiProcessor.getOilStation)
    }
    fun updateOilStation(data:List<OilStation>){
        MyLog.e("updateOilStation")
        _oilStation.postValue(data)
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
                else -> -1
            }
            return res
        }else{
            return -1
        }
    }
}