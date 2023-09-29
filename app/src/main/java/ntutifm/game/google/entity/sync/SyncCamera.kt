package ntutifm.game.google.entity.sync

import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.LatLng
import ntutifm.game.google.global.MyLog
import ntutifm.game.google.net.ApiCallBack
import ntutifm.game.google.net.apiClass.Camera
import ntutifm.game.google.net.ApiManager
import ntutifm.game.google.net.ApiProcessor

object SyncCamera {

    private val _cameraLists = MutableLiveData<List<Camera>>()
    val cameraLists: LiveData<List<Camera>> = _cameraLists
    private val _camara = MutableLiveData<Camera>()
    val camara: LiveData<Camera> = _camara
    fun cameraMarkApi(callBack: ApiCallBack, fragment: Fragment){
        MyLog.e("StartCameraMarkApi")
        ApiManager( callBack).execute(fragment, ApiProcessor.getCameraMark)
    }
    fun updateCameraMark(data:List<Camera>){
        MyLog.e("UpdateCameraMark")
        _cameraLists.postValue(data)
    }
    fun cameraFindCamera(callBack: ApiCallBack, fragment: Fragment,latLng:LatLng){
        MyLog.e("StartCallCameraTestApi")
        ApiManager(callBack,listOf(latLng.latitude.toString(),latLng.longitude.toString())).execute(fragment, ApiProcessor.getFindCamera)
    }
    fun updateFindCamera(data:Camera){
        MyLog.e("UpdateCameraTest")
        _camara.postValue(data)
    }
}