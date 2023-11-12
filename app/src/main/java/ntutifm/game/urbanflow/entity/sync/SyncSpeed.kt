package ntutifm.game.urbanflow.entity.sync

import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ntutifm.game.urbanflow.global.MyLog
import ntutifm.game.urbanflow.net.ApiCallBack
import ntutifm.game.urbanflow.apiClass.CitySpeed
import ntutifm.game.urbanflow.net.ApiManager
import ntutifm.game.urbanflow.net.ApiProcessor

object SyncSpeed {

    private val _speedLists = MutableLiveData<List<CitySpeed>>()
    val speedLists: LiveData<List<CitySpeed>> = _speedLists

    fun updateSpeed(data: List<CitySpeed>) {
        MyLog.e("updateSpeed")
        _speedLists.postValue(data)
    }

    fun getCityRoadSpeed(callBack: ApiCallBack?, data: String, fragment: Fragment) {
        MyLog.e("startUpdateSpeed")
        ApiManager(callBack, listOf(data)).execute(fragment, ApiProcessor.getCityRoadSpeed)
    }

}