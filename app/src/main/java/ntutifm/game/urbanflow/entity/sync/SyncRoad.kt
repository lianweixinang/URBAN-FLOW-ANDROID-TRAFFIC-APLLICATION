package ntutifm.game.urbanflow.entity.sync

import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ntutifm.game.urbanflow.global.MyLog
import ntutifm.game.urbanflow.net.ApiCallBack
import ntutifm.game.urbanflow.apiClass.SearchHistory
import ntutifm.game.urbanflow.net.ApiManager
import ntutifm.game.urbanflow.net.ApiProcessor

object SyncRoad {

    private val _searchLists = MutableLiveData<List<SearchHistory>>()
    val searchLists: LiveData<List<SearchHistory>> = _searchLists
    fun filterSearch(callBack: ApiCallBack?, data: String, fragment: Fragment){
        MyLog.e("filterSearchStart")
        ApiManager(callBack, listOf(data)).execute(fragment, ApiProcessor.getCityRoadId)
    }
    fun clearSearch(){
        _searchLists.postValue(listOf<SearchHistory>())
    }

    fun updateSearch(data:List<SearchHistory>){
        _searchLists.postValue(data)
    }
}