package ntutifm.game.google.entity

import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ntutifm.game.google.global.MyLog
import ntutifm.game.google.net.ApiCallBack
import ntutifm.game.google.net.ApiClass.Camera
import ntutifm.game.google.net.ApiClass.Weather
import ntutifm.game.google.net.ApiManager
import ntutifm.game.google.net.ApiProcessor
import ntutifm.game.google.ui.weather.WeatherFragment

object SyncCamera {

    private val _cameraLists = MutableLiveData<List<Camera>>()
    val cameraLists: LiveData<List<Camera>> = _cameraLists

    fun cameraMarkApi(callBack: ApiCallBack, fragment: Fragment){
        MyLog.e("StartCameraMarkApi")
        ApiManager( callBack).execute(fragment, ApiProcessor.getCameraMark)
    }
    fun updateCameraMark(data:List<Camera>){
        MyLog.e("UpdateCameraMark")
        _cameraLists.postValue(data)
    }
    fun cameraTestApi(callBack: ApiCallBack, fragment: Fragment){
        MyLog.e("StartCallCameraTestApi")
        ApiManager( callBack).execute(fragment, ApiProcessor.getCameraTest)
    }
    fun updateCameraTest(data:List<Camera>){
        MyLog.e("UpdateCameraTest")
        _cameraLists.postValue(data)
    }
}