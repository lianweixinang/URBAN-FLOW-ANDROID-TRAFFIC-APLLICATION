package ntutifm.game.google.dataBase

import android.content.Context
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ntutifm.game.google.apiClass.CCTV
import ntutifm.game.google.apiClass.Camera
import ntutifm.game.google.global.Resource

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
    suspend fun getAllCamera(): Flow<Resource<List<Camera>>> {
        return flow {
            try {
                emit(Resource.Success(cameraDao.getAllCamera()))
            } catch (ex: Exception) {
                emit(Resource.Error(ex))
            }
        }
    }
}