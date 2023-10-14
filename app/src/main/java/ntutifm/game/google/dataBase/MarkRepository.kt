package ntutifm.game.google.dataBase

import android.content.Context
import ntutifm.game.google.apiClass.Camera
import ntutifm.game.google.apiClass.RoadFavorite

class CameraRepository(context: Context) {
    private val cameraDao : CameraDao by lazy {
        MapDatabase.getDatabase(context.applicationContext).getCameraDao()
    }

    fun deleteFavorite(data:String){
        cameraDao.deleteFavorite(data)
    }
    fun addFavorite(data: Camera){
        cameraDao.insertFavorite(data)
    }
    fun isRoadFavorite(data: String):Boolean{
        return cameraDao.isCameraFavorite(data)
    }
}