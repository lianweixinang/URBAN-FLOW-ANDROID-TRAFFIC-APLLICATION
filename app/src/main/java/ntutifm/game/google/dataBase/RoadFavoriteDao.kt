package ntutifm.game.google.dataBase

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import ntutifm.game.google.apiClass.RoadFavorite

@Dao
interface RoadFavoriteDao {
    @Insert
    fun insertFavorite(vararg road: RoadFavorite)
    @Query("DELETE FROM RoadFavorite WHERE roadName = :roadName")
    fun deleteFavorite(roadName: String)
    @Query("SELECT EXISTS (SELECT 1 FROM RoadFavorite WHERE roadName= :roadName LIMIT 1)")
    fun isRoadFavorite(roadName: String): Boolean
}