package ntutifm.game.google.dataBase

import android.content.Context
import ntutifm.game.google.apiClass.Camera

class CameraRepository(context: Context) {
    private val cameraDao : CameraDao by lazy {
        MapDatabase.getDatabase(context.applicationContext).getCameraDao()
    }

    suspend fun deleteFavorite(data:String){
        cameraDao.deleteFavorite(data)
    }
    suspend fun addFavorite(data: Camera){
        cameraDao.insertFavorite(data)
    }
    suspend fun isRoadFavorite(data: String):Boolean{
        return cameraDao.isCameraFavorite(data)
    }
    suspend fun getAllCamera():List<Camera>{
        return cameraDao.getAllCamera()
    }
}