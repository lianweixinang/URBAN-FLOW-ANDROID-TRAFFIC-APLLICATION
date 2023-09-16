package ntutifm.game.google.entity

import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ntutifm.game.google.global.MyLog
import ntutifm.game.google.net.ApiCallBack
import ntutifm.game.google.net.ApiClass.CityRoad
import ntutifm.game.google.net.ApiClass.CitySpeed
import ntutifm.game.google.net.ApiClass.Incident
import ntutifm.game.google.net.ApiManager
import ntutifm.game.google.net.ApiProcessor

object SyncIncident {

    private val _searchLists = MutableLiveData<List<Incident>>()
    val incidentLists: LiveData<List<Incident>> = _searchLists
    fun filterSearch(callBack: ApiCallBack?, data: String, fragment: Fragment){
        ApiManager(callBack, listOf(data)).execute(fragment, ApiProcessor.getIncident)
    }
    fun clearSearch(){
        _searchLists.postValue(listOf<Incident>())
    }

    fun updateIncident(data:List<Incident>){
        _searchLists.postValue(data)
    }

    fun getIncident(callBack: ApiCallBack?, fragment: Fragment){
        ApiManager(callBack).execute(fragment, ApiProcessor.getIncident)
    }
}