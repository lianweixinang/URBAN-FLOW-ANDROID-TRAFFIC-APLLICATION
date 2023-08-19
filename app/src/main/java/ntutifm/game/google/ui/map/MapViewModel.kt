package ntutifm.game.google.ui.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ntutifm.game.google.global.MyLog
import ntutifm.game.google.net.ApiClass.CityRoad
import ntutifm.game.google.net.ApiClass.CitySpeed

class MapViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text
    private val _speedLists = MutableLiveData<List<CitySpeed>>()
    val speedLists: LiveData<List<CitySpeed>> = _speedLists

    fun updateSpeed(data:MutableList<CitySpeed>){
        MyLog.e("updateSpeed")
        _speedLists.postValue(data)
        speedLists.value?.get(0)?.let { MyLog.e(it.direction) }
    }
}