package ntutifm.game.google.dataBase

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ntutifm.game.google.apiClass.OilStation

@Dao
interface OilStationDao{

    @Insert
    suspend fun insertFavorite(vararg oilStation: OilStation)
    @Query("DELETE FROM OilStation WHERE station = :station")
    suspend fun deleteFavorite(station: String)
    @Query("SELECT EXISTS (SELECT 1 FROM OilStation WHERE station= :station LIMIT 1)")
    suspend fun isOilStationFavorite(station: String): Boolean
    @Query("SELECT * FROM OilStation")
    fun getAllStation() : Flow<List<OilStation>>
}