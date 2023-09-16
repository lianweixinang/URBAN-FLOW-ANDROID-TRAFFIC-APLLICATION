package ntutifm.game.google.entity

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ntutifm.game.google.global.MyLog
import ntutifm.game.google.net.ApiCallBack
import ntutifm.game.google.net.ApiClass.CityRoad
import ntutifm.game.google.net.ApiClass.CitySpeed
import ntutifm.game.google.net.ApiManager
import ntutifm.game.google.net.ApiProcessor

object SyncRoad {

    private val _searchLists = MutableLiveData<List<CityRoad>>()
    val searchLists: LiveData<List<CityRoad>> = _searchLists
    fun filterSearch(callBack: ApiCallBack?, data: String, fragment: Fragment){
        MyLog.e("filterSearchStart")
        ApiManager(callBack, listOf(data)).execute(fragment, ApiProcessor.getCityRoadId)
    }
    fun clearSearch(){
        _searchLists.postValue(listOf<CityRoad>())
    }

    fun updateSearch(data:List<CityRoad>){
        _searchLists.postValue(data)
    }
}