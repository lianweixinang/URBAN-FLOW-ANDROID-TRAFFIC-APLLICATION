package ntutifm.game.google.entity

import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ntutifm.game.google.global.MyLog
import ntutifm.game.google.net.ApiCallBack
import ntutifm.game.google.net.ApiClass.Oil
import ntutifm.game.google.net.ApiManager
import ntutifm.game.google.net.ApiProcessor

object SyncOil {

    private val _searchLists = MutableLiveData<List<Oil>>()
    val oilLists: LiveData<List<Oil>> = _searchLists
    fun oilDataApi(callBack: ApiCallBack?, data: String, fragment: Fragment){
        ApiManager(callBack, listOf(data)).execute(fragment, ApiProcessor.getOil)
    }
    fun clearSearch(){
        _searchLists.postValue(listOf<Oil>())
    }

    fun updateOil(data:List<Oil>){
        _searchLists.postValue(data)
        MyLog.e("fuckOil")
    }

    fun getOil(callBack: ApiCallBack?, fragment: Fragment){
        MyLog.e("startFuckOil")
        ApiManager(callBack).execute(fragment, ApiProcessor.getOil)
    }
}