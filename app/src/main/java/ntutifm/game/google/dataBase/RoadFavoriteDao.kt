package ntutifm.game.google.dataBase

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ntutifm.game.google.apiClass.RoadFavorite

@Dao
interface RoadFavoriteDao {
    @Insert
    suspend fun insertFavorite(vararg road: RoadFavorite)

    @Query("DELETE FROM RoadFavorite WHERE roadName = :roadName")
    suspend fun deleteFavorite(roadName: String)

    @Query("SELECT EXISTS (SELECT 1 FROM RoadFavorite WHERE roadName= :roadName LIMIT 1)")
    suspend fun isRoadFavorite(roadName: String):Boolean

    @Query("SELECT * FROM RoadFavorite")
    suspend fun getAllRoadFavorite(): List<RoadFavorite>

}