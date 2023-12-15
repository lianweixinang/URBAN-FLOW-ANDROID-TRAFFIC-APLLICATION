package ntutifm.game.urbanflow.dataBase

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ntutifm.game.urbanflow.apiClass.Camera

@Dao
interface CameraDao{
    @Insert
    suspend fun insertCamera(camera: List<Camera>)
    @Query("UPDATE Camera SET isFavorite = 1 WHERE cameraId= :cameraId")
    suspend fun insertFavorite(cameraId: String)
    @Query("UPDATE Camera SET isFavorite = 0 WHERE cameraId = :cameraId")
    suspend fun deleteFavorite(cameraId: String)
    @Query("SELECT EXISTS (SELECT 1 FROM Camera WHERE cameraId = :cameraId AND isFavorite = 1 LIMIT 1)")
    suspend fun isCameraFavorite(cameraId: String): Boolean
    @Query("SELECT * FROM Camera")
    fun getAllCamera() : Flow<List<Camera>>
    @Query("SELECT * FROM Camera WHERE isFavorite = 1")
    fun getAllFavorite(): Flow<List<Camera>>
}