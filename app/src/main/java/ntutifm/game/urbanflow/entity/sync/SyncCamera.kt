package ntutifm.game.urbanflow.entity.sync

import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import ntutifm.game.urbanflow.global.MyLog
import ntutifm.game.urbanflow.net.ApiCallBack
import ntutifm.game.urbanflow.apiClass.Camera
import ntutifm.game.urbanflow.net.ApiManager
import ntutifm.game.urbanflow.net.ApiProcessor

object SyncCamera {

    private val _cameraLists = MutableSharedFlow<List<Camera>>()
    val cameraLists = _cameraLists.asSharedFlow()
    private val _camara = MutableLiveData<Camera>()
    val camara: LiveData<Camera> = _camara
    fun cameraMarkApi(){
        CoroutineScope(Dispatchers.IO).launch {
            MyLog.e("StartCameraMarkApi")
            ApiProcessor().getCameraMark()
        }
    }
    suspend fun updateCameraMark(data:List<Camera>){
        MyLog.e("UpdateCameraMark")
        _cameraLists.emit(data)
    }
    fun cameraFindCamera(callBack: ApiCallBack, fragment: Fragment,latLng:LatLng){
        MyLog.e("StartCallCameraTestApi")
        ApiManager(callBack,listOf(latLng.latitude.toString(),latLng.longitude.toString())).execute(fragment, ApiProcessor.getFindCamera)
    }
    fun updateFindCamera(data: Camera){
        MyLog.e("UpdateCameraTest")
        _camara.postValue(data)
    }
}