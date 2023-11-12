package ntutifm.game.urbanflow.dataBase

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ntutifm.game.urbanflow.apiClass.Camera

@Dao
interface CameraDao{

    @Insert
     suspend fun insertFavorite(vararg camera: Camera)
    @Query("DELETE FROM Camera WHERE cameraId  = :cameraId")
    suspend fun deleteFavorite(cameraId: String)
    @Query("SELECT EXISTS (SELECT 1 FROM Camera WHERE cameraId= :cameraId LIMIT 1)")
    suspend fun isCameraFavorite(cameraId: String): Boolean
    @Query("SELECT * FROM Camera")
    fun getAllCamera() : Flow<List<Camera>>
}