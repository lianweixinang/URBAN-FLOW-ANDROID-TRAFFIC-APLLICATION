package ntutifm.game.google.dataBase

import android.nfc.Tag
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ntutifm.game.google.apiClass.Camera
import ntutifm.game.google.apiClass.CitySpeed

@Dao
interface CameraDao{

    @Insert
    fun insertFavorite(vararg camera: Camera)
    @Query("DELETE FROM Camera WHERE road = :roadName")
    fun deleteFavorite(roadName: String)
    @Query("SELECT EXISTS (SELECT 1 FROM Camera WHERE road= :roadName LIMIT 1)")
    fun isCameraFavorite(roadName: String): Boolean
}