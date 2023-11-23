package ntutifm.game.urbanflow.entity.sync

import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ntutifm.game.urbanflow.net.ApiCallBack
import ntutifm.game.urbanflow.apiClass.Incident
import ntutifm.game.urbanflow.net.ApiManager
import ntutifm.game.urbanflow.net.ApiProcessor

object SyncIncident {

    private val _searchLists = MutableLiveData<List<Incident>>()
    val incidentLists: LiveData<List<Incident>> = _searchLists

    fun updateIncident(data:List<Incident>){
        _searchLists.postValue(data)
    }

    fun getIncident(callBack: ApiCallBack?, fragment: Fragment){
        ApiManager(callBack).execute(fragment, ApiProcessor.getIncident)
    }
}