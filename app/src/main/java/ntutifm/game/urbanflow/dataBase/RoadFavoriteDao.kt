package ntutifm.game.urbanflow.dataBase

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ntutifm.game.urbanflow.apiClass.RoadFavorite

@Dao
interface RoadFavoriteDao {
    @Insert
    suspend fun insertFavorite(vararg road: RoadFavorite)

    @Query("DELETE FROM RoadFavorite WHERE roadName = :roadName")
    suspend fun deleteFavorite(roadName: String)

    @Query("SELECT EXISTS (SELECT 1 FROM RoadFavorite WHERE roadName= :roadName LIMIT 1)")
    suspend fun isRoadFavorite(roadName: String):Boolean

    @Query("SELECT * FROM RoadFavorite")
    fun getAllRoadFavorite(): Flow<List<RoadFavorite>>

}