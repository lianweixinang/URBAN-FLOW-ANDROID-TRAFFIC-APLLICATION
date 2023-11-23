package ntutifm.game.urbanflow.entity.sync

import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.LatLng
import ntutifm.game.urbanflow.apiClass.WeatherLocation
import ntutifm.game.urbanflow.global.MyLog
import ntutifm.game.urbanflow.net.ApiCallBack
import ntutifm.game.urbanflow.net.ApiManager
import ntutifm.game.urbanflow.net.ApiProcessor

object SyncPosition {
    private val _weatherLocation = MutableLiveData<WeatherLocation>()
    val weatherLocation = _weatherLocation

    fun weatherLocationApi(callBack: ApiCallBack, fragment: Fragment,latLong:LatLng){
        MyLog.e("StartCallWeatherLocationApi")
        ApiManager(callBack,listOf(latLong.latitude.toString(),latLong.longitude.toString())).execute(fragment, ApiProcessor.getWeatherLocation)
    }
    fun updateWeatherLocation(data: WeatherLocation){
        MyLog.e("updateWeatherLocation")
        _weatherLocation.postValue(data)

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