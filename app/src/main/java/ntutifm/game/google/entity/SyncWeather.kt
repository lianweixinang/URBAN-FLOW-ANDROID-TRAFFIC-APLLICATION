package ntutifm.game.google.entity

import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ntutifm.game.google.global.MyLog
import ntutifm.game.google.net.ApiCallBack
import ntutifm.game.google.net.ApiClass.Weather
import ntutifm.game.google.net.ApiManager
import ntutifm.game.google.net.ApiProcessor
import ntutifm.game.google.ui.weather.WeatherFragment

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