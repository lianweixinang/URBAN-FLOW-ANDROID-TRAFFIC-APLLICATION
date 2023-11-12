package ntutifm.game.urbanflow.entity.sync

import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ntutifm.game.urbanflow.global.MyLog
import ntutifm.game.urbanflow.net.ApiCallBack
import ntutifm.game.urbanflow.apiClass.Weather
import ntutifm.game.urbanflow.net.ApiManager
import ntutifm.game.urbanflow.net.ApiProcessor

object SyncWeather {

    private val _searchLists = MutableLiveData<List<Weather>>()
    val weatherLists: LiveData<List<Weather>> = _searchLists
    fun weatherDataApi(callBack: ApiCallBack, fragment: Fragment){
        MyLog.e("StartCallWeatherApi")
        ApiManager( callBack).execute(fragment, ApiProcessor.getWeather)
    }
    fun clearSearch(){
        _searchLists.postValue(listOf<Weather>())
    }

    fun updateWeather(data:List<Weather>){
        MyLog.e("UpdateWeather")
        _searchLists.postValue(data)
    }


}