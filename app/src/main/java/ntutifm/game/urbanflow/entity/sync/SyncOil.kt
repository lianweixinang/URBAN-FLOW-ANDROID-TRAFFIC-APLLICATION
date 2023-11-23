package ntutifm.game.urbanflow.entity.sync

import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ntutifm.game.urbanflow.global.MyLog
import ntutifm.game.urbanflow.net.ApiCallBack
import ntutifm.game.urbanflow.apiClass.Oil
import ntutifm.game.urbanflow.net.ApiManager
import ntutifm.game.urbanflow.net.ApiProcessor

object SyncOil {

    private val _searchLists = MutableLiveData<List<Oil>>()
    val oilLists: LiveData<List<Oil>> = _searchLists

    fun updateOil(data:List<Oil>){
        _searchLists.postValue(data)
        MyLog.e("fuckOil")
    }

    fun getOil(callBack: ApiCallBack?, fragment: Fragment){
        MyLog.e("startFuckOil")
        ApiManager(callBack).execute(fragment, ApiProcessor.getOil)
    }
}