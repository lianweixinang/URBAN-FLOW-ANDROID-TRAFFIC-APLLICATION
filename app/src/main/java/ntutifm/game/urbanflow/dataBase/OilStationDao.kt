package ntutifm.game.urbanflow.dataBase

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ntutifm.game.urbanflow.apiClass.OilStation

@Dao
interface OilStationDao{

    @Insert
    suspend fun insertStation(stations: List<OilStation>)
    @Query("UPDATE OilStation SET isFavorite = 1 WHERE station = :stationName")
    suspend fun insertFavorite(stationName: String)
    @Query("UPDATE OilStation SET isFavorite = 0 WHERE station = :stationName")
    suspend fun deleteFavorite(stationName: String)
    @Query("SELECT EXISTS (SELECT 1 FROM OilStation WHERE station = :stationName AND isFavorite = 1 LIMIT 1)")
    suspend fun isStationFavorite(stationName: String): Boolean
    @Query("SELECT * FROM OilStation")
    fun getAllStation() : Flow<List<OilStation>>

    @Query("SELECT * FROM OilStation WHERE isFavorite = 1")
    fun getAllFavorite(): Flow<List<OilStation>>
}